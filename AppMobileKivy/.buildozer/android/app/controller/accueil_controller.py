
from model.DataBase import DBConnection
from kivymd.uix.screen import MDScreen
from kivy.lang import Builder

Builder.load_file("view/accueil.kv")

class AccueilScreen(MDScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
        self.charger_accueil()

    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")
        self.connexion = DBConnection().get_connection()
        self.connexion = DBConnection().get_connection()
        if self.connexion:
            self.charger_accueil()
        else:
            print("Erreur de connexion à la base de données")

    def compteur_utilisateurs(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT COUNT(*) FROM Utilisateur")
        compteur = cursor.fetchone()[0]
        self.ids.user.text = str(compteur)
    
    def compteur_abonnements(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT COUNT(*) FROM Abonnement")
        compteur = cursor.fetchone()[0]
        self.ids.abonnement.text = str(compteur)

    def compteur_paiements(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT * FROM Paiement ")
        rows =cursor.fetchall()
        compteur=0
        for row in rows:
            compteur+=1
        
        self.ids.paiement.text = str(compteur)

    def compteur_entrees(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT COUNT(*) FROM ScannerEntrer ")
        compteur = cursor.fetchone()[0]
        self.ids.entree.text = str(compteur)
    

    def compteur_sorties(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT COUNT(*) FROM ScannerSortie WHERE DATE(dateHeureSortie) = DATE('now')")
        compteur = cursor.fetchone()[0]
        self.ids.sortie.text = str(compteur)

    def charger_accueil(self):
        self.compteur_abonnements()
        self.compteur_entrees()
        self.compteur_paiements()
        self.compteur_utilisateurs()
        self.compteur_sorties()