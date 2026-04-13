#!/usr/bin/env python3
from __future__ import annotations

import json
import re
from collections import defaultdict, deque
from pathlib import Path

ROOT = Path(__file__).resolve().parent
ENTITY_DIR = ROOT.parent / "java" / "com" / "hims" / "masters" / "common" / "entity"
OUT_DIR = ROOT / "insert_statements"
N_ROWS = 15
EMPLOYEE_ID = 1
TARGET_TABLES = [
    "mt_country",
    "mt_state",
    "mt_district",
    "mt_taluka",
    "mt_city",
    "mt_pincode",
    "mt_area",
]

AUDIT_COLS = ["created_date", "last_modified_date", "created_by", "last_modified_by"]


def camel_to_snake(name: str) -> str:
    return re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()


def sql_quote(v: str) -> str:
    return "'" + v.replace("'", "''") + "'"


def parse_entity_file(path: Path) -> dict:
    txt = path.read_text(encoding="utf-8")
    class_name = re.search(r"public\s+class\s+(\w+)", txt).group(1)
    table = re.search(r'@Table\s*\(\s*name\s*=\s*"([^"]+)"', txt).group(1)
    fields = []
    for m in re.finditer(r"((?:\s*@[\w()., =\"{}]+(?:\r?\n))+)\s*private\s+([\w<>]+)\s+(\w+)\s*;", txt):
        ann, ftype, fname = m.group(1), m.group(2), m.group(3)
        if "@Id" in ann:
            continue
        col = None
        cm = re.search(r'@Column\s*\(\s*name\s*=\s*"([^"]+)"', ann)
        if cm:
            col = cm.group(1)
        jm = re.search(r'@JoinColumn\s*\(\s*name\s*=\s*"([^"]+)"', ann)
        rel = None
        if jm:
            col = jm.group(1)
            rel = ftype
        if not col:
            col = camel_to_snake(fname)
        fields.append(
            {
                "field": fname,
                "column": col,
                "type": ftype,
                "not_null": "@NotNull" in ann,
                "unique": "unique = true" in ann,
                "relation": rel,
            }
        )
    # BaseEntity inherited columns for all entities
    fields.extend(
        [
            {"field": "createdDate", "column": "created_date", "type": "Timestamp", "not_null": True, "unique": False, "relation": None},
            {"field": "lastModifiedDate", "column": "last_modified_date", "type": "Timestamp", "not_null": True, "unique": False, "relation": None},
            {"field": "createdBy", "column": "created_by", "type": "Long", "not_null": False, "unique": False, "relation": None},
            {"field": "lastModifiedBy", "column": "last_modified_by", "type": "Long", "not_null": False, "unique": False, "relation": None},
        ]
    )
    return {"class": class_name, "table": table, "fields": fields}


def topo_order(entities: dict[str, dict]) -> tuple[list[str], list[dict]]:
    edges = []
    indeg = {c: 0 for c in entities}
    adj = defaultdict(set)
    for cls, meta in entities.items():
        for f in meta["fields"]:
            if f["relation"] and f["relation"] in entities:
                p, c = f["relation"], cls
                edges.append({"table": meta["table"], "column": f["column"], "references": entities[p]["table"]})
                if c not in adj[p]:
                    adj[p].add(c)
                    indeg[c] += 1
    q = deque([k for k, v in indeg.items() if v == 0])
    out = []
    while q:
        n = q.popleft()
        out.append(n)
        for nx in adj[n]:
            indeg[nx] -= 1
            if indeg[nx] == 0:
                q.append(nx)
    if len(out) != len(entities):
        out = sorted(entities.keys())
    return out, edges


