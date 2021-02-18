#language: fr
Fonctionnalité: L'utilisateur utilise ses points pour des recompense/reduction


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur est connecté à l'application mobile avec un id égal à 1
    Et une recompense de "ticket gratuit" coutant 100 points
    Et possede 150 points
    Quand il veut acheter la reduction "ticket gratuit"
    Alors il recoit un code
    Et son nombre de point est de 50
    
      Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur est connecté à l'application mobile avec un id égal à 1
    Et une recompense de "ticket gratuit" coutant 30 points
    Et possede 20 points
    Quand il veut acheter la reduction "ticket gratuit"
    Alors il recoit une erreur car pas assez de point
    Et son nombre de point est de 20
    
    Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur est connecté à l'application mobile avec un id égal à 1
    Et une recompense de "ticket gratuit" coutant 100 points
    Et possede 101 points
    Quand il veut acheter la reduction "ticket gratuit"
    Alors il recoit un code
    Et son nombre de point est de 1
    
    Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur est connecté à l'application mobile avec un id égal à 1
    Et une recompense de "ticket gratuit" coutant 2 points
    Et possede 2 points
    Quand il veut acheter la reduction "ticket gratuit"
    Alors il recoit un code
    Et son nombre de point est de 0