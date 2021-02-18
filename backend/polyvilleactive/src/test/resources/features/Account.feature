#language: fr
Fonctionnalité: Se connecter à un compte

  Scénario: Un utilisateur souhaite se connecter à son compte et recoit une réponse du serveur
    Etant donné Un utilisateur de l'application mobile
    Quand il se connecte en utilisant son pseudo "test" et son mot de passe "test"
    Alors il recoit un code de reponse 200
    Et il recoit les informations de son compte id = 1

  Scénario: Un utilisateur souhaite se connecter à son compte
    Etant donné Un utilisateur de l'application mobile qui n'a pas cree son compte
    Quand il se connecte en utilisant son pseudo "test2" et son mot de passe "test2"
    Alors il recoit un code de reponse 400
    Alors il recoit un message d'erreur "no user found"

  Scénario: : Un utilisateur souhaite creer son compte
    Etant donné un utilisateur qui crée son compte avec les informations : email "gege@gogole.fr" pseudo "oui" password "non"
    Et il recoit un code de reponse 201
    Quand il se connecte en utilisant son pseudo "oui" et son mot de passe "non"
    Alors il recoit un code de reponse 200



