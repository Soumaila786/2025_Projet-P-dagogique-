
from model.DBConnection import getConnectionBD
import hashlib

class User:
    def __init__(self, nom, email, login,mdp, role, id=None):
        self.id = id
        self.nom = nom
        self.email = email
        self.login=login
        self.mdp = mdp
        self.role = role

   