
from model.DataBase import DBConnection
from kivymd.uix.screen import MDScreen
from kivymd.toast import toast
from kivy.lang import Builder
from kivy.app import App
import sqlite3

Builder.load_file("view/Login.kv")

class LoginScreen(MDScreen):

    def __init__(self, currentApp, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.currentApp = currentApp
        self.connexion = DBConnection().get_connection()



    def authentifier(self):
        cursor = self.connexion.cursor()
        app = App.get_running_app()
        username = self.ids.name_input.text.strip()
        password = self.ids.password_input.text.strip()
        cursor.execute("SELECT * FROM Utilisateur WHERE login = ? AND mdp = ? ", (username, password))
        result = cursor.fetchone()
        if result:
            role = result[5]  
            if role == "admin":
                app.sm.current = "admin"
                toast("Connexion réussie")
                self.viderChamps()
            elif role == "agent":
                app.sm.current = "agent"
                toast("Connexion réussie")
                self.viderChamps()
        else:
            toast("Identifiants incorrects")
        cursor.close()



    def viderChamps(self):
        self.ids.name_input.text = ""
        self.ids.password_input.text = ""
