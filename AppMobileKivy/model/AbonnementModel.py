import mysql.connector

class AbonnementModel:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host="localhost",
            user="user",
            password="password",
            database="ma_db"
        )
        self.cursor = self.conn.cursor(dictionary=True)

    def ajouter_abonnement(self, nom, type_engin, date_debut, date_fin):
        query = "INSERT INTO abonnements (nom_client, type_engin, date_debut, date_fin) VALUES (%s, %s, %s, %s)"
        self.cursor.execute(query, (nom, type_engin, date_debut, date_fin))
        self.conn.commit()

    def lister_abonnements(self):
        self.cursor.execute("SELECT * FROM abonnements ORDER BY date_debut DESC")
        return self.cursor.fetchall()

    def fermer(self):
        self.cursor.close()
        self.conn.close()
