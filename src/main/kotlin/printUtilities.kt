import java.io.File

/**
 * Mostra per terminal un missatge d'avís quan l'usuari ha esgotat tots els intents.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 * @return Boolean: Control per indicar que s'ha mostrat correctament.
 */
fun printLoseMessage(randomWord: String): Boolean {
  if (randomWord.isNotEmpty()) {
    println("Una pena, no has acertado la palabra...")
    println("La palabra era: $randomWord")
    println("Has perdido la racha...")
    return true
  }
  return false
}

/**
 * Pinta del color específic el caràcter concret de la paraula introdïda per l'usuari.
 * @param color String : Color específic del caràcter.
 * @param reset String : Color per resetejar al color per defecte.
 * @param char Char : Caràcter de la paraula introduïda.
 * @return Boolean: Control per indicar que s'ha mostrat correctament.
 */
fun printChar(color: String?, reset: String?, char: String): Boolean {
  if (!color.isNullOrEmpty() && !reset.isNullOrEmpty()) {
    print("$color$char$reset")
    return true
  }
  return false
}

/**
 * Mostra per terminal les estadístiques finals del joc al acabar la partida.
 * @param currentNumberOfPlays Int : Partides jugades per l'usuari.
 * @param numberOfTotalGuessedWords Int : Nombre de paraules encertades.
 * @param wordsPercentage Double: Percentatge de paraules encertades.
 * @param totalWords Int : Nombre de paraules que queden per encertar.
 * @param continuousGuessedWords Int: Ratxa de paraules encertades.
 * @param bestContinuousGuessedWords Int: Millor ratxa d'encerts.
 * @return Boolean: Control per indicar que s'ha mostrat correctament.
 */
fun showGameStatistics(currentNumberOfPlays: Int,
                       numberOfTotalGuessedWords:Int,
                       wordsPercentage:Double,
                       totalWords:Int,
                       continuousGuessedWords:Int,
                       bestContinuousGuessedWords:Int) : Boolean {
  if (numberOfTotalGuessedWords > 0) {
    println("===============MOSTRANDO ESTADÍSTICA=============")
    println("PARTIDAS JUGADAS: $currentNumberOfPlays")
    println("HAS RESUELTO UN TOTAL DE $numberOfTotalGuessedWords PALABRAS")
    println("PORCENTAJE DE PALABRAS RESUELTAS DEL DICCIONARIO ${String.format("%.2f", wordsPercentage)}%") //Mostrem el percentatge amb dos decimals de precisió.
    println("TODAVÍA PUEDES RESOLVER $totalWords PALABRAS")
    println("TU RACHA ACTUAL ES DE : $continuousGuessedWords PALABRAS")
    println("TU MEJOR RACHA ES DE : $bestContinuousGuessedWords PALABRAS")
    println("**Mostrando media de intentos**")
    return true
  }
  return false
}

/**
 * Mostra les barres d'estadístiques de les mitjanes d'intents de les paraules encertades
 * @param medianOfTries DoubleArray: Emmagatzema la mitjana d'intents de tot el joc.
 * @param numOfTriesAccomulate IntArray: Emmagatzema el total de paraules que s'han encertat per 1..6 intents.
 * @param colors MutableMap<String, String>: Conté els colors que amb què es pintarà l'histograma.
 * @param numberOfTotalGuessedWords Int: Nombre de paraules encertades.
 * @return Boolean: Control per indicar que s'ha mostrat correctament.
 */
fun showGameHistogram(medianOfTries: DoubleArray,
                      numOfTriesAccomulate: IntArray,
                      colors: MutableMap<String, String>,
                      numberOfTotalGuessedWords: Int) : Boolean {
  if (numberOfTotalGuessedWords > 0) {
    for (i in medianOfTries.indices) {
      print("${i+1}: ")
      repeat(numOfTriesAccomulate[i]) {
        print("${colors["white"]} ")
      }
      print(" ${colors["reset"]} (${String.format("%.1f",medianOfTries[i])}%)") //Mostrem el percentatge amb un decimal de precisió.
      println("")
    }
    println("====================FIN ESTADÍSTICAS=================")
    return true
  }
  return false
}


/**
 * Mostra el menú incial de l'aplicació.
 * @param language String : Llenguatge actual del joc.
 * @param userLoaded String : Usuari actual del joc.
 */
fun showMenu(language: String, userLoaded: String) {
  val menu =
    """
    || | /| / / __ \/ _ \/ _ \/ /  / __/
    || |/ |/ / /_/ / , _/ // / /__/ _/  
    ||__/|__/\____/_/|_/____/____/___/
    |
    |=============MENU PRINCIPAL==========  
    |*1.CAMBIAR LENGUAJE DE LAS PALABRAS
    |*2.CAMBIAR USUARIO
    |*3.VER PALABRAS ACERTADAS
    |*0.COMENZAR A JUGAR
    |=====================================
    """.trimMargin()
  println(menu)
  printCurrentState(language, userLoaded)
  println("> Escoge una opción: ")
}

/**
 * S'encarrega de mostrar quins són el llenguatges i l'usuari carregats actualment.
 * @param language String : Llenguatge actual del joc.
 * @param userLoaded String : Usuari actual del joc.
 */
fun printCurrentState(language: String, userLoaded: String) {
  println("~~~USUARIO ACTUAL -> ${userLoaded}")
  println("~~~IDIOMA DE LAS PALABRAS ACTUAL -> ${language}")
}

/**
 * S'encarrega de mostrar les paraules del històric de l'usuari actual en el llenguatge concret.
 * @param wordsData MutableList<MutableList<String>> : Estructura que conté les diferents paraules encertades amb el seu índex i intent corresponent.
 */
fun printHistory(wordsData: MutableList<MutableList<String>>) {
  for (wordData in wordsData) {
    println("PALABRA ${wordData[0]} acertada en intento ${wordData[2]}")
  }
}

/**
 * S'encarrega d'imprimir per terminal els usuaris del sistema.
 */
fun printUsers() {
  val users = File("./users/")
  if (users.exists()) {
    println("Listando usuarios disponibles.")
    //Mostrem tots els usuaris de tots els fitxers d'usuaris (si escau).
    for (userFile in users.listFiles()!!) {
      if (userFile.length() > 0) { //Si el fitxer d'usuaris té contingut.
        println(userFile.readText())
      }
    }
  }

}