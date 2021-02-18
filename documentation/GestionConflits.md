# Les conflits entre événements

Les évènements sont eux mêmes classés par groupes de priorités, du plus fort au plus faible:

- Groupe A

évènement transport : PANNE


- Groupe B

évènement transport : AVANCE

évènements météos : NEIGE PLUIE

  
- Groupe C

évènements transports : RETARD REMPLI


- Groupe D

évènement météo : SOLEIL

---

Lorsque deux règles ou plus sont levées en simultané, un système de gestion de priorités interviendra au niveau de l’application:

Si les évènements sont de niveaux différents alors on retiendra seulement la règle de priorité la plus forte

S’ils sont de même niveau alors l’action résultante sera un compromis:
-   Les instructions INTERDICTION ont une priorité absolue
-   Si une instruction PRIORISER et EVITER sont présentes pour un même transport elles s’annulent
-   Si plusieurs instructions PRIORISER sont présentes pour un même transport, le transport se verra attribué la moyenne des rangs donnés
