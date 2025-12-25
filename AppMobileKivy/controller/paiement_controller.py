
from kivymd.uix.list import ThreeLineAvatarIconListItem, IconLeftWidget, IconRightWidget
from kivymd.uix.screen import MDScreen
from model.DataBase import DBConnection
from kivymd.toast import toast
from kivy.lang import Builder
from sqlite3 import Error

Builder.load_file("view/paiement.kv")
class PaiementScreen(MDScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion=DBConnection().get_connection()
        self.charger_paiement()


    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")
        self.connexion = DBConnection().get_connection()
        if self.connexion:
            self.charger_paiement()
        else:
            print("Erreur de connexion à la base de données")

    def charger_paiement(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT * FROM Paiement ORDER BY id_paiement ASC")
        paiements = cursor.fetchall()
        self.afficher_paiement(paiements)



    def afficher_paiement(self, paiements):
        liste = self.ids.liste_paiement
        liste.clear_widgets()
        for paiement in paiements:
            ligne=ThreeLineAvatarIconListItem(
                text=f"Paiement du : {paiement[3]}",
                secondary_text=f"Mode : {paiement[2]}",
                tertiary_text=f"Montant : {paiement[1]} FCFA" ,
                font_style="H6"
            )
            ligne.add_widget(IconLeftWidget(icon="currency-eur"))
            ligne.add_widget(
                IconRightWidget(
                    icon="trash-can-outline",
                    theme_text_color="Custom",
                    text_color=(1,0,0,1),
                    on_release=lambda btn, uid=paiement[0]: self.supprimer_paiement(uid)
                )
            )
            liste.add_widget(ligne)

    def supprimer_paiement(self, paiement_id):
        try:
            cursor=self.connexion.cursor()
            cursor.execute("DELETE FROM Paiement WHERE id_paiement = ?", (paiement_id,))
            toast("Paiement supprimé")
            self.connexion.commit()
            self.charger_Paiements()
            cursor.close()
        except Error as e : 
            print(f"Erreur : {e}")


    def rechercher_paiement(self,texte):
        try:
            cursor=self.connexion.cursor()
            texte = texte.lower().strip()
            sql = "SELECT * FROM Paiement WHERE LOWER(modePaiement) LIKE ? ORDER BY id_paiement ASC"
            cursor.execute(sql, (texte + '%',))
            paiements = cursor.fetchall()
            self.afficher_paiement(paiements)
            cursor.close()
        except Error as e :
            print(f"Erreur : {e}")


    