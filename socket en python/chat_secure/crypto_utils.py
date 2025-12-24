from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
import os
import base64

def generate_key():
    return base64.b64encode(os.urandom(32))

class AESCipher:
    def __init__(self, key):
        self.key = base64.b64decode(key)
        self.bs = AES.block_size

    def encrypt(self, raw):
        raw = pad(raw, self.bs)
        iv = os.urandom(self.bs)
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        return base64.b64encode(iv + cipher.encrypt(raw))

    def decrypt(self, enc):
        enc = base64.b64decode(enc)
        iv = enc[:self.bs]
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        return unpad(cipher.decrypt(enc[self.bs:]), self.bs)