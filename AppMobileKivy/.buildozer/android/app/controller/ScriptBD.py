import sqlite3

# Chemin vers ta base de données
db_path = "ParkingDB.db"

# Connexion
conn = sqlite3.connect(db_path)
cursor = conn.cursor()

# Liste des scripts SQL
tables_sql = [
"""

CREATE TABLE IF NOT EXISTS Utilisateur (
    id_user INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    login TEXT  NOT NULL,
    mdp TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('admin','agent','client'))
)
""",
"""
CREATE TABLE IF NOT EXISTS Engin (
    id_engin INTEGER PRIMARY KEY AUTOINCREMENT,
    id_user INTEGER NOT NULL,
    type TEXT,
    plaque TEXT UNIQUE,
    FOREIGN KEY (id_user) REFERENCES Utilisateur(id_user) ON DELETE CASCADE
)
""",
"""
CREATE TABLE IF NOT EXISTS Ticket (
    id_ticket INTEGER PRIMARY KEY AUTOINCREMENT,
    id_engin INTEGER NOT NULL,
    reference TEXT NOT NULL UNIQUE,
    dateHeureEntree TEXT NOT NULL,
    imageQR BLOB NOT NULL,
    statut TEXT DEFAULT 'actif' CHECK(statut IN ('actif','expiré','annulé')),
    FOREIGN KEY (id_engin) REFERENCES Engin(id_engin) ON DELETE CASCADE
)
""",
"""
CREATE TABLE IF NOT EXISTS ScannerEntrer (
    id_entree INTEGER PRIMARY KEY AUTOINCREMENT,
    id_engin INTEGER NOT NULL,
    dateHeureEntree TEXT NOT NULL,
    FOREIGN KEY (id_engin) REFERENCES Engin(id_engin) ON DELETE CASCADE
)
""",
"""
CREATE TABLE IF NOT EXISTS ScannerSortie (
    id_sortie INTEGER PRIMARY KEY AUTOINCREMENT,
    id_ticket INTEGER NOT NULL,
    dateHeureSortie TEXT NOT NULL,
    FOREIGN KEY (id_ticket) REFERENCES Ticket(id_ticket) ON DELETE CASCADE
)
""",
"""
CREATE TABLE IF NOT EXISTS Paiement (
    id_paiement INTEGER PRIMARY KEY AUTOINCREMENT,
    montant REAL NOT NULL,
    modePaiement TEXT NOT NULL CHECK(modePaiement IN ('espèces','orange money','moov money')),
    datePaiement TEXT NOT NULL
)
""",
"""
CREATE TABLE IF NOT EXISTS Abonnement (
    id_abonnement INTEGER PRIMARY KEY AUTOINCREMENT,
    id_user INTEGER NOT NULL,
    id_paiement INTEGER,
    dateDebut TEXT NOT NULL,
    dateFin TEXT NOT NULL,
    statut TEXT CHECK(statut IN ('actif','expiré','suspendu')) DEFAULT 'actif',
    FOREIGN KEY (id_user) REFERENCES Utilisateur(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_paiement) REFERENCES Paiement(id_paiement) ON DELETE SET NULL
)
"""
]

# Exécution de chaque script
for sql in tables_sql:
    cursor.execute(sql,)

conn.commit()
conn.close()

print("Toutes les tables ont été créées avec succès !")