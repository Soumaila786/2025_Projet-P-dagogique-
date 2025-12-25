
from model.DataBase import DBConnection
from .scanner_controller import ScannerBaseScreen
from kivymd.toast import toast
from kivymd.uix.screen import MDScreen
from .scanner_controller import ScannerBaseScreen
from kivy.lang import Builder
import cv2

# Charger le fichier KV
Builder.load_file("view/scannerSortie.kv")

class SortieScreen(ScannerBaseScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
       

    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")


        # self.connexion = DBConnection().get_connection()
        # if self.connexion:
        #     self.charger_utilisateurs()
        # else:
        #     print("Erreur de connexion à la base de données")

    
    def qr_detecte(self, data):
        self.ids.qr_message.text = f"QR détecté : {data}"
        toast(f"QR Sortie: {data}")