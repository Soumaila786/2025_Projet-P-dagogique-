
from kivymd.uix.list import ThreeLineAvatarIconListItem, IconLeftWidget, IconRightWidget
from PIL import Image, ImageDraw, ImageFont
from kivymd.uix.textfield import MDTextField
from kivymd.uix.boxlayout import MDBoxLayout
from kivymd.uix.button import MDFlatButton
from kivymd.uix.scrollview import ScrollView
from model.DataBase import DBConnection
from kivymd.uix.screen import MDScreen
from kivymd.uix.dialog import MDDialog
from kivymd.toast import toast
from kivy.lang import Builder
from kivy.metrics import dp
from sqlite3 import Error
import qrcode

Builder.load_file("view/abonnement.kv")

class AbonnementScreen(MDScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
        self.charger_abonnements()

    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")

        if self.connexion:
            self.charger_abonnements()
        else:
            print("Erreur de connexion à la base de données")


    def charger_abonnements(self):
        try:     
            cursor = self.connexion.cursor()
            # Affichage avec jointures pour afficher nom et type_engin, num_plaque, mode_paiement
            sql="""
                SELECT a.id_abonnement, u.nom, a.statut, a.dateDebut, a.dateFin
                FROM Abonnement a
                JOIN Utilisateur u ON a.id_user = u.id_user
                ORDER BY a.id_abonnement ASC
            """
            cursor.execute(sql)
            abonnements = cursor.fetchall()
            self.afficher_abonnement(abonnements)
            cursor.close()
        except Error as e:
            print(f"Erreur : {e}")


    def afficher_abonnement(self, abonnements):
        ligne = self.ids.liste_abonnement
        ligne.clear_widgets()

        for abonnement in abonnements:
            item=ThreeLineAvatarIconListItem(
                text=abonnement[1],
                tertiary_text=f"Du: {abonnement[3]} à  {abonnement[4]}" ,
                secondary_text= f"Statut: {abonnement[2]}",
                font_style="H6"
            )
            item.add_widget(IconLeftWidget(icon="calendar-check"))
            item.add_widget(
                IconRightWidget(
                    icon="trash-can-outline",
                    theme_text_color="Custom",
                    text_color=(1,0,0,1),
                    on_release=lambda btn, uid=abonnement[0]: self.supprimer_abonnement(uid)
                )
            )
          
            ligne.add_widget(item)

    def ajouter_abonnement(self):   
        nom = self.nom_input.text.strip()
        email = self.email_input.text.strip()
        type_engin = self.type_input.text.strip()
        num_plaque = self.plaque_input.text.strip()
        date_debut = self.dateDebut_input.text.strip()
        date_fin = self.dateFin_input.text.strip()
        statut = self.statut_input.text.strip()
        if self.montant_input.text.strip():
            montant=float(self.montant_input.text.strip())
        else:
            montant = 0.0
        mode_paiement = self.modePaiement_input.text.strip()

        if not (nom and email and type_engin and num_plaque and date_debut and date_fin and statut and mode_paiement):
            toast("Veuillez remplir tous les champs SVP.")
            return
        try:
            cursor = self.connexion.cursor()
            cursor.execute("SELECT * FROM Utilisateur WHERE nom = ? ", (nom,))
            user = cursor.fetchone()
            if user:
                id_user = user[0]
            else:
                cursor.execute("INSERT INTO Utilisateur (nom,email,login,mdp,role) VALUES ( ?, ? , ?, ?, ?)", (nom,email,"client","1111","client"))
                self.connexion.commit()
                id_user = cursor.lastrowid
            cursor.execute("SELECT * FROM Engin WHERE plaque = ?", (num_plaque,))
            engin = cursor.fetchone()

            if engin:
                id_engin = engin[0]
            else:
                cursor.execute("INSERT INTO Engin (id_user,type, plaque) VALUES ( ?, ?, ? )", (id_user, type_engin, num_plaque))
                self.connexion.commit()
                id_engin = cursor.lastrowid


            # 3. Vérifier ou créer paiement
       
            cursor.execute("INSERT INTO Paiement (montant,modePaiement,datePaiement) VALUES (? , ? , ?)", (montant,mode_paiement,date_debut,))
            self.connexion.commit()
            id_paiement = cursor.lastrowid

            sql = """
                INSERT INTO Abonnement (id_user, id_paiement, dateDebut, dateFin, statut)
                VALUES ( ?, ?, ?, ?, ? )
            """
            valeurs = (id_user, id_paiement, date_debut, date_fin, statut)
            cursor.execute(sql, valeurs)
            self.dialog.dismiss()
            self.connexion.commit()


            # definition de la taille de notre carte
            largeur=500
            hauteur=250
            largeur_qr=int(largeur * 0.30)  #30% pour le qr
            largeur_text= largeur - largeur_qr  # A droite 70% pour les informations
            # creation du qr
            codeQR=f"Type:{type_engin} \nPlaque:{num_plaque} \n Date début:{date_debut} \n Date fin : {date_fin} \n Statut : {statut}"
            qr=qrcode.QRCode(version=1,box_size=10,border=4)
            qr.add_data(codeQR)
            qr.make(fit=True)
            image_qr=qr.make_image(fill_color='black', back_color='white')
            image_qr=image_qr.resize((largeur_qr,largeur_qr))
            # creation de la carte d'abonnement
            carteAbonnement=Image.new("RGB", (largeur,hauteur),color='white')
            draw=ImageDraw.Draw(carteAbonnement)
            # le style
            font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 18)
            x=10
            y=20
            espace=40
            lignes = [
                f"Nom: {nom}",
                f"Email: {email}",
                f"Type: {type_engin}",
                f"Date début: {date_debut}",
                f"Date fin: {date_fin}"
            ]
            # Dessiner chaque ligne
            for ligne in lignes:
                draw.text((x, y), ligne, font=font, fill="black")
                y += espace
            a=largeur_text
            b=(hauteur - largeur_qr)
            carteAbonnement.paste( image_qr,( a, b ) )  #placement du qr code
            #Enregistrement de la carte sous forme image 
            carteAbonnement.save("Ges-Parking_Carte.png")
            toast("Abonnement ajouté avec succès")
            self.charger_abonnements()
            self.vider_champs()
            cursor.close()
        except Error as e:
            toast(f"Erreur : {e}")



    def vider_champs(self):
        self.nom_input.text = ""
        self.email_input.text = ""
        self.type_input.text = ""
        self.plaque_input.text = ""
        self.dateDebut_input.text = ""
        self.dateFin_input.text = ""
        self.statut_input.text = ""
        self.montant_input.text = ""
        self.modePaiement_input.text = ""



    def supprimer_abonnement(self, id_abonnement):

        try:
            cursor = self.connexion.cursor()
            cursor.execute("DELETE  FROM Abonnement WHERE id_abonnement = ?", (id_abonnement,))
            self.connexion.commit()
            toast("Suppression reussie")
            self.charger_abonnements()
            cursor.close()
        except Error as e:
            toast(f"Erreur : {e}")




    def rechercher_abonnement(self,texte):
        texte = texte.lower().strip()
        try:
            cursor = self.connexion.cursor()
            sql = """
                SELECT a.id_abonnement, u.nom, e.type a.date_debut, a.date_fin
                FROM Abonnement a
                JOIN Utilisateur u ON a.id_user = u.id_user
                JOIN Engin e ON a.id_engin = e.id_engin
                WHERE LOWER(u.nom) LIKE ?
                ORDER BY a.id_abonnement ASC
            """
            cursor.execute(sql, (texte + '%',))
            abonnements = cursor.fetchall()
            self.afficher_abonnement(abonnements)
            cursor.close()
        except Error as e:
            print(f"Erreur : {e}")


    def enregistrer(self):
        box = MDBoxLayout(
            orientation='vertical',
            spacing=dp(10),
            padding=[dp(10)]*4,
            size_hint_y=None
        )
        box.bind(minimum_height=box.setter('height'))  

        self.nom_input = MDTextField(mode="rectangle", hint_text="Nom et Prénom", size_hint_y=None, height=dp(30))
        self.email_input = MDTextField(mode="rectangle", hint_text="Email", size_hint_y=None, height=dp(30))
        self.type_input = MDTextField(mode="rectangle", hint_text="Type d'engin", size_hint_y=None, height=dp(30))
        self.plaque_input = MDTextField(mode="rectangle", hint_text="Immatriculation", size_hint_y=None, height=dp(30))
        self.dateDebut_input = MDTextField(mode="rectangle", hint_text="Date début", size_hint_y=None, height=dp(30))
        self.dateFin_input = MDTextField(mode="rectangle", hint_text="Date fin", size_hint_y=None, height=dp(30))
        self.montant_input = MDTextField(mode="rectangle", hint_text="Montant Paiement", size_hint_y=None, height=dp(30))
        self.modePaiement_input = MDTextField(mode="rectangle", hint_text="Mode de paiement", size_hint_y=None, height=dp(30))
        self.statut_input = MDTextField(mode="rectangle", hint_text="Statut abonnement", size_hint_y=None, height=dp(30))

        box.add_widget(self.nom_input)
        box.add_widget(self.email_input)
        box.add_widget(self.type_input)
        box.add_widget(self.plaque_input)
        box.add_widget(self.dateDebut_input)
        box.add_widget(self.dateFin_input)
        box.add_widget(self.montant_input)
        box.add_widget(self.modePaiement_input)
        box.add_widget(self.statut_input)

        scroll = ScrollView(size_hint=(1, None),size=(dp(70), dp(450)))
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
                    on_release=lambda x: self.ajouter_abonnement())
            ]
        )
        self.dialog.open()