/**
 * Mostra per terminal un missatge d'avís quan l'usuari ha esgotat tots els intents.
 * @param randomWOrd String: Paraula aleatòria seleccionada de la partida actual.
 */
fun printLoseMessage(randomWOrd: String) {
  println("Una pena, no has acertado la palabra...")
  println("La palabra era: $randomWord")
  println("Has perdido la racha...")
}

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