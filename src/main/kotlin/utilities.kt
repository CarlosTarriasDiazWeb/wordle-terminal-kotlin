import java.io.File

/**
 * Emplena el diccionari de paraules a partir d'un fitxer extern.
 * @param fileName String: Path relatiu al arxiu de tipus text amb les paraules.
 * @param dictionary ArrayList<String>: Estructura buïda que contindrá totes les paraules del joc.
 */
fun loadFile(fileName: String, dictionary: ArrayList<String>) {
  if (fileName.isNotEmpty()) {
    val path = "src/main/kotlin/$fileName"
    val lines: List<String> = File(path).readLines()
    lines.forEach { line -> dictionary.add(line) }
  }
}

/**
 * Escull una paraula de forma aleatòria del diccionari generat.
 * @param dictionary ArrayList<String>: Conté totes les paraules del joc.
 * @return String : Paraula aleatòria del diccionari.
 */
fun getRandomWord(dictionary: ArrayList<String>): String = dictionary[dictionary.indices.random()].uppercase()

/**
 * Emplena el diccionari amb el nombre de vegades que apareix cada caràcter en la paraula seleccionada.
 * @param letterToCount MutableMap<Char,Int>: Diccionari buit que tindrá les ocurrències de cada caràcter.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 */
fun setLetterToCount(letterToCount:MutableMap<Char,Int>, randomWord: String) {
  if (randomWord.length == 5) {
    for (c in randomWord) {
      if (!letterToCount.containsKey(c)) {
        letterToCount[c] = 1
      }
      else {
        letterToCount[c] = letterToCount.getValue(c) + 1
      }
    }
  }
}

/**
 * Processa la paraula que l'usuari introduirà com a resposta al intent.
 * @return String : Paraula introduïda per l'usuari.
 */
fun insertWord() : String {
  var userLetter: String
  var guessWord = ""
  repeat(5) { index->
    println("Introduce la letra ${index + 1}")
    userLetter = readln().uppercase() //cal pasar-ho ja que la paraula generada està en majúscules.
    while (userLetter.isEmpty()){ //Mentre l'usuari no introdueixi res li anem demanant la lletra.
      println("Tienes que escribir algo...")
      userLetter = readln().uppercase()
    }
    if (userLetter.length > 1) { //Per controlar que només es recolli només un caràcter cada cop que demanem input al usuari.
      println("Has introducido más de un carácter. Se recogerá sólo el primero.")
      userLetter = userLetter.slice(0..0)
    }
    println("Ha introducido la letra $userLetter")
    guessWord += userLetter
  }

  return guessWord
}

/**
 * Imprimeix per terminal la paraula que ha introduït el usuari amb els colors adients segons les regles del joc i conta el nombre de caràcters que coincideixen amb la paraula generada.
 * @param guessWord String: Paraula que ha introduït l'usuari.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 * @param letterToCount MutableMap<Char,Int>: Diccionari que té les ocurrències de cada caràcter.
 * @param colors MutableMap<String, String>: Diccionari que conté els colors que amb què es pintaran els caràcters.
 * @return Int : Nombre de caràcters correctes respecte la paraula original.
 */
fun printWord(guessWord: String, randomWord: String, letterToCount: MutableMap<Char, Int>, colors: MutableMap<String, String>): Int {
  var correctLetters = 0
  for (i in guessWord.indices) {
    //Per controlar el nombre de vegades que apareix un caràcter concret a la paraula tenim en compte el seu valor al diccionari
    if (guessWord[i] == randomWord[i] && letterToCount.getValue(guessWord[i]) > 0){
      print("${colors["green"]}${guessWord[i]}${colors["reset"]}")
      correctLetters++
      //Controlem duplicitat del caràcter
      letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
    }
    else if (guessWord[i] in randomWord && letterToCount.getValue(guessWord[i]) > 0) {
      print("${colors["yellow"]}${guessWord[i]}${colors["reset"]}")
      //Controlem duplicitat del caràcter
      letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
    }
    else {
      print("${guessWord[i]}")
    }
  }

  println("")

  return correctLetters
}

/**
 * S'encarrega de decidir si l'usuari ha guanyat la partida o no.
 * @param correctLetters Int: Nombre de caràcters encertats per l'usuari.
 */
fun playerWins(correctLetters: Int) : Boolean = correctLetters == 5

/**
 * Mostra per terminal les estadístiques finals del joc al acabar la partida.
 * @param currentNumberOfPlays Int : Partides jugades per l'usuari.
 * @param numberOfTotalGuessedWords Int : Nombre de paraules encertades.
 * @param wordsPercentage Double: Percentatge de paraules encertades.
 * @param totalWords Int : Nombre de paraules que queden per encertar.
 * @param continuousGuessedWords Int: Ratxa de paraules encertades.
 * @param bestContinuousGuessedWords Int: Millor ratxa d'encerts.
 */
