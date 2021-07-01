Drop database paymybuddy;
Create database paymybuddy;
use paymybuddy;

CREATE TABLE utilisateur(
   id INT AUTO_INCREMENT,
   email VARCHAR(254) NOT NULL,
   mots_de_passe VARCHAR(50) NOT NULL,
   nom VARCHAR(100) NOT NULL,
   solde DECIMAL(15,2) NOT NULL,
   iban VARCHAR(34),
   PRIMARY KEY(id),
   UNIQUE(email)
);

CREATE TABLE transaction_interne(
   id INT AUTO_INCREMENT,
   montant DECIMAL(15,2) NOT NULL,
   description VARCHAR(50),
   id_utilisateur_debiteur INT NOT NULL,
   id_utilisateur_crediteur INT NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_utilisateur_debiteur) REFERENCES utilisateur(id),
   FOREIGN KEY(id_utilisateur_crediteur) REFERENCES utilisateur(id)
);

CREATE TABLE transaction_bancaire(
   id INT AUTO_INCREMENT,
   montant DECIMAL(15,2) NOT NULL,
   id_utilisateur INT NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id)
);

CREATE TABLE liste_contact(
   id_utilisateur_liste INT,
   id_utilisateur_contact INT,
   PRIMARY KEY(id_utilisateur_liste, id_utilisateur_contact),
   FOREIGN KEY(id_utilisateur_liste) REFERENCES utilisateur(id),
   FOREIGN KEY(id_utilisateur_contact) REFERENCES utilisateur(id)
);

