#!/usr/bin/env python3
"""
Parse table_schema.txt, compute FK insert order, emit sample INSERT scripts.
Requires existing public.employees row id = 1 for audit columns.
Also seeds mt_employee_benefit_type (app table name) because employee_benefits_details references it.
"""
from __future__ import annotations

import json
import re
import sys
from collections import defaultdict, deque
from pathlib import Path

ROOT = Path(__file__).resolve().parent
SCHEMA_PATH = ROOT / "table_schema.txt"
OUT_DIR = ROOT / "insert_statements"

EXTERNAL = {"employees"}
N_ROWS = 15
TS = "now()"
E = 1  # employee id for created_by / last_modified_by
TARGET_TABLES = [
    "mt_country",
    "mt_state",
    "mt_district",
    "mt_taluka",
    "mt_city",
    "mt_pin_code",
    "mt_area",
]

# state_id -> country_id (matches rows_mt_state sample data)
COUNTRY_FOR_STATE = {
    1: 1,
    2: 2,
    3: 1,
    4: 1,
    5: 1,
    6: 1,
    7: 1,
    8: 1,
    9: 1,
    10: 2,
    11: 2,
    12: 3,
    13: 3,
    14: 4,
    15: 4,
}


def normalize_schema_text(raw: str) -> str:
    lines = []
    for line in raw.splitlines():
        s = line.strip()
        if s == '""':
            continue
        if s.startswith('"') and not s.startswith('"CREATE'):
            s = s[1:]
        if s.endswith('"') and len(s) > 1 and not s.endswith(')"'):
            s = s[:-1]
        s = s.replace('""', '"')
        lines.append(s)
    return "\n".join(lines)


def extract_create_blocks(text: str) -> list[tuple[str, str]]:
    """Return list of (table_name, ddl_fragment)."""
    out = []
    pat = re.compile(
        r"CREATE\s+TABLE\s+IF\s+NOT\s+EXISTS\s+public\.(\w+)\s*\(",
        re.IGNORECASE,
    )
    for m in pat.finditer(text):
        tname = m.group(1)
        start = m.start()
        open_paren = text.find("(", m.end() - 1)
        depth = 0
        i = open_paren
        while i < len(text):
            c = text[i]
            if c == "(":
                depth += 1
            elif c == ")":
                depth -= 1
                if depth == 0:
                    i += 1
                    break
            i += 1
        ddl = text[start:i]
        out.append((tname, ddl))
    return out


def parse_fks(ddl: str) -> list[tuple[str, str]]:
    pairs = []
    for m in re.finditer(
        r"FOREIGN\s+KEY\s*\(\s*(\w+)\s*\)\s*REFERENCES\s+public\.(\w+)\s*\(\s*id\s*\)",
        ddl,
        re.IGNORECASE | re.DOTALL,
    ):
        pairs.append((m.group(1), m.group(2)))
    return pairs


def split_sql_items(s: str) -> list[str]:
    """Split comma-separated SQL list while respecting nested parentheses."""
    out: list[str] = []
    buf: list[str] = []
    depth = 0
    for ch in s:
        if ch == "(":
            depth += 1
        elif ch == ")":
            depth -= 1
        if ch == "," and depth == 0:
            out.append("".join(buf).strip())
            buf = []
            continue
        buf.append(ch)
    if buf:
        out.append("".join(buf).strip())
    return out


def parse_constraints(ddl: str) -> dict:
    """
    Parse table-level constraints from DDL for validation.
    Returns: {"not_null": set[str], "unique": list[tuple[str, ...]]}
    """
    constraints = {"not_null": set(), "unique": []}
    m = re.search(r"\((.*)\)\s*$", ddl, re.IGNORECASE | re.DOTALL)
    if not m:
        return constraints
    body = m.group(1)
    items = split_sql_items(body)
    for item in items:
        up = item.upper()
        if up.startswith("CONSTRAINT "):
            um = re.search(r"UNIQUE\s*\(([^)]+)\)", item, re.IGNORECASE | re.DOTALL)
            if um:
                cols = tuple(c.strip() for c in um.group(1).split(","))
                if cols:
                    constraints["unique"].append(cols)
            continue
        cm = re.match(r"^\s*(\w+)\s+.+$", item, re.DOTALL)
        if not cm:
            continue
        col = cm.group(1)
        if "NOT NULL" in up:
            constraints["not_null"].add(col)
    return constraints