CUSTOM = {
    "mt_country": {
        "country_code": ["IN", "US", "GB", "CA", "AU", "DE", "FR", "JP", "SG", "AE", "SA", "NP", "BD", "LK", "PK"],
        "country_name": ["India", "United States", "United Kingdom", "Canada", "Australia", "Germany", "France", "Japan", "Singapore", "United Arab Emirates", "Saudi Arabia", "Nepal", "Bangladesh", "Sri Lanka", "Pakistan"],
        "isd_code": ["+91", "+1", "+44", "+120", "+61", "+49", "+33", "+81", "+65", "+971", "+966", "+977", "+880", "+94", "+92"],
        "mobile_length": ["10", "10", "10", "10", "9", "11", "9", "10", "8", "9", "9", "10", "10", "9", "10"],
    },
    "mt_state": {
        "state_code": ["MH", "TX", "ENG", "ON", "NSW", "BW", "IDF", "TK", "SG", "DXB", "RUH", "BAG", "DHK", "WP", "PB"],
        "state_name": ["Maharashtra", "Texas", "England", "Ontario", "New South Wales", "Baden-Württemberg", "Île-de-France", "Tokyo Prefecture", "Singapore Region", "Dubai Emirate", "Riyadh Province", "Bagmati", "Dhaka Division", "Western Province", "Punjab"],
    },
    "mt_district": {
        "district_code": ["PUN", "HCO", "GLN", "TOR", "SYD", "STM", "PAR", "TKC", "SGC", "DBD", "RYD", "KTM", "DHK", "CMB", "LHR"],
        "district_name": ["Pune District", "Harris County", "Greater London", "Toronto District", "Sydney District", "Stuttgart District", "Paris District", "Tokyo Central", "Singapore Central", "Dubai District", "Riyadh District", "Kathmandu District", "Dhaka District", "Colombo District", "Lahore District"],
    },
    "mt_taluka": {
        "taluka_code": ["HVL", "SPR", "WST", "ETY", "PRM", "MTT", "LDF", "CHY", "DWT", "DER", "OLY", "LTP", "DMD", "FRT", "MDL"],
        "taluka_name": ["Haveli", "Spring", "Westminster", "Etobicoke", "Parramatta", "Mitte", "La Défense", "Chiyoda", "Downtown", "Deira", "Olaya", "Lalitpur", "Dhanmondi", "Fort", "Model Town"],
    },
    "mt_city": {
        "city_code": ["PUN", "HOU", "LON", "TOR", "SYD", "STR", "PAR", "TYO", "SGP", "DXB", "RUH", "KTM", "DAC", "CMB", "LHE"],
        "city_name": ["Pune", "Houston", "London", "Toronto", "Sydney", "Stuttgart", "Paris", "Tokyo", "Singapore", "Dubai", "Riyadh", "Kathmandu", "Dhaka", "Colombo", "Lahore"],
    },
    "mt_pincode": {
        "pincode": ["411001", "77001", "SW1A1AA", "M5H2N2", "2000", "70173", "75001", "1000001", "018989", "00000", "12271", "44600", "1205", "00100", "54000"],
    },
    "mt_area": {
        "area_code": ["KOT", "DWT", "WMS", "DWTN", "CBD", "MIT", "LDF", "AKB", "MRN", "BJR", "OLY", "THM", "GLS", "SLV", "GUL"],
        "area_name": ["Kothrud", "Downtown", "Westminster", "Downtown Toronto", "Sydney CBD", "Mitte Center", "La Défense Hub", "Akihabara", "Marina Bay", "Burj Area", "Olaya Business", "Thamel", "Gulshan", "Slave Island", "Gulberg"],
    },
}


def value_for(table: str, col: str, ftype: str, i: int, rel: str | None, class_to_idx: dict[str, int]) -> str:
    if col == "created_date" or col == "last_modified_date":
        return "now()"
    if col == "created_by" or col == "last_modified_by":
        return str(EMPLOYEE_ID)
    if rel:
        return str(i)
    if table in CUSTOM and col in CUSTOM[table]:
        return sql_quote(CUSTOM[table][col][i - 1])
    if col == "active":
        return "true"
    if col == "delete_flag":
        return "false"
    if col == "is_default":
        return "true" if i == 1 else "false"
    if "code" in col:
        return sql_quote(f"{col[:3].upper()}{i:03d}")
    if "name" in col:
        return sql_quote(f"{table}_{col}_{i}")
    if ftype in {"Long", "Integer", "int", "long"}:
        return str(i)
    if ftype == "Boolean" or ftype == "boolean":
        return "true"
    return sql_quote(f"{col}_{i}")


def validate(table: str, cols: list[dict], rows: list[list[str]]) -> None:
    for f in cols:
        if f["not_null"]:
            idx = cols.index(f)
            for r, row in enumerate(rows, 1):
                if row[idx].lower() == "null":
                    raise ValueError(f"{table}.{f['column']} NULL at row {r}")
    for f in [c for c in cols if c["unique"]]:
        idx = cols.index(f)
        seen = set()
        for r, row in enumerate(rows, 1):
            v = row[idx]
            if v in seen:
                raise ValueError(f"{table}.{f['column']} duplicate at row {r}: {v}")
            seen.add(v)


