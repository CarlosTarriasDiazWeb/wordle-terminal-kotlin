import java.io.File

fun main() {

  //  Autor: Carlos Tarrias Diaz
  //  Proyecto troncal M3 - Wordle

  // DESCRIPCIÓN DEL JUEGO
  /*
    El joc consisteix en endevinar una paraula concreta de cinc lletres en un màxim de sis intents.
    El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari),
    en cas contrari haurà perdut la partida.
    S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
    Al joc original ho mostra en color verd i groc respectivament.
    El jugador al final de la partida pot decidir si vol jugar una altra o no.
   */

  //============DEFINICION E INICIALIZACION DEL ARRAY DE PALABRAS QUE TENDRA EL JUEGO

  // 1.DICICIONARIO BÁSICO // YA NO SE USA
//  val testWords: List<String>  = listOf<String>(
//    "Actas", "Albas",
//    "Barra", "Bulos",
//    "Campo", "Casos",
//    "Dados", "Datos",
//    "Ocaso", "Espia",
//    "Falta", "Focos",
//    "Giros", "Gorra",
//    "Hijos", "Halos",
//    "Joyas", "Lagos",
//    "Magos", "Notas"
//  )

  // 2.MEJORA: CARGA DE DICCIONARIO EXTERNO
  val dictionary = ArrayList<String>()

  val filePath = "src/main/kotlin/test1.txt"
  val lines: List<String> = File(filePath).readLines()
  lines.forEach { line -> dictionary.add(line) }
  //dictionary.forEach{ println(it)} debug
  //println(dictionary.size) debug

  //============DEFINICION E INICIALIZACIÓN DE LOS COLORES PARA LAS LETRAS (CODIGO ANSI)=========//
  val colourReset = "\u001B[0m"
  val colourGreen = "\u001B[32m"
  val colourYellow = "\u001B[33m"
  val bgColourWhite = "\u001b[47m" //Per pintar els blocs en l'historial de mitjanes d'intents.

  //================DEFINICION DE VARIABLES DE PARTIDA===============//
  var numTries: Int //almacena el número de intentos, máximo 6.
  var playerWins: Boolean //controla si el usuario gana la partida o no.
  var guessWord: String //palabra que escribe el usuario en cada intento de la partida.
  var userLetter: String //contiene la letra que compone guessWord.
  var correctLetters: Int //contador de letras correctas, las que se pintan en verde.
  var letterToCount = mutableMapOf<Char, Int>() //diccionario de ocurrencias de cada carácter.
  var userOption: Int = -1 //recoge si el usuario quiere seguir jugando o no.
  var randomIndex: Int
  var randomWord: String

  //Nuevas variables de la actualización del juego
  var continuousGuessedWords = 0 //recoge la racha actual de palabras acertadas seguidas.
  var bestContinuousGuessedWords = 0 //recoge la mejor racha de palabras acertadas seguidas.
  var numberOfTotalGuessedWords = 0 //recoge el número de palabras acertadas respecto al total.
  var wordsPercentage: Double //porcentaje de palabras acertadas respecto al total.
  var currentNumberOfPlays = 0 //recoge el numero total de partidas jugadas.
  val medianOfTries = DoubleArray(6){0.0} //array que recoge la media de intento del total de partidas.
  val numOfTriesAccomulate = IntArray(6){0} //array que en cada posicion i-1 el total de palabras que se han acertado con i intentos.
  var totalWords = 109 //recoge el total de palabras totales que se podrán adivinar en el juego. Cada palabra sólo puede salir una sola vez.

  //Aqui empieza bucle principal del juego.
  do {
    //Vamos seleccionando la palabra del diccionario a partir del indice aleatorio hasta que seleccionemos una que no esté vacía.
    do {
      //Escogemos la palabra del diccionario a partir del indice aleatorio.
      randomIndex = dictionary.indices.random()
      randomWord = dictionary[randomIndex].uppercase()
    }while(randomWord.isEmpty())


    //Eliminamos la palabra marcándola como vacía en el diccionario para que no vuelva a salir.
    dictionary[randomIndex] = ""

    //Disminuimos el número total de palabras del juego.
    totalWords--

    //Creamos diccionario con ocurrencias de cada carácter para ver aquellos duplicados en cada intento.
    for (c in randomWord) {
      if (!letterToCount.containsKey(c)) {
        letterToCount[c] = 1
      }
      else {
        letterToCount[c] = letterToCount.getValue(c) + 1
      }
    }

    // println(randomWord) //debug

    //Reseteamos valores iniciales para la nueva partida.
    numTries = 0
    playerWins = false
    println("Intenta adivinar la palabra (son 5 letras!) ")

    //Aqui empieza el bucle de la partida.
    do{
      //Reseteamos valores iniciales para el nuevo intento.
      guessWord = ""
      correctLetters = 0
      //Restauramos valores iniciales de ocurrencias de carácteres para el nuevo intento.
      for (c in randomWord) {
        letterToCount[c] = randomWord.count{it == c}
      }
      //println(letterToCount) //debug
      println("Te quedan ${6 - (numTries)} intentos")
      repeat(5) { index->
        println("Introduce la letra ${index + 1}")
        userLetter = readln().uppercase()
        while (userLetter.isEmpty()){ //mientras el usuario no introduzca nada, vamos pidiendo la letra.
          println("Tienes que escribir algo...")
          userLetter = readln().uppercase()
        }
        if (userLetter.length > 1) { //para controlar que sólo se recoja un solo carácter cada vez que se lo pedimos al usuario.
          println("Has introducido más de un carácter. Se recogerá sólo el primero.")
          userLetter = userLetter.slice(0..0)
        }
        println("Ha introducido la letra $userLetter")
        guessWord += userLetter
      }
      for (i in guessWord.indices) {
        //Para controlar el número de veces que aparece un carácter concreto en la palabra tenemos en cuenta su valor en el diccionario
        if (guessWord[i] == randomWord[i] && letterToCount.getValue(guessWord[i]) > 0){
          print("$colourGreen${guessWord[i]}$colourReset ")
          correctLetters++
          //controlamos duplicidad del carácter
          letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
        }
        else if (guessWord[i] in randomWord && letterToCount.getValue(guessWord[i]) > 0) {
          print("$colourYellow${guessWord[i]}$colourReset ")
          //controlamos duplicidad del carácter
          letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
        }
        else {
          print("${guessWord[i]} ")
        }
      }
      numTries++
      if (correctLetters == 5) { // El jugador ha acertado la palabra.
        playerWins = true
        println("Enhorabuena has ganado!")
        //incrementamos el contador de la racha actual y el contador de palabras acertadas
        continuousGuessedWords++
        numberOfTotalGuessedWords++
        if (continuousGuessedWords > bestContinuousGuessedWords) bestContinuousGuessedWords = continuousGuessedWords
        //Limpiamos diccionario de ocurrencias
        letterToCount = mutableMapOf()
      }
      else { //El jugador no acierta la palabra.
        if (numTries == 6) {
          println("Una pena, no has acertado la palabra...")
          println("La palabra era: $randomWord")
          //reseteamos a 0 la racha porque hemos fallado
          continuousGuessedWords = 0
          println("Has perdido la racha...")
        }
      }

      println("=================================")
    }while(numTries < 6 && !playerWins) //seguimos intentando mientras no adivinemos la palabra y no hayamos superado el número máximo de intentos.

    //actaulizamos el número de partidas ganadas.
    currentNumberOfPlays++

    //Limpiamos diccionario de ocurrencias
    letterToCount = mutableMapOf()

    wordsPercentage = (numberOfTotalGuessedWords/109.0)*100.0
    println("===============MOSTRANDO ESTADÍSTICA=============")
    println("PARTIDAS JUGADAS: $currentNumberOfPlays")
    println("HAS RESUELTO UN TOTAL DE $numberOfTotalGuessedWords PALABRAS")
    println("PORCENTAJE DE PALABRAS RESUELTAS DEL DICCIONARIO ${String.format("%.2f", wordsPercentage)}%")
    println("TODAVÍA PUEDES RESOLVER $totalWords PALABRAS")
    println("TU RACHA ACTUAL ES DE : $continuousGuessedWords PALABRAS")
    println("TU MEJOR RACHA ES DE : $bestContinuousGuessedWords PALABRAS")
    println("**Mostrando media de intentos**")

    //Si el jugador ha adivinado la palabra actualizamos el contador de intentos correspondiente a la partida y actualizamos los valores de las medias
    if (playerWins) {
      numOfTriesAccomulate[numTries - 1]++
      //Fórmula: media = numero de veces que ha acertado la palabra en el intento num X / numero total de partidas jugadas
      var partialResult = 0.0
      //Calculamos la estadísitica de medias sólo si ha conseguido acertar como mínimo una palabra.
      if (numberOfTotalGuessedWords > 0) {
        for (i in medianOfTries.indices) {
          partialResult = numOfTriesAccomulate[i].toDouble()/numberOfTotalGuessedWords.toDouble()
          medianOfTries[i] = partialResult*100.0
        }
      }
    }
    //mostramos barra de estadísticas de las medias
    for (i in medianOfTries.indices) {
      print("${i+1}: ")
      repeat(numOfTriesAccomulate[i]) {
        print("$bgColourWhite ")
      }
      print(" $colourReset (${String.format("%.1f",medianOfTries[i])}%)")
      println("")
    }
    println("====================FIN ESTADÍSTICAS=================")

    if (totalWords == 0) { //Comprobamos si el jugador ha jugado tanto que ya no quedan palabras para jugar en el juego
      println("Se han acabado todas la palabras disponibles. Gracias por jugar tanto.")
    }
    else { //Sólo tiene sentido preguntar al jugador si quiere seguir jugando mientras haya palabras disponibles
      println("Quieres hacer otra partida?")
      println("SI --> Pulsa 1")
      println("NO --> Pulsa 2")
      userOption = readln().toInt()
      while (userOption < 1 || userOption > 2) { //mientras la opción no sea correcta, vamos pidiéndole el input correcto al usuario.
        println("Opción no válida")
        println("SI --> Pulsa 1")
        println("NO --> Pulsa 2")
        userOption = readln().toInt()
      }
    }
  }while(userOption == 1 && totalWords > 0) //seguimos jugando mientras el jugador así lo indique.

  println("Apagando WORDLE...")
}