def topo_sort(tables: set[str], edges: list[tuple[str, str]]) -> list[str]:
    """edges: (parent, child) — parent must be inserted before child."""
    adj = defaultdict(set)
    indeg = {t: 0 for t in tables}
    for p, c in edges:
        if p in tables and c in tables:
            if c not in adj[p]:
                adj[p].add(c)
                indeg[c] += 1
    q = deque([t for t in tables if indeg[t] == 0])
    order = []
    while q:
        u = q.popleft()
        order.append(u)
        for v in adj[u]:
            indeg[v] -= 1
            if indeg[v] == 0:
                q.append(v)
    if len(order) != len(tables):
        return sorted(tables)
    return order


def sql_str(s: str) -> str:
    return "'" + s.replace("'", "''") + "'"


def write_file(name: str, body: str) -> None:
    p = OUT_DIR / name
    p.write_text(body, encoding="utf-8")
    print("Wrote", p)


def split_sql_values(values_sql: str) -> list[str]:
    """
    Split SQL tuple values by top-level commas.
    Example input: "(1, 'A', TIMESTAMP '2026-01-01 00:00:00', null)"
    """
    s = values_sql.strip()
    if not (s.startswith("(") and s.endswith(")")):
        raise ValueError(f"Expected SQL tuple, got: {values_sql}")
    inner = s[1:-1]
    out: list[str] = []
    buf: list[str] = []
    in_quote = False
    depth = 0
    i = 0
    while i < len(inner):
        ch = inner[i]
        if ch == "'":
            # Handle escaped single quote ('')
            if in_quote and i + 1 < len(inner) and inner[i + 1] == "'":
                buf.append("''")
                i += 2
                continue
            in_quote = not in_quote
            buf.append(ch)
            i += 1
            continue
        if not in_quote:
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth -= 1
            elif ch == "," and depth == 0:
                out.append("".join(buf).strip())
                buf = []
                i += 1
                continue
        buf.append(ch)
        i += 1
    if buf:
        out.append("".join(buf).strip())
    return out


def drop_id_from_row(values_sql: str) -> str:
    vals = split_sql_values(values_sql)
    if not vals:
        return values_sql
    return "(" + ", ".join(vals[1:]) + ")"


def validate_rows(table: str, cols: list[str], rows: list[str], constraints: dict) -> None:
    parsed = [split_sql_values(r) for r in rows]
    col_index = {c: i for i, c in enumerate(cols)}

    for i, vals in enumerate(parsed, start=1):
        if len(vals) != len(cols):
            raise ValueError(
                f"{table}: row {i} has {len(vals)} values, expected {len(cols)} for columns {cols}"
            )

    # Validate NOT NULL for explicitly inserted columns.
    for col in constraints["not_null"]:
        if col not in col_index:
            continue
        idx = col_index[col]
        for i, vals in enumerate(parsed, start=1):
            if vals[idx].strip().lower() == "null":
                raise ValueError(f"{table}: NOT NULL column '{col}' has NULL at row {i}")

    # Validate UNIQUE constraints for columns present in INSERT.
    for uniq_cols in constraints["unique"]:
        if not all(c in col_index for c in uniq_cols):
            continue
        seen: set[tuple[str, ...]] = set()
        for i, vals in enumerate(parsed, start=1):
            key = tuple(vals[col_index[c]].strip() for c in uniq_cols)
            if key in seen:
                raise ValueError(
                    f"{table}: UNIQUE violation for columns {uniq_cols} at row {i}, key={key}"
                )
            seen.add(key)