def main() -> int:
    entity_files = sorted(ENTITY_DIR.glob("*.java"))
    entities = {}
    for ef in entity_files:
        meta = parse_entity_file(ef)
        if meta["table"] in TARGET_TABLES:
            entities[meta["class"]] = meta

    order_classes, fk_edges = topo_order(entities)
    discovered = [entities[c]["table"] for c in order_classes]
    # Keep business-requested sequence while still validating FK order from entities.
    order_tables = [t for t in TARGET_TABLES if t in discovered]
    cls_by_table = {v["table"]: k for k, v in entities.items()}
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    for old in OUT_DIR.glob("*_insert.sql"):
        old.unlink()

    # Validate FK order for selected hierarchy.
    pos = {t: i for i, t in enumerate(order_tables)}
    for e in fk_edges:
        if e["references"] in pos and e["table"] in pos and pos[e["references"]] > pos[e["table"]]:
            raise ValueError(
                f"FK order violation: {e['references']} must come before {e['table']}"
            )

    report = {
        "source": "entities",
        "entity_path": str(ENTITY_DIR),
        "tables_generated": order_tables,
        "foreign_keys": fk_edges,
    }
    (OUT_DIR / "schema_dependency_report.json").write_text(json.dumps(report, indent=2), encoding="utf-8")

    # Relationship maintenance file for requested hierarchy.
    relationship_maintain = {
        "sequence": [
            {"order": 1, "table": "mt_country", "depends_on": []},
            {"order": 2, "table": "mt_state", "depends_on": ["mt_country"]},
            {"order": 3, "table": "mt_district", "depends_on": ["mt_state"]},
            {"order": 4, "table": "mt_taluka", "depends_on": ["mt_district"]},
            {"order": 5, "table": "mt_city", "depends_on": ["mt_taluka", "mt_state (derived via taluka->district->state)"]},
            {"order": 6, "table": "mt_pincode", "depends_on": ["mt_city"]},
            {"order": 7, "table": "mt_area", "depends_on": ["mt_pincode"]},
        ],
        "constraints_considered": ["PK(id auto-generated)", "FK(@JoinColumn)", "NOT NULL(@NotNull)", "UNIQUE(@Column(unique=true))"],
    }
    (OUT_DIR / "relationship_maintain_report.json").write_text(
        json.dumps(relationship_maintain, indent=2), encoding="utf-8"
    )

    for table in order_tables:
        cls = cls_by_table[table]
        meta = entities[cls]
        cols = [f for f in meta["fields"] if f["column"] != "id"]
        # Enforce audit timestamp columns first for all SQL statements.
        first_cols = {"created_date": 0, "last_modified_date": 1}
        cols.sort(key=lambda f: first_cols.get(f["column"], 10))
        rows = []
        for i in range(1, N_ROWS + 1):
            row = [value_for(table, f["column"], f["type"], i, f["relation"], {}) for f in cols]
            rows.append(row)
        validate(table, cols, rows)

        cols_sql = ", ".join(f["column"] for f in cols)
        vals_sql = ",\n".join("(" + ", ".join(v for v in row) + ")" for row in rows)
        body = (
            f"-- Sample data for public.{table} ({N_ROWS} rows)\n"
            f"-- Entity source: {cls}.java; id is server-generated.\n"
            f"-- Assumes public.employees.id = {EMPLOYEE_ID} exists for audit columns.\n\n"
            f"INSERT INTO public.{table} ({cols_sql})\nVALUES\n{vals_sql};\n"
        )
        p = OUT_DIR / f"{table}_insert.sql"
        p.write_text(body, encoding="utf-8")
        print("Wrote", p)

    lines = [
        "-- Master script generated from common entities",
        f"-- Entity path: {ENTITY_DIR}",
        "\\set ON_ERROR_STOP on",
        "",
    ]
    for t in order_tables:
        lines.append(f"\\echo 'Seeding {t}...'")
        lines.append(f"\\i {t}_insert.sql")
        lines.append("")
    (OUT_DIR / "run_all_inserts.sql").write_text("\n".join(lines), encoding="utf-8")
    print("Wrote", OUT_DIR / "run_all_inserts.sql")
    print("Insert order:", " -> ".join(order_tables))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
