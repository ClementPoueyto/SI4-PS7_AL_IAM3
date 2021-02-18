grammar DSL;

/*
 * Lexer Rules
 * Note: règles de lexer nécessitent full maj
 */

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

INT :  ('+'|'-')?[0-9]+;

GLOBAL          : G L O B A L;
FINGLOBAL       : F I N ' ' GLOBAL;
LOCAL           : L O C A L;
FINLOCAL       : F I N ' ' LOCAL;

QUAND           : Q U A N D;
ALORS           : A L O R S;

PRIORISER       : P R I O R I S E R;
EVITER          : E V I T E R;
INTERDIRE		: I N T E R D I R E;

BUS : B U S;
METRO : M E T R O;
TRAIN : T R A I N;
TRAMWAY : T R A M W A Y;
VELO : V E L O;

PANNE : P A N N E;
REMPLI : R E M P L I ;
RETARD : R E T A R D;
AVANCE : A V A N C E;

SOLEIL : S O L E I L;
PLUIE  : P L U I E;
NEIGE : N E I G E;

WHITESPACE 		: (' '|'\r'|'\n'|'\t')+ -> skip ;


/*
 * Parser Rules
 * Note: règles de parser nécessitent pas de full maj
 */
prog            								: global local? EOF
												;
												
global          								: GLOBAL priorite* FINGLOBAL
												;
local                                           : LOCAL whenBlock* FINLOCAL
                                                ;

whenBlock                                       : QUAND evenement ALORS action
                                                ;

evenement returns [String type, String aleas] 	: transport alea {$type=$transport.text;$aleas=$alea.text;}
                                                | meteo          {$aleas=$meteo.text;}
                                                ;

meteo                                           : SOLEIL | PLUIE | NEIGE
                                                ;

alea											: RETARD | PANNE | REMPLI | AVANCE
												;
                                                
action											: (interdiction|priorite)+
												;
												
interdiction		returns [String type]		: INTERDIRE transport {$type=$transport.text;}
												;
												
priorite returns [String type, int rank, String instruction]		: PRIORISER transport INT {$type=$transport.text; $rank=$INT.int;$instruction="PRIORISER";}
								                | EVITER transport  {$type=$transport.text; $rank=-1;$instruction="EVITER";}
								                ;
								                
transport      									: BUS | METRO | TRAIN | TRAMWAY | VELO
												;