# Projecte Troncal M3 : Wordle
## Autors
- [Carlos Tarrias Diaz](https://gitlab.com/carlos.tarrias.7e6)

## Index
- [Objectiu](#objectiu) 
- [Descripció del joc](#descripció)
- [Funcionalitats](#funcionalitats)
- [Backlog](#backlog)

## Objectiu 
Realitzar un programa que simuli el conegut joc en línia de Wordle mitjançant el llenguatge de programació Kotlin.

## Descripció
1. El joc consisteix a endevinar una paraula concreta de cinc lletres en un màxim de sis intents. Tractant d’encertar una paraula formada per la combinació de 5 elements que poden contenir 
totes les lletres de l’abecedari i sense possibilitat de repetició.  
2. El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari), en cas contrari haurà perdut la partida.
3. S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
   Al joc original ho mostra en color verd i groc respectivament.
4. El jugador al final de la partida pot decidir si vol jugar una altra o no.
## Funcionalitats

- **Càrrega de diccionari de paraules**: 
  - Principalment, s'utilitza un arxiu de text
    que conté 109 paraules de 5 lletres cadascuna.
      - > L'arxiu s'anomena _test1.txt_ i ha d'estar ubicat a la ruta _src/main/kotlin_ dins del projecte.
  - Per generar una paraula escollim un índex aleatori.
- **Duplicació de paraules**: 
  - S'ha decidit controlar les paraules que tenen caràcters diferents
  mitjançant una estructura de dades tipus diccionari on s'emmagatzema quantes vegades apareix el caràcter en la paraula generada. Així també podem controlar 
  si hi ha caràcters repetits.
  - Exemple: OCASO -> {O:2,C:1,A:1,S:1}
  - **Nota bug**: No obstant això, existeix un error, si un caràcter es troba en la posició correcta i el seu valor al diccionari és 0, aquest caràcter es pinta de gris
    (quan hauria de ser verd).
- **Pintar els caràcters**: 
  - Es fa servir el codi ANSI dels colors verd 
    i groc en els respectius caràcters en mostrar el resultat en cada intent.  
  - En cada intent, s'itera per la paraula introduïda pel jugador i per cada caràcter:
    1. Si queden ocurrències al diccionari generat per aquella paraula:
       1. Si el caràcter està a la paraula i en la mateixa posició que en la paraula correcta, el caràcter es pinta de verd. 
       2. Si el caràcter està a la paraula i en una posició diferent al de la paraula correcta, es pinta de groc.
       3. En tots dos casos es disminueix l'ocurrència en 1 al caràcter corresponent dins el diccionari.  
    2. Si el nombre d'ocurrències és 0, es pinta de gris. 
- **Joc Multipartida**:
  - S'ha afegit un bucle extern per permetre que el jugador pugui jugar una altra partida amb una altra paraula
  aleatòria que hi hagi al fitxer extern, si així ho desitja. Ara hem tingut en compte el nombre paraules restants al diccionari
  de joc, de manera que si no en queden, el joc termina automàticament.
- **Estadística del joc**:
  - Al final de cada partida el joc ens mostra:
    - La quantitat de paraules resoltes: s'ha utilitzat una variable que va incrementant cada vegada que el jugador 
    encerta una paraula del diccionari.
    - La quantitat de paraules no resoltes: correspon al total de paraules del diccionari (109) menys el nombre de paraules
    encertades.
    - El percentatge de paraules que resolem: es calcula a partir del total de paraules del diccionari.
    - Mitjana d'intents:
      - S'ha creat un array estàtic que en cada posició _i-1_ té el nombre de vegades que ha encertat una paraula
      en l'intent _i_ .
      - S'ha creat un array estàtic que en cada posició _i_ conté la mitjana de l'intent _i_ calculada a partir del número de partides
      jugades on s'ha encertat la paraula.
      - A partir del codi ANSI pel color blanc s'ha creat una gràfic que indica de manera visual les
      mitjanes d'intents al jugador després de cada partida. Mostrant al final els percentatges emmagatzemats
      al array de mitjes. S'ha corretgit que es faci la mitjà respecte al nombre de paraules encertades i que només la faci
      quan el jugador a encertat almenys 1 paraula.
    - Millor ratxa de paraules encertades:
      - Hem anat incrementant una variable cada vegada que el jugador guanyava i en una altra variable 
      hem emmagatzemat la ratxa màxima que s'anava substituint per la ratxa actual només si aquesta 
      era major.
    - Control de paraules del diccionari
      - A mesura que escollíem una paraula nova, al diccionari la marcàvem com a buida ("") per indicar
      que no la tornaríem a escollir en una altra partida. Com que tenim en compte les paraules restants al diccionari,
      evitem que el bucle de cerca de la paraula aleatòria es faci infinit.
      - Tenim una variable on controlem les paraules disponibles restants al diccionari. Decrementem aquest 
      comptador de paraules restants cada vegada que el jugador termina una partida.

## Backlog
  - **Millores i correccions**
    - Ara l'estadística només es mostra quan el jugador ha encertat com a mínim una paraula.
    - Els colors s'han agrupat en una estructura de dades per poder-los reutilitzar més còmodament.
    - Les variables del joc també s'han canviat del mòdul principal a un fitxer secundari per afavorir la claredat del codi. 
  - **Modularització del programa**.
  - Les funcionalitats esmentades s'han modularitzat en funcions. Per una banda, tenim les funcions que controlen la lògica
  del joc, i, per altra banda, tenim les funcions auxiliars per mostrar dades (paraules, estadístiques del joc,...) al terminal.
  - Hem utilitzat el framework de testing JUnit versió 5 per realitzar els tests unitaris a les diferents funcions.
  - Tenim els tests de les funcions principals i els tests de les funcions auxiliars per imprimir per pantalla.
    >Document HTML dels resultats de testing: build/reports/tests/test/index.html  
  - Per documentar cada funció, hem fet servir el plugin de Dokka.
    >Document HTML de la documentació generada: build/dokka/index.html