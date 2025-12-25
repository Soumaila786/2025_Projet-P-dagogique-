
from datetime import datetime
from model.DataBase import DBConnection
from .scanner_controller import ScannerBaseScreen
from kivymd.toast import toast
from kivy.lang import Builder

# Charger le fichier KV
Builder.load_file("view/scannerSortie.kv")

class SortieScreen(ScannerBaseScreen):
    def __init__(self, widgetParent, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.widgetParent = widgetParent
        self.connexion = DBConnection().get_connection()
       

    def showDrawer(self, *args):
        self.widgetParent.ids.nav_drawer.set_state("toggle")

    
    def verifier_ticket_simple(self, data):
        try:
            typeTicket = data[0]
            reference = data[1].strip()   
            type = data[2].strip()   
            date_entree = data[3].strip()
            plaque = data[4].strip()   
            cursor = self.connexion.cursor()
            # Vérifie si l'entrée existe
            cursor.execute("SELECT id_ticket , dateHeureEntree ,statut FROM ScannerEntrer WHERE reference = ?", (reference,))
            result = cursor.fetchone()

            if not result:
                self.ids.qr_message.text = f"Ticket non trouvée pour ce QR."
                return    
            
            id_ticket = result[0]
            date_entree = result[1]
            statut=result[2]

            if statut =='expiré':
                self.ids.qr_message.text ="Ticket expiré "
                return
            
            # Insère la sortie avec la date/heure actuelle
            cursor.execute("""
                INSERT INTO ScannerSortie (referenceTicket, dateHeureSortie)
                VALUES (?, datetime('now'))
            """, (reference,))

            cursor.execute("""UPDATE ScannerEntrer SET statut = 'expiré' WHERE id_ticket = ?
                           """,(id_ticket,))
            self.connexion.commit()

            #Partie de calcul la duree
            date_entree = datetime.strptime(date_entree , "%Y-%m-%d %H:%M:%S")
            date_sortie = datetime.now()
            duree = (date_sortie - date_entree).total_seconds() / 3600
            #on arrondi
            montant = round(duree * 100 , 0)
            # on passe a l'affichage 
            self.ids.qr_message.text = f"Type: {typeTicket}\n {reference} \n Type Engin : {type}\n Date entrée : {date_entree}\n Plaque : {plaque} \n Montant à payé : {montant} FCFA"
            toast(f"Sortie enregistrée.")
            cursor.close()
        except Exception as e:
            print(f"Erreur lors de l'enregistrement de la sortie : {e}")



    def qr_detecte(self, data):
        try:
            elements = data.strip().split("\n")

            if not elements or len(elements) == 0:
                self.ids.qr_message.text = "QR code invalide."
                return

            type_ticket = elements[0].strip() 
            if type_ticket == "Simple":
                self.verifier_ticket_simple(elements)   
                return
            elif type_ticket == "Abonnement":
                self.verifier_ticket_abonnement(elements)
                return
            else:
                self.ids.qr_message.text = "Faux ticket."
        except Exception as e:
            print(f"Erreur lors de la lecture du QR : {e}")
            self.ids.qr_message.text = "Erreur de lecture du QR."


    def verifier_ticket_abonnement(self, data):
        try:
            typeTicket = data[0].strip()
            reference = data[1].strip()
            typeEngin = data[2].strip()   
            plaque = data[3].strip()   
            date_debut = data[4].strip()
            date_fin = data[5].strip()   
            statutQR = data[6].strip()  

            cursor = self.connexion.cursor()

            # Vérifie si l'entrée existe
            cursor.execute("SELECT id_abonnement , statut FROM Abonnement WHERE reference = ?", (reference,))
            result = cursor.fetchone()
            if not result:
                self.ids.qr_message.text = f"Ticket non trouvée pour ce QR."
                return    
            
            id_abonnement = result[0]
            statutDB = result[1]
            if statutDB =='expiré':
                self.ids.qr_message.text ="Ticket expiré "
                return
            # Insère la sortie avec la date/heure actuelle
            cursor.execute("""
                INSERT INTO ScannerSortie (referenceTicket, dateHeureSortie)
                VALUES (?, datetime('now'))
            """, (reference,))


            if datetime.now().date() > datetime.strptime(date_fin, "%d-%m-%Y").date():
                cursor.execute("""UPDATE Abonnement SET statut = 'expiré' WHERE id_abonnement = ? """,(id_abonnement,))
                toast("Abonnement expiré ")
                self.connexion.commit()
                cursor.close()
                return

            
            # on passe a l'affichage 
            self.ids.qr_message.text = (f"Ticket: {typeTicket}\n {reference} \n Type:{typeEngin} \nPlaque:{plaque} \n Date début:{date_debut} \nDate fin :{date_fin} \n Statut : {statutQR}")
            self.connexion.commit()
            cursor.close()
            toast(f"Sortie enregistrée.")
        except Exception as e:
            print(f"Erreur lors de l'enregistrement de la sortie : {e}")
    