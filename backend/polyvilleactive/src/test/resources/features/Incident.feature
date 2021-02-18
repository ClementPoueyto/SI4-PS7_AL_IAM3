#language: fr

Fonctionnalité: Un incident est detecte par un arbibus

  Scénario: Un abribus detecte un changement de meteo
    Etant donné un abribus qui detecte un changement meteo "neige" sur la ligne "ligne de test" de "BUS"
    Quand il envoie la requete a l'application
    Alors un incident "neige" est cree sur la ligne "ligne de test" de "BUS"
