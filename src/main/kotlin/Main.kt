import java.io.File

fun main() {

  //  Autor: Carlos Tarrias Diaz
  //  Proyecto troncal M3 - Wordle

  //============DEFINICION E INICIALIZACION DEL ARRAY DE PALABRAS QUE TENDRA EL JUEGO

  // 1.DICICIONARIO BÁSICO
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

  // MEJORA: CARGA DE DICCIONARIO EXTERNO
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

  //================DEFINICION DE VARIABLES DE PARTIDA===============//
  var numTries: Int //almacena el número de intentos, máximo 6.
  var playerWins: Boolean //controla si el usuario gana la partida o no.
  var guessWord: String //palabra que escribe el usuario en cada intento de la partida.
  var userLetter: String //contiene la letra que compone guessWord
  var correctLetters: Int //contador de letras correctas, las que se pintan en verde.
  var letterToCount = mutableMapOf<Char, Int>() //diccionario de ocurrencias de cada carácter.
  var userOption: Int //recoge si el usuario quiere seguir jugando o no.
  var randomIndex: Int
  var randomWord: String

  //Aqui empieza bucle principal del juego.
  do {
    //Escogemos la palabra del diccionario a partir del indice aleatorio.
    randomIndex = dictionary.indices.random()
    randomWord = dictionary[randomIndex].uppercase()

    //Creamos diccionario con ocurrencias de cada carácter para ver aquellos duplicados en cada intento.
    for (c in randomWord) {
      if (!letterToCount.containsKey(c)) {
        letterToCount[c] = 1
      }
      else {
        letterToCount[c] = letterToCount.getValue(c) + 1
      }
    }

    //println(randomWord) //debug

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
      if (correctLetters == 5) {
        playerWins = true
        println("Enhorabuena has ganado!")
        //Limpiamos diccionario de ocurrencias
        letterToCount = mutableMapOf()
      }
      else {
        numTries++
        if (numTries == 6) {
          println("Una pena, no has acertado la palabra...")
          println("La palabra era: $randomWord")
        }
      }
      println("=================================")
    }while(numTries < 6 && !playerWins) //seguimos intentando mientras no adivinemos la palabra y no hayamos superado el número máximo de intentos.
    //Limpiamos diccionario de ocurrencias
    letterToCount = mutableMapOf()
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
  }while(userOption == 1) //seguimos jugando mientras el jugador así lo indique.
  println("Apagando WORDLE...")

}