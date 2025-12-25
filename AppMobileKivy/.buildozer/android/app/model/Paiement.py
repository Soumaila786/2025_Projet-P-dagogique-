# models/paiement.py
from model.DBConnection import getConnectionBD
from datetime import datetime

class Paiement:
    def __init__(self, ticket_id=None, montant=0.0, date_paiement=None, mode="", id=None):
        self.id = id
        self.ticket_id = ticket_id      # Peut être None si paiement abonnement (à adapter si besoin)
        self.montant = montant
        self.date_paiement = date_paiement or datetime.now().isoformat()
        self.mode = mode                # ex: "espèces", "carte", "mobile money"

   