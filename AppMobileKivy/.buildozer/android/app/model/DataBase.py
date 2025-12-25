import sqlite3

class DBConnection:
    def __init__(self, db_path="/home/lankoandesoumailq/Bureau/AppMobileKivy/ParkingDB.db"):
        self.db_path = db_path
        self.connection = None


    def connect(self):
        try:
            self.connection = sqlite3.connect(self.db_path)
            self.connection.row_factory = sqlite3.Row  # permet d'accéder aux colonnes par nom
            print("Connexion SQLite réussie")
        except sqlite3.Error as e:
            print(f"Erreur lors de la connexion SQLite : {e}")
            self.connection = None

    def get_connection(self):
        if self.connection is None:
            self.connect()
        return self.connection

    def close(self):
        if self.connection:
            self.connection.close()
            print("Connexion SQLite fermée")
