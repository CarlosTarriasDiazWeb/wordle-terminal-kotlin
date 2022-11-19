# Projecte Troncal M3 : Wordle
## Autors
- [Carlos Tarrias Diaz](https://gitlab.com/carlos.tarrias.7e6)

## Index
- [Objectiu](#objectiu) 
- [Descripció del joc](#descripció)
- [Funcionalitats](#funcionalitats)

## Objectiu 
Realitzar un programa que simuli el conegut joc en línia de Wordle mitjançant el llenguatge de programació Kotlin.

## Descripció
1. El joc consisteix en endevinar una paraula concreta de cinc lletres en un màxim de sis intents. Tractant d’encertar una paraula formada per la combinació de 5 elements que poden contenir 
totes les lletres de l’abecedari i sense possibilitat de repetició.  
2. El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari), en cas contrari haurà perdut la partida.
3. S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
   Al joc original ho mostra en color verd i groc respectivament.
4. El jugador al final de la partida pot decidir si vol jugar una altra o no.
## Funcionalitats

- **Càrrega de diccionari de paraules**: 
  - S'ha utilitzat un array per emmagatzemar 20 paraules que
  s'utilitzaran a les diferents partides. (*Aquesta part es sustitueix per això)
      - > (*)Nota: com a millora s'ha utilitzat un arxiu de text
      que conté 109 paraules i al inici del programa l'array s'emplena amb aquestes paraules.
        S'ha utilitzat l'objecte `File` de la llibreria `java.util`.
      - > L'arxiu s'anomena _test1.txt_ i ha d'estar ubicat a la ruta _src/main/kotlin_ dins del projecte.
  - Per generar una paraula aleatòria s'escogeix un índex del array de paraules amb la funció `random()`.
- **Duplicació de paraules**: 
  - S'ha decidit controlar les paraules que tenen caràcters diferents
  mitjançant una estructura de dades tipus diccionari (`MutableMap`) on s'emmagatzema quantes vegades apareix el caràcter en la paraula generada.
  - Exemple: OCASO -> {O:2,C:1,A:1,S:1}
  - Nota: No obstant això, existeix un error, si un caràcter es troba en la posició correcta i el seu valor al diccionari és 0, aquest caràcter es pinta de gris
    (quan hauria de ser verd).
- **Pintar els caràcters**: 
  - S'utilitza el codi ANSI dels colors verd  `\u001B[32m`
    i groc `\u001B[33m` en els respectius caràcters al mostrar el resultat en cada intent.  
  - En cada intent, s'itera per la paraula introduïda pel jugador i per cada caràcter:
    1. Si queden ocurrències al diccionari generat per aquella paraula:
       1. Si el caràcter està a la paraula i en la mateixa posició que en la paraula correcta, el caràcter es pinta de verd. 
       2. Si el caràcter està a la paraula i en una posició diferent al de la paraula correcta, es pinta de groc.
       3. En tots dos casos es disminueix la ocurrència en 1 al caràcter corresponent dins el diccionari.  
    2. Si el número de ocurrències és 0, es pinta de gris. 
- **Joc Multipartida**:
  - S'ha afegit un bucle extern per permetre que el jugador pugui jugar una altra partida amb una altra paraula
  aleatòria que hi hagi al fitxer extern, si així ho desitja. Ara hem tingut en compte el nombre paraules restants al diccionari
  de joc, de manera que si no en queden, el joc termina automàticament.
- **Estadística del joc**:
  - Al final de cada partida el joc ens mostra:
    - La quantitat de paraules resoltes : s'ha utilitzat una variable que va incrementant cada vegada que el jugador 
    encerta una paraula del diccionari.
    - La quantitat de paraules no resoltes : correspon al total de paraules del diccionario (109) menys el número de paraules
    encertades.
    - El percentatge de paraules que resolem : es calcula a partir del total de paraules del diccionari.
    - Mitjana d'intents:
      - S'ha creat un array estàtic que en cada posició _i-1_ té el numero de vegades que ha encertant la paraula
      en el intent _i_ .
      - S'ha creat un array estàtic que en cada posicio _i_ conté la mitjà del intent _i_ calculada a partir del número de partides
      jugades on s'ha encertat la paraula.
      - A partir del codi ANSI pel color blanc `\u001b[47m` s'ha creat una gràfic que indica de manera visual les
      mitjanes d'intents al jugador després de cada partida. Mostrant al final els percentatges emmagatzemats
      al array de mitjes. S'ha corretgit que es faci la mitjà respecte al nombre de paraules encertades i que només la faci
      quan el jugador a encertat almenys 1 paraula.
    - Millor ratxa de paraules encertades:
      - Hem anat incrementant una variable cada vegada que el jugador guanyava i en una altra variable 
      hem emmagatzemat la ratxa màxima que s'anava substituïnt per la ratxa actual només si aquesta 
      era major.
    - Control de paraules del diccionari
      - A mesura que escolliem una paraula nova, al diccionari la marcàvem com a buida ("") per indicar
      que no la tornariem a escollir en una altra partida. Com que tenim en compte les paraules restants al diccionari,
      evitem que el bucle de cerca de la paraula aleatòria es faci infinit.
      - Tenim una variable on controlem les paraules disponibles restants al diccionari. Decrementem aquest 
      comptador de paraules restants cada vegada que el jugador termina una partida.