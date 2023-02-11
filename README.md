# Projecte Troncal M3 : Wordle
## Autors
- [Carlos Tarrias Diaz](https://gitlab.com/carlos.tarrias.7e6)

## Index
- [Objectiu](#objectiu) 
- [Descripció](#descripció)
- [Funcionalitats](#funcionalitats)
- [Backlog](#backlog)

## Objectiu 
Realitzar un programa que simuli el conegut joc en línia de Wordle mitjançant el llenguatge de programació Kotlin. Amb l'afegit de poder tenir múltiples usuaris amb la seva pròpia estadística 
i historial de paraules encertades.

## Descripció
1. El joc consisteix a endevinar una paraula concreta de cinc lletres en un màxim de sis intents. Tractant d’encertar una paraula formada per la combinació de 5 elements que poden contenir 
totes les lletres de l’abecedari i sense possibilitat de repetició.  
2. El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari), en cas contrari haurà perdut la partida.
3. S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
   Al joc original ho mostra en color verd i groc respectivament.
4. El jugador al final del joc pot decidir si vol jugar una altra partida o no.
## Funcionalitats

- **Càrrega de diccionari de paraules**: 
  - Al inici del joc s'utilitza un arxiu de text
    que conté 109 paraules de 5 lletres cadascuna (per defecte en llenguatge espanyol).
  - Ara el joc permet jugar amb paraules en anglès (també conté 109 paraules)
      - > L'arxiu amb les paraules a carregar s'anomena _test1.txt_ (o _test2.txt_ si és l'anglès) i ha d'estar ubicat a la ruta _src/main/kotlin_ dins del projecte. Cada línia del arxiu és una paraula.
- **Sistema d'usuaris i persistència de dades**:
  - Al carregar el joc iniciem a un jugador per defecte que tenim dins del sistema. Si no tenim cap usuari registrat demanem al usuari que 
  creï un de nou. 
    > El directori __users__ conté un nom d'usuari per cada línia.
  - Cada usuari del sistema té associades dades d'historial de paraules encertades, i dades de guardat que contenen les estadístiques 
  que ha anat obtenint el jugador al llarg de les seves partides.  
  - **Historial de paraules** 
    - Utilitzem el directori _savedata_ per emmagatzemar les paraules encertades. Cal destacar que **per cada llenguatge (ES-EN) 
    existeix un fitxer associat per a cada usuari**. 
      - > Si tenim l'usuari _carlos24_ llavors dins de la carpeta _savedata_ tenim EScarlos24.txt i ENcarlos24.txt  
      - > Cada línia del fitxer está format per tres camps separats per comes: la paraula encertada, l'index on es troba 
      la paraula en el seu respectiu diccionari, i el nombre d'intents que s'han necessitat per encertar-la.
  - **Dades de guardat**
    - Utilitzem el directori _history_ per emmagatzemar les dades estadístiques de cada usuari. Cada usuari té associat només 
    un fitxer d'estadístiques.
      - > Si tenim l'usuari john33 llavors dins de a carpeta _history_ hi ha Histjohn33.txt
      - Tenim sempre tres línies al fitxer:
          - 1era línia : valors de estadístiques separats per comes.
          - 2ona línia : valors acomulats d'intents separats per comes.
          - 3era línia : nombre de paraules totals amb que es pot jugar encara.
- **Menú de joc**:
   - Al inici del joc, i al inici d'una nova partida, mostrem al usuari un menú de joc amb les següents funcionalitats:
     - Canviar el llenguatge de les paraules
     - Canviar l'usuari actual
     - Mostrar les paraules encertades en el llenguatge actual.
     - Començar a jugar.
- **Pintar els caràcters**: 
  - Es fa servir el codi ANSI dels colors verd 
    i groc en els respectius caràcters en mostrar el resultat en cada intent.   
- **Joc Multipartida**:
  - S'ha afegit un bucle extern per permetre que el jugador pugui jugar una altra partida amb una altra paraula
  aleatòria que hi hagi al fitxer extern, si així ho desitja. Ara hem tingut en compte el nombre paraules restants al diccionari
  de joc, de manera que si no en queden, el joc termina automàticament.
      > Les paraules totals del joc són totes les paraules disponibles **en tots els llenguatges que permet el joc** (en aquest cas 218 paraules).  
- **Estadística del joc**:
  - **Els valors estadístics estan calculats respecte les paraules totals del joc**. 
  - Al final de cada partida el joc ens mostra:
    - La quantitat de paraules resoltes: s'ha utilitzat una variable que va incrementant cada vegada que el jugador 
    encerta una paraula del diccionari.
    - La quantitat de paraules no jugades: correspon al total de paraules del diccionari (218) menys el nombre de paraules
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
    - Les variables del joc també s'han canviat del mòdul principal a un fitxer secundari per afavorir la claredat del codi principal. 
    - La funcionalitat d'introduir una paraula s'ha modificat per a que sigui més amigable per al usuari.
    - S'ha modificat l'algorisme per pintar els caràcters, de forma que ara es cobreixen més casos límits (corregint els bugs que teniem fins ara en aquesta part) i ens estalviem l'us d'un diccionari d'ocurrències auxiliar.
  - **Modularització del programa**.
    - Les funcionalitats esmentades s'han modularitzat en funcions. Per una banda, tenim les funcions que controlen la lògica
    del joc, i, per altra banda, tenim les funcions auxiliars per mostrar dades (paraules, estadístiques del joc,...) al terminal.
    - Hem utilitzat el framework de testing JUnit versió 5 per realitzar els tests unitaris a les diferents funcions.
    - Tenim els tests de les funcions principals del joc (excepte les funcions auxiliar per imprimir per pantalla).
      >Document HTML dels resultats de testing: build/reports/tests/test/index.html  
    - Per documentar cada funció, hem fet servir el plugin de Dokka.
      >Document HTML de la documentació generada: build/dokka/index.html