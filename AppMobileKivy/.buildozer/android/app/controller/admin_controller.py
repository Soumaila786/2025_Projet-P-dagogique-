from kivymd.uix.screen import MDScreen
from kivy.lang import Builder

class AdminScreen(MDScreen):

    def on_kv_post(self, base_widget):
        from controller.accueil_controller import AccueilScreen
        from controller.abonnement_controller import AbonnementScreen
        from controller.paiement_controller import PaiementScreen
        from controller.users_controller import UsersScreen

        self.ids.ecran_principal.add_widget(AccueilScreen(self))
        self.ids.ecran_principal.add_widget(AbonnementScreen(self))
        self.ids.ecran_principal.add_widget(PaiementScreen(self))
        self.ids.ecran_principal.add_widget(UsersScreen(self))
