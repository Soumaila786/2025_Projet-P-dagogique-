import random
import string
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
        

   


    def prendre_photo(self):
        self.typeEngin = self.ids.typeEngin_input.text.strip()
        self.dateHeureEntree = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        self.reference=self.generer_Reference_Ticket()
        typeTicket  = 'Simple'

        if self.capture:
            ret, fenetre = self.capture.read()
            if ret:
                nomImage = f"Ges-Parking_{datetime.now().strftime('%Y%m%d_%H%M%S')}.png"
                cv2.imwrite(nomImage, fenetre)
                # OCR (plaque)
                gray = cv2.cvtColor(fenetre, cv2.COLOR_BGR2GRAY)
                texte = pytesseract.image_to_string(gray, lang='fra').strip()
                # Génération QR avec infos
                codeQR = f"{typeTicket}\n {self.reference}\n {self.typeEngin}\n {self.dateHeureEntree}\n {texte} "
                imageQR = qrcode.make(codeQR)
                nomQR = "Ticket_Ges-Parking.png"
                imageQR.save(nomQR)
                # Conversion des images en BLOB pour DB
                self.image = self.conversion_image(nomImage)
                self.image_qr = self.conversion_image(nomQR)
                self.plaque = texte 
                #appel de la methode pour enregistrer automatiquement dans la base de donnees
                self.ajouter_entree()


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
            valeurs = (id_engin,self.reference, self.dateHeureEntree, self.image_qr,'actif')
            cursor.execute(sql_entree, valeurs)
            self.connexion.commit()

            toast("Enregistrée avec succès")

        except Error as e:
            print(f"Erreur DB : {e}")




    def generer_Reference_Ticket(self):
            suffixe = ''.join(random.choices(string.ascii_uppercase + string.digits, k=5))
            return f"TICK{suffixe}"