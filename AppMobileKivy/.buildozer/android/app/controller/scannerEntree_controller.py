from kivymd.uix.list import ThreeLineAvatarIconListItem, IconLeftWidget, IconRightWidget
from .scanner_controller import ScannerBaseScreen
from model.DataBase import DBConnection
from kivymd.toast import toast
from datetime import datetime
from kivy.lang import Builder
from sqlite3 import Error
import pytesseract
import qrcode
import cv2


Builder.load_file("view/scannerEntree.kv")

class EntreeScreen(ScannerBaseScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
        
        
    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")
        self.connexion = DBConnection().get_connection()
        if self.connexion:
            self.charger_entree()
        else:
            print("Erreur de connexion à la base de données")

   


    def prendre_photo(self):
        self.typeEngin = self.ids.typeEngin_input.text.strip()
        self.dateHeureEntree = datetime.now().strftime("%d-%m-%Y %H:%M:%S")
        self.ref_QR = f"TICK{datetime.now().strftime('%Y%m%d%H%M%S')}"

        if self.capture:
            ret, fenetre = self.capture.read()
            if ret:
                nomImage = f"Ges-Parking_{datetime.now().strftime('%Y%m%d_%H%M%S')}.png"
                cv2.imwrite(nomImage, fenetre)

                # OCR (plaque)
                gray = cv2.cvtColor(fenetre, cv2.COLOR_BGR2GRAY)
                texte = pytesseract.image_to_string(gray, lang='fra').strip()

                # Génération QR avec infos
                codeQR = f"Type:{self.typeEngin}\nDate:{self.dateHeureEntree}\nPlaque:{texte} \n QR : {self.ref_QR}"
                imageQR = qrcode.make(codeQR)
                nomQR = "Ticket_Ges-Parking.png"
                imageQR.save(nomQR)

                # Conversion des images en BLOB pour DB
                self.image = self.conversion_image(nomImage)
                self.image_qr = self.conversion_image(nomQR)
                self.plaque = texte 
                #appel de la methode pour enregistrer automatiquement dans la base de donnees
                self.ajouter_entree()
                self.arreter_camera()


    def ajouter_entree(self):
        try:
            cursor = self.connexion.cursor()

            # Enregistrer l'engin
            sql_engin = """INSERT INTO Engin (type, plaque, imagePlaque) VALUES (?, ?, ?)"""
            cursor.execute(sql_engin, (self.typeEngin, self.plaque, self.image))
            self.connexion.commit()
            id_engin = cursor.lastrowid  # récupérer l'id de l'engin inséré
            # Enregistrer l'entrée
            sql_entree = """ INSERT INTO ScannerEntrer (id_engin,reference, dateHeureEntree, imageQR,statut) VALUES (?, ?, ?, ?, ?) """
            valeurs = (id_engin,self.ref_QR, self.dateHeureEntree, self.image_qr,'actif')
            cursor.execute(sql_entree, valeurs)
            self.connexion.commit()

            toast("Enregistrée avec succès")

        except Error as e:
            print(f"Erreur DB : {e}")



    def supprimer_entree(self, id_entree):

        try:
            cursor = self.connexion.cursor()
            cursor.execute("DELETE  FROM ScannerEntrer WHERE id_entree = ?", (id_entree,))
            self.connexion.commit()
            toast("Suppression reussie")
            self.charger_entree()
            cursor.close()
        except Error as e:
            print(f"Erreur : {e}")

    

    def charger_entree(self):
        try:
            
            cursor = self.connexion.cursor()
            # Affichage avec jointures pour afficher nom et type_engin, num_plaque, mode_paiement
            sql="""
                SELECT sc.id_entree, e.type, sc.dateHeureEntree
                FROM ScannerEntrer sc
                JOIN Engin e ON e.id_engin = e.id_engin
                ORDER BY sc.id_entree ASC
            """
            cursor.execute(sql)
            entrees = cursor.fetchall()
            self.afficher_entree(entrees)
            cursor.close()
        except Error as e:
            print(f"Erreur : {e}")





    def afficher_entree(self, entrees):
        ligne = self.ids.liste_entree
        ligne.clear_widgets()

        for entree in entrees:
            item=ThreeLineAvatarIconListItem(
                text=entree[1],
                secondary_text=f"Date et heure : {entree[2]}" ,
                font_style="H6"
                
            )
            item.add_widget(IconLeftWidget(icon="calendar-check"))
            item.add_widget(
                IconRightWidget(
                    icon="trash-can-outline",
                    theme_text_color="Custom",
                    text_color=(1,0,0,1),
                    on_release=lambda btn, uid=entree[0]: self.supprimer_entree(uid)
                )
            )
          
            ligne.add_widget(item)



