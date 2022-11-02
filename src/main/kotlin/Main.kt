fun main(args: Array<String>) {
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

  val COLOUR_RESET = "\u001B[0m"
  val COLOUR_GREEN = "\u001B[32m"
  val COLOUR_YELLOW = "\u001B[33m"

  var randomIndex = dictionary.indices.random()
  var randomWord = dictionary[randomIndex].uppercase()

  var numTries: Int
  var playerWins: Boolean
  var guessWord: String
  var userLetter: String
  var correctLetters: Int

  println(randomWord) //debug

  //Aqui empieza bucle del juego

  numTries = 0
  playerWins = false
  guessWord  = ""
  userLetter = ""
  correctLetters = 0

  do{
    println("Intenta adivinar la palabra (5 letras!) ")
    println("Te quedan ${6 - (numTries + 1)} intentos")
    repeat(5) { index->
      println("introduce la letra ${index + 1}")
      userLetter = readln().slice(0..0).uppercase()
      guessWord += userLetter
    }
    if(randomWord == guessWord) playerWins = true;
    else {
      for (i in 0..guessWord.length-1) {
        if (guessWord[i] in randomWord && guessWord[i] == randomWord[i]){
          print("$COLOUR_GREEN${guessWord[i]}$COLOUR_RESET ")
          correctLetters++
        }
        else if (guessWord[i] in randomWord) {
          print("$COLOUR_YELLOW${guessWord[i]}$COLOUR_RESET ")
          correctLetters--
        }
        else {
          print("${guessWord[i]} ")
          correctLetters--
        }
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
      guessWord = ""
    }
    println("=================================")
  }while(numTries < 5 || !playerWins)

}