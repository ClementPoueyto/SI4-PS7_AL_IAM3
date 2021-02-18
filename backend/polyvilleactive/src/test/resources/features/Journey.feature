#language: fr
Fonctionnalité: L'utilisateur recherche un trajet


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Quand il envoie la requete
    Alors il recoit un journey avec le transport "METRO"
    Et il recoit un code de reponse status 200

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "METRO"
    Quand il envoie la requete
    Alors il recoit un journey sans le transport "METRO"
    Et il recoit un code de reponse status 200

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 11
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "METRO"
    Et il filtre le transport "BUS"
    Quand il envoie la requete
    Alors il recoit un journey sans le transport "BUS"
    Et il recoit un journey sans le transport "METRO"
    Et il recoit un journey avec le transport "TRAIN"
    Et il recoit un code de reponse status 200


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "METRO"
    Et il filtre le transport "BUS"
    Et il filtre le transport "TRAIN"
    Quand il envoie la requete
    Alors il recoit un message "No journey Found" avec un code de reponse 400


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "METRO"
    Et il filtre le transport "BUS"
    Et il y a une panne sur la ligne "Aéroport CDG 2 / Mitry Claye - Robinson / St-Rémy lès Chevreuse" de "TRAIN"
    Quand il envoie la requete
    Alors il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "TRAIN"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "METRO"
    Et il y a une panne sur la ligne "Aéroport CDG 2 / Mitry Claye - Robinson / St-Rémy lès Chevreuse" de "TRAIN"
    Quand il envoie la requete
    Alors il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il y a un retard sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Quand il envoie la requete
    Alors il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Et il y a un retard sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Quand il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Quand il demande la liste des incidents sur son trajet
    Alors il ne recoit pas d'incident

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Et il y a un retard sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Quand il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 13
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "BUS"
    Et il y a un retard sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Et il y a une panne sur la ligne "Aéroport CDG 2 / Mitry Claye - Robinson / St-Rémy lès Chevreuse" de "TRAIN"
    Quand il envoie la requete
    Alors il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "TRAIN"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro prioriser bus 1 fin global"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Quand il envoie la requete
    Alors il recoit un journey sans le transport "METRO"
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro prioriser train 1 fin global"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Quand il envoie la requete
    Alors il recoit un journey sans le transport "METRO"
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "TRAIN"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train eviter bus fin global"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Quand il envoie la requete
    Alors il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    #seul transport disponible donc on prend le metro quand meme

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train eviter bus fin global local quand metro retard alors prioriser train 1 fin local"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Quand il y a un retard sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "TRAIN"


  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train eviter bus fin global local quand metro panne alors prioriser bus 1 quand metro retard alors prioriser train 1 fin local"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Quand il y a une panne sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train eviter bus fin global local quand metro avance alors prioriser train 1 fin local"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Quand il y a de l'avance sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "TRAIN"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train fin global local quand bus rempli alors prioriser train 1 fin local"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "TRAIN"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"
    Quand il y a un bus rempli sur la ligne "Mairie du XVIIIè Julles Joffrin - Porte de Versailles" de "BUS"
    Et les demandes de liberer une place pour l'utilisateur 1 ont echouees
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Mairie du XVIIIè Julles Joffrin - Porte de Versailles" du transport "BUS"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro eviter train fin global local quand bus rempli alors prioriser train 1 eviter metro fin local"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il filtre le transport "TRAIN"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"
    Quand il y a un bus rempli sur la ligne "Mairie du XVIIIè Julles Joffrin - Porte de Versailles" de "BUS"
    Et les demandes de liberer une place pour l'utilisateur 1 ont echouees
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Mairie du XVIIIè Julles Joffrin - Porte de Versailles" du transport "BUS"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Quand il y a de l'avance sur la ligne "Balard - Créteil-Pointe du Lac" de "METRO"
    Et il demande la liste des incidents sur son trajet
    Alors il recoit un incident sur la ligne "Balard - Créteil-Pointe du Lac" du transport "METRO"
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "METRO"
    Et sans la ligne de "METRO" "Balard - Créteil-Pointe du Lac"

  Scénario: Un utilisateur fait une recherche de trajet
    Etant donné  Un utilisateur connecté à l'application mobile avec un id égal à 1
    Et un expert en mobilité ayant defini des regles grace au dsl "global eviter metro fin global"
    Et il rentre un depart lat : 48.856614, longitude 2.3522219000000004 et une arrivee lat : 48.844303800000006, longitude 2.3743773
    Et il envoie la requete
    Et il recoit un code de reponse status 200
    Et il recoit un journey avec le transport "BUS"
    Et il a atteint l'etape a la position lat : 48.859069, longitude 2.303598
    Quand il y a un bus rempli sur la ligne "Mairie du XVIIIè Julles Joffrin - Porte de Versailles" de "BUS"
    Et il demande la liste des incidents sur son trajet
    Alors il ne recoit pas d'incident

