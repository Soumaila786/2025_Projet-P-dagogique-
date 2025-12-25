from kivymd.uix.screen import MDScreen
from kivy.lang import Builder

Builder.load_file("view/Agent.kv")
class AgentScreen(MDScreen):
           
    def on_kv_post(self, base_widget):
        from controller.accueil_controller import AccueilScreen
        from controller.abonnement_controller import AbonnementScreen
        from controller.paiement_controller import PaiementScreen
        from controller.scannerEntree_controller import EntreeScreen
        from controller.scannerSortie_controller import SortieScreen

        self.ids.ecran_principal.add_widget(AccueilScreen(self))
        self.ids.ecran_principal.add_widget(AbonnementScreen(self))
        self.ids.ecran_principal.add_widget(PaiementScreen(self))
        self.ids.ecran_principal.add_widget(EntreeScreen(self))
        self.ids.ecran_principal.add_widget(SortieScreen(self))

   