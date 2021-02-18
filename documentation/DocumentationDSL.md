
# Ecrire des règles de priorité - Documentation

  

Ce document est à l’attention des experts mobilités, il constitue la documentation du langage dédié permettant de définir les règles du moteur de l’application PolyVilleActive.

  

Veuillez noter que ce langage n’est pas sensible à la casse et ne requiert pas d’indentation, toutefois pour garantir la lisibilité pour son auteur, il est préférable de définir des conventions.

Dans la suite de ce document, les exemples présentent des délimiteurs de sections en majuscule, les instructions débutant par une majuscule et des constantes en minuscule.

  
  

# Les constantes reconnues

## Transports

BUS

METRO

TRAIN

TRAMWAY


Ces constantes peuvent être utilisée aux emplacement indiqués par **\<transport\>**

  

## Aléas

RETARD

PLEIN

REMPLI

AVANCE


Ces constantes peuvent être utilisée aux emplacement indiqués par **\<alea\>**

  
  

## Conditions météos

SOLEIL

PLUIE

NEIGE


Ces constantes peuvent être utilisée aux emplacement indiqués par **\<meteo\>**

  

# Instructions du langage

## priorisation

PRIORISER [\<transport\>](#Transports) <rang>

Permet d’attribuer un rang (entier strictement positif) de priorité à ce transport, plus le rang est petit plus le transport sera prioritaire, lors de la demande de trajet.

Un même rang ne peut être assigné à deux transports différents au sein d’une même clause

De même, un même transport ne peut pas se voir assigner deux rangs différents au sein d’une même clause.

Exemple:

    Prioriser bus 1

    Prioriser metro 2

## évitement

EVITER [\<transport\>](#Transports)

Permet d’éviter ce transport, si aucun autre choix est possible il sera tout de même disponible.

  

Exemple:

    Eviter bus

    Eviter metro

  

## interdiction

INTERDIRE [\<transport\>](#Transports)

Permet d’éliminer un transport des résultat de recherche, si aucun autre choix est possible la recherche d’itinéraire ne fournira pas de résultat

Exemple:

    Interdire bus

    Interdire metro

  

## instruction conditionnelle

QUAND <evenement> ALORS <actions>

Lorsque l'événement <evenement> affecte le trajet d’un utilisateur, les actions décrites dans <actions> seront considérées.

Un événement peut être [de transport](#Événement-transports) ou [de météo](#Événement-météo).

Les actions peuvent être des instructions de [priorisation](#priorisation), d’[évitement](#évitement) et d’[interdiction](#interdiction).

  

### Événement transports

Un évènement transport s’écrit : [\<transport\>](#Transports)  [\<alea\>](#Aléas)

Il se déclenche lorsque le transport <transport> est affecté par l’aléa <alea> précisé.

Exemple :

    metro panne

    bus retard

    train plein

  

### Événement météo

Un événement météo s’écrit simplement en indiquant la condition météo [<météo>](#Conditions-météos) qui doit déclencher l'événement.

  
  

# Les sections

Un programme écrit dans ce langage se compose de deux sections GLOBAL et LOCAL.

Ces sections ne peuvent apparaître plus d’une fois chacune dans un même programme.

La section GLOBAL est obligatoire, mais peut être vide.

La section LOCAL est facultative et peut être vide.

## Section global

  

La première partie permet de définir les règles de priorité par défaut, autrement dit la gestion globale.

  

Elle commence par GLOBAL et se termine par FIN GLOBAL.

Elle peut inclure des instructions de priorisation, d’évitement.

Ses instructions seront appliquées lorsque le trajet d’un utilisateur ne rencontre pas d’aléa.

Exemple:

    GLOBAL
    	Prioriser bus 1
    	Eviter metro
    FIN GLOBAL

## Section local

La première partie permet de définir les règles de priorité en réponse à des aléas, autrement dit la gestion locale.

Elle commence par LOCAL et se termine par FIN LOCAL.

Elle ne peut inclure des instructions conditionnelles.

Exemple:

    LOCAL
    	Quand bus retard Alors
    		Prioriser bus 1
    		Prioriser tramway 2
    		Eviter metro
    	Quand metro panne Alors
    		Interdire metro
    		Prioriser tramway 1
    		Prioriser bus 2
    		Eviter train
    	Quand pluie Alors
    		Prioriser metro 1
    FIN LOCAL
