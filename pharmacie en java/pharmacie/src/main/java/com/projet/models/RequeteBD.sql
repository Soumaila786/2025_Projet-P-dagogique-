-- Table Client
CREATE TABLE client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20)
);

-- Table Ordonnance
CREATE TABLE ordonnance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reference VARCHAR(50) NOT NULL,
    num_ordonnance VARCHAR(50) NOT NULL,
    nom_medecin VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    client_id INT,
    CONSTRAINT fk_ordonnance_client FOREIGN KEY (client_id) REFERENCES client(id)
);

-- Table Vente
CREATE TABLE vente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codeVente VARCHAR(50) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    montant DECIMAL(10,2) NOT NULL,
    type VARCHAR(100) NOT NULL,
    montant INT NOT NULL,
    client_id INT,
    ordonnance_id INT NULL,
    CONSTRAINT fk_vente_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT fk_vente_ordonnance FOREIGN KEY (ordonnance_id) REFERENCES ordonnance(id)
);
