import socket
import threading
from crypto_utils import AESCipher, generate_key

HOST = '127.0.0.1'
PORT = 5555
clients = []
client_names = {}
key = generate_key()
cipher = AESCipher(key)

def handle_client(client):
    name = None
    try:
        # Recevoir le nom du client
        name_msg = client.recv(4096)
        if not name_msg:
            return
        name = cipher.decrypt(name_msg).decode('utf-8')
        client_names[client] = name
        print(f"{name} a rejoint la conversation.")
        broadcast(cipher.encrypt(f"{name} a rejoint la conversation.".encode('utf-8')), client)
        
        while True:
            msg = client.recv(4096)
            if not msg:
                break
                
            decrypted_msg = cipher.decrypt(msg).decode('utf-8')
            
            # Vérifier les commandes spéciales
            if decrypted_msg.lower() == '/quit':
                break
                
            print(f"{name}: {decrypted_msg}")
            broadcast(cipher.encrypt(f"{name}: {decrypted_msg}".encode('utf-8')), client)
            
    except Exception as e:
        print(f"Erreur avec {name if name else 'client inconnu'}: {e}")
    finally:
        if client in clients:
            clients.remove(client)
        if client in client_names:
            name = client_names[client]
            del client_names[client]
            print(f"{name} a quitté la conversation.")
            broadcast(cipher.encrypt(f"{name} a quitté la conversation.".encode('utf-8')), client)
        client.close()

def broadcast(msg, sender=None):
    for c in clients:
        if c != sender:
            try:
                c.send(msg)
            except:
                if c in clients:
                    clients.remove(c)
                if c in client_names:
                    del client_names[c]
                c.close()

def admin_commands():
    while True:
        cmd = input("Commande admin (/list, /kick <nom>, /quit): ")
        if cmd.lower() == '/quit':
            print("Arrêt du serveur...")
            for c in clients:
                c.close()
            os._exit(0)
        elif cmd.lower() == '/list':
            print("Clients connectés:")
            for name in client_names.values():
                print(f"- {name}")
        elif cmd.startswith('/kick '):
            name_to_kick = cmd[6:]
            for client, name in client_names.items():
                if name == name_to_kick:
                    try:
                        client.send(cipher.encrypt("Vous avez été expulsé par l'admin.".encode('utf-8')))
                        client.close()
                        print(f"{name} expulsé.")
                    except:
                        pass
                    break
            else:
                print(f"Client {name_to_kick} introuvable.")

def start_server():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((HOST,PORT))
    server.listen()
    
    print(f"Serveur démarré sur {HOST}:{PORT}")
    print(f"Clé partagée: {key.decode()}")
    
    # Démarrer le thread pour les commandes admin
    admin_thread = threading.Thread(target=admin_commands, daemon=True)
    admin_thread.start()
    
    try:
        while True:
            client, addr = server.accept()
            print(f"Connexion entrante de {addr}")
            clients.append(client)
            thread = threading.Thread(target=handle_client, args=(client,))
            thread.start()
    except KeyboardInterrupt:
        print("Arrêt du serveur...")
        for c in clients:
            c.close()
        server.close()

if __name__ == "__main__":
    start_server()