fun showGameStatistics(currentNumberOfPlays: Int,
                       numberOfTotalGuessedWords:Int,
                       wordsPercentage:Double,
                       totalWords:Int,
                       continuousGuessedWords:Int,
                       bestContinuousGuessedWords:Int) {
  println("===============MOSTRANDO ESTADÍSTICA=============")
  println("PARTIDAS JUGADAS: $currentNumberOfPlays")
  println("HAS RESUELTO UN TOTAL DE $numberOfTotalGuessedWords PALABRAS")
  println("PORCENTAJE DE PALABRAS RESUELTAS DEL DICCIONARIO ${String.format("%.2f", wordsPercentage)}%") //mostrem el percentatge amb dos decimals de precisió.
  println("TODAVÍA PUEDES RESOLVER $totalWords PALABRAS")
  println("TU RACHA ACTUAL ES DE : $continuousGuessedWords PALABRAS")
  println("TU MEJOR RACHA ES DE : $bestContinuousGuessedWords PALABRAS")
  println("**Mostrando media de intentos**")
}

/**
 * @param medianOfTries DoubleArray: Emmagatzema la mitjana d'intents de tot el joc.
 * @param numOfTriesAccomulate IntArray: Emmagatzema el total de paraules que s'han encertat per 1..6 intents respectivament.
 * @param numTries Int: Nombre d'intents que ha necessitat el jugador per encertar la paraula.
 * @param numberOfTotalGuessedWords Int : Nombre de paraules encertades.
 */
fun calculateGameHistogram(medianOfTries: DoubleArray, numOfTriesAccomulate: IntArray, numTries: Int, numberOfTotalGuessedWords: Int) {
  if (numTries > 0) {
    numOfTriesAccomulate[numTries - 1]++
    //Fórmula: media = nombre de vegades que ha encertat la paraula en l'intent num X / nombre total de partides jugades
    var partialResult = 0.0

    //Calculem l'estadísitica de mitges només si ha aconseguit encertar com a mínim una paraula.
    if (numberOfTotalGuessedWords > 0) {
      for (i in medianOfTries.indices) {
        partialResult = numOfTriesAccomulate[i].toDouble()/numberOfTotalGuessedWords.toDouble()
        medianOfTries[i] = partialResult*100.0
      }
    }
  }
}
/**
 * Mostra les barres d'estadístiques de les mitjanes d'intents de les paraules encertades
 * @param medianOfTries DoubleArray: Emmagatzema la mitjana d'intents de tot el joc.
 * @param numOfTriesAccomulate IntArray: Emmagatzema el total de paraules que s'han encertat per 1..6 intents.
 * @param colors MutableMap<String, String>: Conté els colors que amb què es pintarà l'histograma.
 */
fun showGameHistogram(medianOfTries: DoubleArray, numOfTriesAccomulate: IntArray, colors: MutableMap<String, String>) {
  for (i in medianOfTries.indices) {
    print("${i+1}: ")
    repeat(numOfTriesAccomulate[i]) {
      print("${colors["white"]} ")
    }
    print(" ${colors["reset"]} (${String.format("%.1f",medianOfTries[i])}%)") //mostrem el percentatge amb un decimal de precisió.
    println("")
  }
  println("====================FIN ESTADÍSTICAS=================")
}

/**
 * Controla l'input de l'usuari per decidir si es jugarà a una altra partida o no (també en funció del nombre de paraules disponibles).
 * @param totalWords Int: Nombre de paraules que queden per encertar.
 * @return Int: Input numèric del usuari.
 */
fun processUserInput(totalWords: Int): Int {
  var userOption: Int
  if (totalWords == 0) { //Comprovem si el jugador ha jugat tant que ja no queden paraules per jugar.
    println("Se han acabado todas la palabras disponibles. Gracias por jugar.")
    userOption = -1
  }
  else { //Només té sentit preguntar al jugador si vol seguir jugant mentre hi hagi paraules disponibles.
    println("Quieres hacer otra partida?")
    println("SI --> Pulsa 1")
    println("NO --> Pulsa 2")
    userOption = readln().toInt()
    while (userOption < 1 || userOption > 2) { //Mentre l'opció no sigui correcta, li demanem l'input correcte a l'usuari.
      println("Opción no válida")
      println("SI --> Pulsa 1")
      println("NO --> Pulsa 2")
      userOption = readln().toInt()
    }
  }
  return userOption
}