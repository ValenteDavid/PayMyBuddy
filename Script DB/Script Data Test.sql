Use paymybuddy;

INSERT INTO `paymybuddy`.`utilisateur`
(`id`,`email`,`mots_de_passe`,`nom`,`solde`,`iban`)
VALUES
(1,"aaa@email.com","mdp","Albert",1000,null),
(2,"bbb@email.com","mdp","Bob",250,null),
(3,"ccc@email.com","mdp","Jack",500,null);

INSERT INTO `paymybuddy`.`liste_contact`
(`id_utilisateur_liste`,`id_utilisateur_contact`)
VALUES
(1,2),
(1,3);

INSERT INTO `paymybuddy`.`transaction_bancaire`
(`montant`,`id_utilisateur`)
VALUES
(1000,1),
(-500,1);

INSERT INTO `paymybuddy`.`transaction_interne`
(`montant`,`description`,`id_utilisateur_debiteur`,`id_utilisateur_crediteur`)
VALUES
(250,"Paiement",1,2)





