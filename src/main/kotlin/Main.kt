fun main(args: Array<String>) {

  //============DEFINICION E INICIALIZACION DEL ARRAY DE PALABRAS QUE TENDRA EL JUEGO
  val dictionary = arrayOf (
    "Actas", "Albas",
    "Barra", "Bulos",
    "Campo", "Casos",
    "Dados", "Datos",
    "Ocaso", "Espia",
    "Falta", "Focos",
    "Giros", "Gorra",
    "Hijos", "Halos",
    "Joyas", "Lagos",
    "Magos", "Notas"
  )

  //============DEFINICION E INICIALIZACIÓN DE LOS COLORES PARA LAS LETRAS (CODIGO ANSI)=========//
  val COLOUR_RESET = "\u001B[0m"
  val COLOUR_GREEN = "\u001B[32m"
  val COLOUR_YELLOW = "\u001B[33m"

  //================DEFINICION DE VARIABLES DE PARTIDA===============//
  var numTries: Int
  var playerWins: Boolean
  var guessWord: String
  var userLetter: String
  var correctLetters: Int
  val letterToCount = mutableMapOf<Char, Int>()
  var userOption: Int
  var randomIndex: Int
  var randomWord: String

  //Aqui empieza bucle principal del juego
  do {
    //Escogemos la palabra a partir del indice aleatorio
    randomIndex = dictionary.indices.random()
    randomWord = dictionary[randomIndex].uppercase()

    //Creamos diccionario con ocurrencias de cada carácter para ver aquellos duplicados
    for (c in randomWord) {
      if (!letterToCount.containsKey(c)) {
        letterToCount[c] = 1
      }
      else {
        letterToCount[c] = letterToCount.getValue(c) + 1
      }
    }

    println(letterToCount)//debug
    println(randomWord) //debug

    //Reseteamos valores iniciales para la nueva partida
    numTries = 0
    playerWins = false
    println("Intenta adivinar la palabra (son 5 letras!) ")

    //Aqui empieza el bucle de la partida
    do{
      //Reseteamos valores iniciales para el nuevo intento
      guessWord = ""
      correctLetters = 0
      println("Te quedan ${6 - (numTries)} intentos")
      repeat(5) { index->
        println("Introduce la letra ${index + 1}")
        userLetter = readln().uppercase()
        while (userLetter.isEmpty()){
          println("Tines que escribir algo...")
          userLetter = readln().uppercase()
        }
        if (userLetter.length > 1) {
          println("Has introducido mas de un carácter. Se recogerá solo el primero.")
          userLetter = userLetter.slice(0..0)
        }
        println("Ha introducido la letra $userLetter")
        guessWord += userLetter
      }
      for (i in guessWord.indices) {
        if (guessWord[i] in randomWord && guessWord[i] == randomWord[i]){
          print("$COLOUR_GREEN${guessWord[i]}$COLOUR_RESET ")
          correctLetters++
        }
        else if (guessWord[i] in randomWord) {
          print("$COLOUR_YELLOW${guessWord[i]}$COLOUR_RESET ")
        }
        else {
          print("${guessWord[i]} ")
        }
      }
      if (correctLetters == 5) {
        playerWins = true
        println("Enhorabuena has ganado!")
      }
      else {
        numTries++
        if (numTries == 5) {
          println("Una pena, no has acertado la palabra...")
        }
      }
      println("=================================")
    }while(numTries < 5 && !playerWins) //seguimos intentando mientras no adivinemos la palabra y no hayamos superado el número máximo de intentos
    println("Quieres hacer otra partida?")
    println("SI --> Pulsa 1")
    println("NO --> Pulsa 2")
    userOption = readln().toInt()
    while (userOption < 1 || userOption > 2) {
      println("Opción no válida")
      println("SI --> Pulsa 1")
      println("NO --> Pulsa 2")
      userOption = readln().toInt()
    }
  }while(userOption == 1) //seguimos jugando mientras el jugador así lo desee
  println("Apagando WORDLE...")

}