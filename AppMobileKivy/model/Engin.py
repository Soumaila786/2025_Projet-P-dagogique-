# models/engin.py
from model.DBConnection import getConnectionBD


class Engin:
    def __init__(self, type, immatriculation, proprietaire, id=None):
        self.id = id
        self.type = type  # ex : "moto", "voiture"
        self.immatriculation = immatriculation
        self.proprietaire = proprietaire

  