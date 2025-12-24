import socket
import threading
from crypto_utils import AESCipher

HOST = '127.0.0.1'
PORT = 5555

def receive_messages(client, cipher):
    while True:
        try:
            msg = client.recv(4096)
            if not msg:
                print("Déconnecté du serveur.")
                break
            decrypted_msg = cipher.decrypt(msg)
            print(decrypted_msg.decode('utf-8'))
        except:
            print("Erreur de connexion.")
            client.close()
            break

def start_client():
    key = input("Entrez la clé de chiffrement: ").encode('utf-8')
    cipher = AESCipher(key)
    
    name = input("Entrez votre nom: ")
    
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        client.connect((HOST, PORT))
        # Envoyer le nom au serveur
        client.send(cipher.encrypt(name.encode('utf-8')))
        
        # Démarrer le thread pour recevoir les messages
        receive_thread = threading.Thread(target=receive_messages, args=(client, cipher))
        receive_thread.start()
        
        print("Connecté au serveur. Tapez '/quit' pour quitter.")
        
        while True:
            msg = input()
            if msg.lower() == '/quit':
                client.send(cipher.encrypt('/quit'.encode('utf-8')))
                break
            client.send(cipher.encrypt(msg.encode('utf-8')))
            
    except Exception as e:
        print(f"Erreur de connexion: {e}")
    finally:
        client.close()
        print("Déconnecté.")

if __name__ == "__main__":
    start_client()