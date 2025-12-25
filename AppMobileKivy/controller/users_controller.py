
from kivymd.uix.list import ThreeLineAvatarIconListItem, IconLeftWidget, IconRightWidget
from kivymd.uix.textfield import MDTextField
from kivymd.uix.boxlayout import MDBoxLayout
from kivymd.uix.button import MDFlatButton
from model.DataBase import DBConnection
from kivymd.uix.dialog import MDDialog
from kivymd.uix.screen import MDScreen
from kivymd.toast import toast
from kivy.lang import Builder
from kivy.metrics import dp
from sqlite3 import Error

Builder.load_file("view/users.kv")
class UsersScreen(MDScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
        self.charger_utilisateurs()


    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")
        self.connexion = DBConnection().get_connection()
        if self.connexion:
            self.charger_utilisateurs()
        else:
            print("Erreur de connexion à la base de données")

    def charger_utilisateurs(self):
        cursor = self.connexion.cursor()
        cursor.execute("SELECT * FROM Utilisateur ORDER BY id_user ASC")
        users = cursor.fetchall()
        self.afficher_utilisateurs(users)



    def afficher_utilisateurs(self, utilisateurs):
        liste = self.ids.liste_utilisateur
        liste.clear_widgets()
        for user in utilisateurs:
            # Création d'un élément de liste complet
            ligne=ThreeLineAvatarIconListItem(
                text=user[1],
                secondary_text=str(user[2]),
                tertiary_text=f"Role : {user[5]}" ,
                font_style="H6", 
            )
            ligne.add_widget(IconLeftWidget(icon="account"))
            ligne.add_widget(
                IconRightWidget(
                    icon="trash-can-outline",
                    theme_text_color="Custom",
                    text_color=(1,0,0,1),
                    on_release=lambda btn, uid=user[0]: self.supprimer_utilisateur(uid)
                )
            )      
            liste.add_widget(ligne)




    def ajouter_utilisateur(self):
        cursor=self.connexion.cursor()
        nom = self.nom_input.text
        email = self.email_input.text
        login = self.login_input.text
        mdp = self.mdp_input.text
        role = self.role_input.text
        if not nom or not email or not login or not mdp or not role:
            toast("Veuillez remplir tous les champs SVP.")
            return
        sql = """
        INSERT INTO Utilisateur (nom, email, login, mdp, role)
        VALUES ( ?, ?, ?, ?, ?)
        """
        valeurs = (nom, email, login, mdp, role)

        try:
            cursor.execute(sql, valeurs)
            self.dialog.dismiss()
            toast("Utilisateur ajouté avec succès")
            self.connexion.commit()
            self.charger_utilisateurs()
            self.vider_champs()
            cursor.close()
        except Error as e:
            print(f"Erreur : {e}")




    def vider_champs(self):
        self.nom_input.text=""
        self.email_input.text=""
        self.login_input.text=""
        self.mdp_input.text=""
        self.role_input.text=""



    def supprimer_utilisateur(self, user_id):
        try:
            cursor=self.connexion.cursor()
            cursor.execute("DELETE FROM Utilisateur WHERE id_user = ? ", (user_id,))
            toast("Utilisateur supprimé")
            self.connexion.commit()
            self.charger_utilisateurs()
            cursor.close()
        except Error as e :
            print(f"Erreur : {e}")


    def rechercher_utilisateur(self,texte):
        cursor=self.connexion.cursor()
        texte = texte.lower().strip()
        sql = "SELECT * FROM Utilisateur WHERE LOWER(nom) LIKE ? ORDER BY id_user ASC"
        cursor.execute(sql, (texte + '%',))
        users = cursor.fetchall()
        self.afficher_utilisateurs(users)
        cursor.close()


    def enregistrer(self):
        box = MDBoxLayout(
            orientation='vertical',
            spacing=dp(10),
            padding=[dp(10)]*4,
            size_hint_y=None
        )
        box.bind(minimum_height=box.setter('height'))  

        self.nom_input = MDTextField(mode="rectangle", hint_text="Nom et Prénom", size_hint_y=None, height=dp(50))
        self.email_input = MDTextField(mode="rectangle", hint_text="Email", size_hint_y=None, height=dp(50))
        self.login_input = MDTextField(mode="rectangle", hint_text="Login", size_hint_y=None, height=dp(50))
        self.mdp_input = MDTextField(mode="rectangle", hint_text="Mot de Passe",password=True, size_hint_y=None, height=dp(50))
        self.role_input = MDTextField(mode="rectangle", hint_text="Role", size_hint_y=None, height=dp(50))

        box.add_widget(self.nom_input)
        box.add_widget(self.email_input)
        box.add_widget(self.login_input)
        box.add_widget(self.mdp_input)
        box.add_widget(self.role_input)

        scroll = MDBoxLayout(size_hint=(1, None), size=(dp(95), dp(350)))
        scroll.add_widget(box)

        self.dialog = MDDialog(
            title="Saisissez vos informations",
            type="custom",
            content_cls=scroll,
            size_hint=(0.9, None),
            buttons=[
                MDFlatButton(
                    text="Annuler", 
                    theme_text_color='Custom',
                    text_color=(1,0,0,1),
                    on_release=lambda x: self.dialog.dismiss()),

                MDFlatButton(
                    text="Enregistrer", 
                    theme_text_color='Custom',
                    text_color=(0,0,1,1),
                    on_release=lambda x: self.ajouter_utilisateur())
            ]
        )
        self.dialog.open()
    


