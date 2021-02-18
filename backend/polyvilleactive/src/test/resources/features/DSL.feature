#language: fr
Fonctionnalité: Utilisation du Language Dédié

  Plan du Scénario: Soumettre un programme via navigateur
    Etant donné un utilisateur de l'application web
    Quand il écrit un programme présentant <état>
    Et il valide le programme
    Alors il reçoit une réponse commençant par <réponse>

  Exemples:
    | état                 | réponse                   |
    | aucune faute         | "Votre programme est OK"  |
    | une faute lexicale   | "Erreur lexicale"         |
    | une faute syntaxique | "Erreur syntaxique"       |
    | une faute sémantique | "Erreur sémantique"       |


