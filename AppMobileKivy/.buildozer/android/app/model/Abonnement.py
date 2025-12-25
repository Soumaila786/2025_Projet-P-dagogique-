
from datetime import datetime
from model.DBConnection import getConnectionBD

class Abonnement:
    def __init__(self, nom_client, immatriculation, type_engin, date_debut=None, date_fin=None, montant=0.0, id=None):
        self.id = id
        self.nom_client = nom_client
        self.immatriculation = immatriculation
        self.type_engin = type_engin
        self.date_debut = date_debut or datetime.now().date().isoformat()
        self.date_fin = date_fin

    