def main() -> int:
    raw = SCHEMA_PATH.read_text(encoding="utf-8", errors="replace")
    text = normalize_schema_text(raw)
    blocks = extract_create_blocks(text)
    tables = {t for t, _ in blocks}
    ddl_map = dict(blocks)

    all_fks: list[dict] = []
    constraints_by_table: dict[str, dict] = {}
    edges: list[tuple[str, str]] = []
    for t, ddl in blocks:
        constraints_by_table[t] = parse_constraints(ddl)
        for col, ref in parse_fks(ddl):
            all_fks.append({"table": t, "column": col, "references": ref})
            if ref in EXTERNAL:
                continue
            if ref not in tables:
                continue
            edges.append((ref, t))

    topo_order = topo_sort(tables, edges)
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    insert_order_explicit = TARGET_TABLES[:]
    if not set(insert_order_explicit).issubset(tables):
        print(
            "Target tables missing in parsed schema.\n"
            f"  target: {sorted(set(insert_order_explicit))}\n"
            f"  parsed: {sorted(tables)}",
            file=sys.stderr,
        )
        return 1

    pos = {t: i for i, t in enumerate(insert_order_explicit)}
    for parent, child in edges:
        if parent not in pos or child not in pos:
            continue
        if pos[parent] > pos[child]:
            print(
                f"FK order violation: {parent} must come before {child}",
                file=sys.stderr,
            )
            return 1

    (OUT_DIR / "schema_dependency_report.json").write_text(
        json.dumps(
            {
                "tables_in_schema": sorted(tables),
                "tables_generated": insert_order_explicit,
                "insert_order_topological": topo_order,
                "insert_order": insert_order_explicit,
                "foreign_keys": all_fks,
            },
            indent=2,
        ),
        encoding="utf-8",
    )

    # --- Sample row generators (15 rows each) ---
    def rows_mt_employee_type():
        types = [
            ("Clinical Doctor", True, True),
            ("Nursing Staff", True, False),
            ("Lab Technician", True, False),
            ("Radiology Tech", True, False),
            ("Pharmacist", True, False),
            ("Admin Officer", False, False),
            ("HR Executive", False, False),
            ("Accountant", False, False),
            ("IT Support", False, False),
            ("Housekeeping", False, False),
            ("Security", False, False),
            ("Receptionist", False, False),
            ("Billing Clerk", False, False),
            ("Ambulance Driver", False, False),
            ("Biomedical Engineer", True, False),
        ]
        lines = []
        for i, (et, clin, doc) in enumerate(types, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, true, false, {sql_str(et)}, {str(clin).lower()}, "
                f"{str(doc).lower() if doc is not None else 'null'}, {E}, {E})"
            )
        return lines

    def rows_mt_employee_category():
        cats = [
            ("Permanent", "PERM", 1),
            ("Contract", "CONT", 2),
            ("Visiting", "VIS", 3),
            ("Intern", "INT", 1),
            ("Trainee", "TRN", 2),
            ("Locum", "LOC", 4),
            ("Part-time", "PT", 5),
            ("On-call", "OC", 3),
            ("Probation", "PROB", 1),
            ("Senior Resident", "SR", 1),
            ("Junior Resident", "JR", 1),
            ("Fellowship", "FEL", 3),
            ("Consultant Pool", "CP", 4),
            ("Agency Staff", "AGY", 5),
            ("Outsourced", "OUT", 2),
        ]
        lines = []
        for i, (c, code, et) in enumerate(cats, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(c)}, {sql_str(code)}, false, {E}, {E}, {et})"
            )
        return lines

    def rows_mt_prefix():
        data = [
            ("Mr", "Mister"),
            ("Mrs", "Missus"),
            ("Ms", "Miss"),
            ("Dr", "Doctor"),
            ("Prof", "Professor"),
            ("Baby", "Baby Of"),
            ("Master", "Master"),
            ("Sri", "Sri"),
            ("Smt", "Shrimati"),
            ("Messrs", "Messrs"),
            ("Capt", "Captain"),
            ("Col", "Colonel"),
            ("Maj", "Major"),
            ("Rev", "Reverend"),
            ("Hon", "Honorable"),
        ]
        lines = []
        for i, (code, name) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(code)}, {sql_str(name)}, {E}, {E}, null)"
            )
        return lines

    def rows_mt_marital_status():
        codes = [
            ("S", "Single"),
            ("M", "Married"),
            ("D", "Divorced"),
            ("W", "Widowed"),
            ("SP", "Separated"),
            ("DP", "Domestic Partner"),
            ("UNK", "Unknown"),
            ("NR", "Not Reported"),
            ("C", "Civil Union"),
            ("A", "Annulled"),
            ("E", "Engaged"),
            ("L", "Legally Separated"),
            ("P", "Prefer Not Say"),
            ("I", "Interfaith Married"),
            ("R", "Remarried"),
        ]
        lines = []
        for i, (c, n) in enumerate(codes, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(c)}, {sql_str(n)}, {E}, {E})"
            )
        return lines

    def rows_mt_blood_group():
        data = [
            ("A+", "A Positive"),
            ("A-", "A Negative"),
            ("B+", "B Positive"),
            ("B-", "B Negative"),
            ("O+", "O Positive"),
            ("O-", "O Negative"),
            ("AB+", "AB Positive"),
            ("AB-", "AB Negative"),
            ("H", "Bombay"),
            ("A2", "A2 Subgroup"),
            ("A2B", "A2B Subgroup"),
            ("Oh", "Oh Variant"),
            ("Du", "Du Variant"),
            ("NAT", "Not Tested"),
            ("UNK", "Unknown"),
        ]
        lines = []
        for i, (c, n) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(c)}, {sql_str(n)}, false, {E}, {E})"
            )
        return lines

    def rows_mt_nationality():
        data = [
            ("IND", "Indian"),
            ("USA", "American"),
            ("GBR", "British"),
            ("CAN", "Canadian"),
            ("AUS", "Australian"),
            ("DEU", "German"),
            ("FRA", "French"),
            ("JPN", "Japanese"),
            ("SGP", "Singaporean"),
            ("ARE", "Emirati"),
            ("SAU", "Saudi"),
            ("NPL", "Nepalese"),
            ("BGD", "Bangladeshi"),
            ("LKA", "Sri Lankan"),
            ("PAK", "Pakistani"),
        ]
        lines = []
        for i, (c, n) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(c)}, {sql_str(n)}, {E}, {E})"
            )
        return lines

    def rows_mt_country():
        data = [
            ("IN", "India", "+91", "10"),
            ("US", "United States", "+1", "10"),
            ("GB", "United Kingdom", "+44", "10"),
            ("CA", "Canada", "+120", "10"),
            ("AU", "Australia", "+61", "9"),
            ("DE", "Germany", "+49", "11"),
            ("FR", "France", "+33", "9"),
            ("JP", "Japan", "+81", "10"),
            ("SG", "Singapore", "+65", "8"),
            ("AE", "United Arab Emirates", "+971", "9"),
            ("SA", "Saudi Arabia", "+966", "9"),
            ("NP", "Nepal", "+977", "10"),
            ("BD", "Bangladesh", "+880", "10"),
            ("LK", "Sri Lanka", "+94", "9"),
            ("PK", "Pakistan", "+92", "10"),
        ]
        lines = []
        for i, (code, name, isd, ml) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(code)}, {sql_str(name)}, false, "
                f"{str(i == 1).lower()}, {sql_str(isd)}, {sql_str(ml)}, {E}, {E})"
            )
        return lines

    def rows_mt_state():
        data = [
            ("MH", "Maharashtra", 1),
            ("TX", "Texas", 2),
            ("KA", "Karnataka", 1),
            ("GJ", "Gujarat", 1),
            ("KL", "Kerala", 1),
            ("TN", "Tamil Nadu", 1),
            ("DL", "Delhi", 1),
            ("UP", "Uttar Pradesh", 1),
            ("WB", "West Bengal", 1),
            ("CA", "California", 2),
            ("NY", "New York", 2),
            ("ENG", "England", 3),
            ("SCT", "Scotland", 3),
            ("QLD", "Queensland", 4),
            ("NSW", "New South Wales", 4),
        ]
        lines = []
        for i, (c, n, cid) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(c)}, {sql_str(n)}, {E}, {E}, {cid})"
            )
        return lines

    def rows_mt_district():
        names = [
            ("PUNE", "Pune", 1),
            ("MUM", "Mumbai Suburban", 2),
            ("HOU", "Houston", 4),
            ("BLR", "Bengaluru Urban", 3),
            ("AMD", "Ahmedabad", 4),
            ("ERN", "Ernakulam", 5),
            ("CHN", "Chennai", 6),
            ("NDL", "New Delhi", 7),
            ("LKO", "Lucknow", 8),
            ("KOL", "Kolkata", 9),
            ("LAX", "Los Angeles", 10),
            ("BRK", "Brooklyn", 11),
            ("LND", "London", 12),
            ("EDB", "Edinburgh", 13),
            ("BNE", "Brisbane", 14),
        ]
        lines = []
        for i, (c, n, sid) in enumerate(names, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(c)}, {sql_str(n)}, {E}, {E}, {sid})"
            )
        return lines

    def rows_mt_taluka():
        names = [
            ("HVL", "Haveli", 1),
            ("BWD", "Borivali", 2),
            ("SPR", "Spring", 3),
            ("WHF", "Whitefield", 4),
            ("NAR", "Naroda", 5),
            ("KCH", "Kochi City", 6),
            ("TMP", "Tambaram", 7),
            ("DWK", "Dwarka", 8),
            ("LKO_C", "Lucknow City", 9),
            ("HOW", "Howrah", 10),
            ("OCN", "Ocean", 11),
            ("MNH", "Manhattan", 12),
            ("WSM", "Westminster", 13),
            ("LEH", "Leith", 14),
            ("GC", "Gold Coast", 15),
        ]
        lines = []
        for i, (c, n, did) in enumerate(names, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, false, {sql_str(c)}, {sql_str(n)}, {E}, {E}, {did})"
            )
        return lines

    def rows_mt_city():
        names = [
            ("PUN", "Pune", 1, 1),
            ("MUM", "Mumbai", 2, 2),
            ("HOU_C", "Houston", 3, 4),
            ("BLR_C", "Bengaluru", 4, 3),
            ("AMD_C", "Ahmedabad", 5, 4),
            ("KOC", "Kochi", 6, 5),
            ("CHE", "Chennai", 7, 6),
            ("DEL", "New Delhi", 8, 7),
            ("LKO_CITY", "Lucknow", 9, 8),
            ("KOL_C", "Kolkata", 10, 9),
            ("LA", "Los Angeles", 11, 10),
            ("NYC", "New York City", 12, 11),
            ("LON", "London", 13, 12),
            ("EDI", "Edinburgh", 14, 13),
            ("BNE_C", "Brisbane", 15, 14),
        ]
        lines = []
        for i, (c, n, tid, sid) in enumerate(names, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(c)}, {sql_str(n)}, false, {E}, {E}, {tid}, {sid})"
            )
        return lines

    def rows_mt_pin_code():
        pins = [
            411001,
            400001,
            77001,
            560001,
            380001,
            682001,
            600001,
            110001,
            226001,
            700001,
            90001,
            10001,
            10001,
            10001,
            4000,
        ]
        # ensure unique pin_code
        pins = [411001 + i for i in range(N_ROWS)]
        lines = []
        for i, p in enumerate(pins, start=1):
            lines.append(f"({i}, {TS}, {TS}, true, false, {p}, {E}, {E}, {i})")
        return lines

    def rows_mt_area():
        areas = [
            "Kothrud",
            "Andheri West",
            "Downtown Houston",
            "Indiranagar",
            "Navrangpura",
            "Marine Drive Kochi",
            "T Nagar",
            "Connaught Place",
            "Hazratganj",
            "Salt Lake",
            "Santa Monica",
            "Brooklyn Heights",
            "Westminster Abbey Area",
            "Old Town",
            "Surfers Paradise",
        ]
        lines = []
        for i, a in enumerate(areas, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(a)}, false, {E}, {E}, {i})"
            )
        return lines

    def rows_mt_qualification():
        data = [
            ("MBBS", "Bachelor of Medicine"),
            ("MD", "Doctor of Medicine"),
            ("MS", "Master of Surgery"),
            ("DM", "Doctorate of Medicine"),
            ("MCH", "Magister Chirurgiae"),
            ("DNB", "Diplomate National Board"),
            ("BDS", "Bachelor Dental Surgery"),
            ("MDS", "Master Dental Surgery"),
            ("BAMS", "Ayurveda Medicine"),
            ("BPT", "Physiotherapy"),
            ("BSC_N", "BSc Nursing"),
            ("MSC_N", "MSc Nursing"),
            ("BPHARM", "Bachelor Pharmacy"),
            ("MPHARM", "Master Pharmacy"),
            ("MBA_HA", "MBA Hospital Admin"),
        ]
        lines = []
        for i, (c, n) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(c)}, false, {sql_str(n)}, {E}, {E})"
            )
        return lines

    def rows_mt_designation():
        data = [
            ("CONS", "Consultant", True, True),
            ("SR", "Senior Resident", False, False),
            ("JR", "Junior Resident", False, False),
            ("HOD", "Head of Department", True, True),
            ("NURS_M", "Nursing Manager", False, True),
            ("PHARM_H", "Pharmacy Head", False, True),
            ("ACC", "Accountant", False, True),
            ("ADM", "Administrator", True, True),
            ("IT_MGR", "IT Manager", False, False),
            ("HR_MGR", "HR Manager", False, False),
            ("LAB_H", "Lab Head", False, True),
            ("RAD_H", "Radiology Head", False, True),
            ("FRONT", "Front Office Lead", False, False),
            ("BILL", "Billing Supervisor", False, False),
            ("STORE", "Store Manager", False, True),
        ]
        lines = []
        for i, (c, d, po, pu) in enumerate(data, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(c)}, false, {sql_str(d)}, "
                f"{str(po).lower()}, {str(pu).lower()}, {E}, {E})"
            )
        return lines

    def rows_mt_bank():
        banks = [
            ("State Bank of India", "SBI001"),
            ("HDFC Bank", "HDFC002"),
            ("ICICI Bank", "ICICI003"),
            ("Axis Bank", "AXIS004"),
            ("Kotak Mahindra Bank", "KOTAK005"),
            ("Bank of Baroda", "BOB006"),
            ("Punjab National Bank", "PNB007"),
            ("Union Bank of India", "UBI008"),
            ("Canara Bank", "CAN009"),
            ("IDFC FIRST Bank", "IDFC010"),
            ("Yes Bank", "YES011"),
            ("IndusInd Bank", "INDUS012"),
            ("Federal Bank", "FED013"),
            ("South Indian Bank", "SIB014"),
            ("City Union Bank", "CUB015"),
        ]
        lines = []
        for i, (n, c) in enumerate(banks, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(n)}, {sql_str(c)}, false, {E}, {E})"
            )
        return lines

    def rows_mt_organization():
        lines = []
        for i in range(1, N_ROWS + 1):
            cid = COUNTRY_FOR_STATE[i]
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str(f'{i} MG Road, Healthcare Block')}, "
                f"9123456789{i % 10}, false, {sql_str(f'contact{i}@hims.org')}, "
                f"{sql_str(f'ORG{i:03d}')}, {sql_str(f'HIMS Partner Hospital {i}')}, "
                f"{sql_str(f'https://hosp{i}.hims.org')}, {E}, {E}, {i}, {i}, {cid}, {i}, {i}, {i}, {i})"
            )
        return lines

    def rows_mt_unit():
        """82 values matching emitters mt_unit column list (see DDL public.mt_unit)."""

        def lit(x):
            if x is None:
                return "null"
            if isinstance(x, bool):
                return "true" if x else "false"
            if isinstance(x, int):
                return str(x)
            if isinstance(x, str) and x.strip().upper().startswith("TIMESTAMP"):
                return x
            return sql_str(x)

        lines = []
        for i in range(1, N_ROWS + 1):
            cid = COUNTRY_FOR_STATE[i]
            c = [None] * 82
            c[0] = i
            c[1] = TS
            c[2] = TS
            # c[3]..c[7] ipd/opd/time — null
            c[8] = f"UHID{i:03d}"
            c[9] = True
            # c[10]..c[12]
            c[13] = False  # delete_flag
            # c[14]..c[15] discharge, email
            # c[16]..c[21] followup + flags
            c[22] = False  # is_medical_college NOT NULL
            # c[23]..c[48] remaining nullable masters through trust_registration
            c[49] = f"U{i:03d}"  # unit_abbreviation (unique)
            # c[50] unit_address
            c[51] = f"UNIT{i:03d}"
            # c[52] unit_gst_no
            c[53] = f"HIMS Clinical Unit {i}"
            c[54] = f"REG-MH{i:05d}"
            # c[55] unit_website
            c[56] = E
            c[57] = E
            # c[58] advance_type_id
            c[59] = i  # area_id
            c[60] = i  # city_id
            # c[61] client_id
            c[62] = cid  # country_id
            c[63] = i  # district_id
            c[64] = i  # organization_id
            c[65] = i  # pin_code_id
            c[66] = i  # state_id
            c[67] = i  # taluka_id
            # c[68]..c[81] nullable tail
            lines.append("(" + ", ".join(lit(x) for x in c) + ")")
        return lines

    def rows_mt_department():
        depts = [
            ("CARD", "CARDIO", "Cardiology", True),
            ("NEPH", "NEPHRO", "Nephrology", True),
            ("ORTH", "ORTHO", "Orthopedics", True),
            ("PEDS", "PEDS", "Pediatrics", True),
            ("OBG", "OBGYN", "Obstetrics Gynecology", True),
            ("RAD", "RADIO", "Radiology", True),
            ("PATH", "PATH", "Pathology", True),
            ("EMER", "EMERG", "Emergency", True),
            ("SURG", "SURG", "General Surgery", True),
            ("DERM", "DERM", "Dermatology", True),
            ("ADMIN", "ADMIN", "Administration", False),
            ("HR", "HR", "Human Resources", False),
            ("FIN", "FIN", "Finance", False),
            ("IT", "IT", "Information Technology", False),
            ("CSSD", "CSSD", "Central Sterile", True),
        ]
        lines = []
        for i, (code, cst, name, clin) in enumerate(depts, start=1):
            lines.append(
                f"({i}, {TS}, {TS}, true, {sql_str('ALL')}, false, {sql_str(code)}, "
                f"{sql_str(cst)}, {sql_str(name)}, {str(clin).lower()}, null, {E}, {E})"
            )
        return lines

    def rows_mt_department_unit_mapping():
        lines = []
        for i in range(1, N_ROWS + 1):
            lines.append(f"({i}, {i}, true, true, true, true, {i}, true)")
        return lines

    def rows_employee_benefits_details():
        lines = []
        for i in range(1, N_ROWS + 1):
            lines.append(
                f"({i}, true, false, 50000.0, 10.0, true, 1000000 + {i}, "
                f"{str(i % 2 == 0).lower()}, 1000.0 * {i}, "
                f"'2026-01-01'::date, '2027-12-31'::date, {i}, {E}, null)"
            )
        return lines

    emitters = {
        "mt_employee_type": (
            "id, created_date, last_modified_date, active, applicable_to_doctor_type, "
            "delete_flag, employee_type, is_clinical, is_doctor, created_by, last_modified_by",
            rows_mt_employee_type,
        ),
        "mt_employee_category": (
            "id, created_date, last_modified_date, active, category, code, delete_flag, "
            "created_by, last_modified_by, employee_type_id",
            rows_mt_employee_category,
        ),
        "mt_prefix": (
            "id, created_date, last_modified_date, active, delete_flag, prefix_code, "
            "prefix_name, created_by, last_modified_by, gender_prefix",
            rows_mt_prefix,
        ),
        "mt_marital_status": (
            "id, created_date, last_modified_date, active, delete_flag, marital_status_code, "
            "marital_status_name, created_by, last_modified_by",
            rows_mt_marital_status,
        ),
        "mt_blood_group": (
            "id, created_date, last_modified_date, active, blood_group_code, blood_group_name, "
            "delete_flag, created_by, last_modified_by",
            rows_mt_blood_group,
        ),
        "mt_nationality": (
            "id, created_date, last_modified_date, active, delete_flag, nationality_code, "
            "nationality_name, created_by, last_modified_by",
            rows_mt_nationality,
        ),
        "mt_country": (
            "id, created_date, last_modified_date, active, country_code, country_name, "
            "delete_flag, is_default, isd_code, mobile_length, created_by, last_modified_by",
            rows_mt_country,
        ),
        "mt_state": (
            "id, created_date, last_modified_date, active, delete_flag, state_code, state_name, "
            "created_by, last_modified_by, country_id",
            rows_mt_state,
        ),
        "mt_district": (
            "id, created_date, last_modified_date, active, delete_flag, district_code, "
            "district_name, created_by, last_modified_by, state_id",
            rows_mt_district,
        ),
        "mt_taluka": (
            "id, created_date, last_modified_date, active, delete_flag, taluka_code, "
            "taluka_name, created_by, last_modified_by, district_id",
            rows_mt_taluka,
        ),
        "mt_city": (
            "id, created_date, last_modified_date, active, city_code, city_name, delete_flag, "
            "created_by, last_modified_by, taluka_id, state_id",
            rows_mt_city,
        ),
        "mt_pin_code": (
            "id, created_date, last_modified_date, active, delete_flag, pin_code, "
            "created_by, last_modified_by, city_id",
            rows_mt_pin_code,
        ),
        "mt_area": (
            "id, created_date, last_modified_date, active, area, delete_flag, created_by, "
            "last_modified_by, pincode_id",
            rows_mt_area,
        ),
        "mt_qualification": (
            "id, created_date, last_modified_date, active, code, delete_flag, "
            "qualification_name, created_by, last_modified_by",
            rows_mt_qualification,
        ),
        "mt_designation": (
            "id, created_date, last_modified_date, active, code, delete_flag, "
            "designation_description, po_approval_authority, purchase_approval_authority, "
            "created_by, last_modified_by",
            rows_mt_designation,
        ),
        "mt_bank": (
            "id, created_date, last_modified_date, active, bank_name, code, delete_flag, "
            "created_by, last_modified_by",
            rows_mt_bank,
        ),
        "mt_organization": (
            "id, created_date, last_modified_date, active, address, contact, delete_flag, "
            "email, organization_code, organization_name, website, created_by, "
            "last_modified_by, area_id, city_id, country_id, district_id, pin_code_id, state_id, "
            "taluka_id",
            rows_mt_organization,
        ),
        "mt_unit": (
            "id, created_date, last_modified_date, ipdbill_round_up, opdbill_round_up, "
            "ot_end_time, ot_start_time, pharmacy_bill_round_up, uhidprefix, active, "
            "auto_lock_indent, billing_cash_limit, contact_no, delete_flag, "
            "discharge_approval_sequence_applicable, email_id, followup_days, "
            "is_collection_center_only, is_cradle_charges_applicable, is_general_instruction, "
            "is_investigation_allow, is_local_instruction, is_medical_college, is_out_sourced, "
            "is_prepaid, logo_image_path, max_emergency_assessment_time, max_ipd_assessment_time, "
            "nabh_valid_from, nabh_valid_to, nabl_valid_from, nabl_valid_to, no_followup_days, "
            "pan_card_amount_limit, pan_number, path_order_create, path_signature_authorization, "
            "pharmacy_cash_limit, pharmacy_contact, pharmacy_email, pharmacy_gst_no, "
            "pharmacy_license_no, pharmacy_logo_image_path, pharmacy_name, "
            "po_approval_sequence_applicable, print_case_paper, radio_order_create, "
            "set_rates_as_zero, trust_registration, unit_abbreviation, unit_address, unit_code, "
            "unit_gst_no, unit_name, registration_no, unit_website, created_by, last_modified_by, "
            "advance_type_id, area_id, city_id, client_id, country_id, district_id, "
            "organization_id, pin_code_id, state_id, taluka_id, ipd_advance_type_id, "
            "opd_advance_type_id, is_ipd_print_with_annexure, is_lab_technician_required, "
            "auto_patch_machine_values, is_mother_ans_child_care, currency, currency_description, "
            "biometric_limit, is_group_wise_bill, is_mother_and_child_care, "
            "print_header_image_path, bed_release_at_allow_to_go, grade_wise_surgery_charges_applicable",
            rows_mt_unit,
        ),
        "mt_department": (
            "id, created_date, last_modified_date, active, applicable_to, delete_flag, "
            "department_code, department_constant, department_name, dept_is_clinical, "
            "image_path, created_by, last_modified_by",
            rows_mt_department,
        ),
        "mt_department_unit_mapping": (
            "department_id, unit_id, applicable_for_allow_to_go_flag, "
            "applicable_for_discharge_approval, is_mandatory, is_notify, sequence, "
            "is_appointment_booking",
            rows_mt_department_unit_mapping,
        ),
        "employee_benefits_details": (
            "id, benefit_auto_renew, benefit_carry_forward, credit_amount, credit_percentage, "
            "including_pharmacy, insurance_number, is_group, utilized_amount, valid_from, "
            "valid_till, benefit_type_id, employee_id, insurance_company_id",
            rows_employee_benefits_details,
        ),
    }

    for t in insert_order_explicit:
        if t not in emitters:
            print("Missing emitter for", t, file=sys.stderr)
            return 1
        cols, fn = emitters[t]
        row_values = fn()
        col_list = [c.strip() for c in cols.split(",")]
        has_id = bool(col_list) and col_list[0].lower() == "id"
        if has_id:
            cols = ", ".join(col_list[1:])
            row_values = [drop_id_from_row(v) for v in row_values]
            col_list = col_list[1:]
        validate_rows(t, col_list, row_values, constraints_by_table.get(t, {"not_null": set(), "unique": []}))
        vals = ",\n".join(row_values)
        body = (
            f"-- Sample data for public.{t} ({N_ROWS} rows)\n"
            f"-- Assumes public.employees.id = {E} exists for audit columns.\n\n"
            f"INSERT INTO public.{t} ({cols})\nVALUES\n{vals};\n"
        )
        write_file(f"{t}_insert.sql", body)

    # run_all_inserts.sql
    run_order = insert_order_explicit
    lines = [
        "-- Master script: run from this directory, e.g. psql -v ON_ERROR_STOP=1 -f run_all_inserts.sql",
        f"-- Requires existing row public.employees(id) = {E}.",
        "",
        "\\set ON_ERROR_STOP on",
    ]
    for t in run_order:
        lines.append(f"\\echo 'Seeding {t}...'")
        lines.append(f"\\i {t}_insert.sql")
        lines.append("")
    write_file("run_all_inserts.sql", "\n".join(lines))

    print("Insert order:", " -> ".join(insert_order_explicit))
    print("Done.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
