Documentation du Projet - Chat Chiffré Client-Serveur
Table des matières
Aperçu du projet

Architecture technique

Installation

Utilisation

Fonctionnalités

Sécurité

Dépannage

Limitations connues

Améliorations futures

Aperçu du projet
Ce projet implémente un système de chat en temps réel avec chiffrement AES entre un serveur et plusieurs clients. Les principales caractéristiques incluent :

Communication chiffrée de bout en bout

Gestion des connexions multiples

Interface administrateur côté serveur

Déconnexion propre des clients

Architecture technique
Composants
Serveur (server.py):

Gère les connexions entrantes

Achemine les messages entre clients

Fournit des commandes d'administration

Client (client.py):

Interface utilisateur simple

Envoi/réception de messages chiffrés

Crypto Utils (crypto_utils.py):

Implémentation du chiffrement AES

Gestion des clés de chiffrement

Schéma de communication
text
Client 1 <--chiffré--> Serveur <--chiffré--> Client 2
                       (Gestion des connexions et routage)
Installation
Prérequis
Python 3.6+

Bibliothèque PyCryptodome

Configuration
Installer les dépendances :

bash
pip install pycryptodome
Cloner le dépôt (si applicable) :

bash
git clone [URL_DU_DEPOT]
cd [NOM_DU_DEPOT]
Utilisation
Lancer le serveur
bash
python server.py
Le serveur affichera :

L'adresse IP et le port d'écoute

La clé de chiffrement à partager avec les clients

Lancer un client
bash
python client.py
Le client demandera :

La clé de chiffrement (affichée par le serveur)

Un nom d'utilisateur

Commandes spéciales
Côté client :

/quit - Quitter proprement la conversation

Côté serveur (admin) :

/list - Afficher les clients connectés

/kick <nom> - Expulser un client spécifique

/quit - Arrêter le serveur

Fonctionnalités
Serveur
Gestion des connexions simultanées

Broadcast des messages

Chiffrement/déchiffrement des messages

Interface d'administration

Liste des clients

Expulsion de clients

Arrêt propre

Client
Connexion sécurisée

Envoi de messages chiffrés

Réception asynchrone des messages

Déconnexion propre

Sécurité
Implémentation cryptographique
Chiffrement AES-256 en mode CBC

IV (Vecteur d'Initialisation) aléatoire pour chaque message

Padding PKCS7 pour les données

Gestion des clés
Génération aléatoire de clé au démarrage du serveur

Partage manuel de la clé (hors bande)

Stockage sécurisé en mémoire uniquement

Dépannage
Problèmes courants
Erreur de connexion

Vérifier que le serveur est lancé

Confirmer l'adresse IP et le port

Vérifier les pare-feux éventuels

Erreurs de chiffrement

S'assurer que la même clé est utilisée par tous les clients

Redémarrer le serveur si la clé a été compromise

Client expulsé

Se reconnecter avec un nom différent si nécessaire

Limitations connues
Manque d'authentification

Les clients peuvent s'identifier avec n'importe quel nom

Pas de vérification d'identité

Distribution de clé manuelle

La clé doit être partagée manuellement entre les participants

Pas de journalisation sécurisée

Les messages sont déchiffrés côté serveur pour le broadcast

Améliorations futures
Améliorations de sécurité

Ajout de Perfect Forward Secrecy

Implémentation de l'authentification mutuelle

Chiffrement asymétrique pour l'échange de clés

Fonctionnalités supplémentaires

Salons de discussion multiples

Transfert de fichiers

Messages privés entre clients

Améliorations d'interface

Interface graphique

Historique des messages

Notifications