import java.nio.file.Path
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.path.*

//  Autor: Carlos Tarrias Diaz
//  Projecte troncal M3 - Wordle

// DESCRIPCIÓ DEL JOC
/*
  El joc consisteix en endevinar una paraula concreta de cinc lletres en un màxim de sis intents.
  El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari),
  en cas contrari haurà perdut la partida.
  S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
  Al joc original ho mostra en color verd i groc respectivament.
  El jugador al final de la partida pot decidir si vol jugar una altra o no.
 */

/**
 * Punt d'entrada del programa.
 */
fun main() {

  //============Array de paraules que tindrá el joc=======//
  // Càrrega de diccionari de paraules en espanyol (per defecte ) mitjançant el fitxer corresponent.
  val dictionary = ArrayList<String>()
  loadFile(language, dictionary)

  //Carga del primer usuari registrat
  var userLoaded = loadUser("./users/")
  if (userLoaded.isNotEmpty()) {
    println("Se ha cargado el usuario $userLoaded")
    var userOption: Int
    do {
      showMenu(language, userLoaded)
      userOption = readln().toInt()
      when(userOption) {
        1 -> language = changeLanguage(language, dictionary)
        2 -> {
          println("Introduce el nombre de usuario al que quieras cambiar:")
          val newUserSelected = readln()
          userLoaded = changeUser("./users/", userLoaded, newUserSelected)
        }
        0 -> println("Comenzando juego...")
        else -> println("No existe esta opción. Vuelvélo a intentar:")
      }
    }while(userOption != 0)

  }

  //Bucle principal del joc
  do {
    do {
      randomWord = getRandomWord(dictionary)
    }while(randomWord.isEmpty()) //Anem seleccionant una paraula del diccionari fins que escollim una que no està buïda.
    //println(dictionary) //debug
    //Marquem la paraula com a buïda per no tornar a seleccionar-la
    var accessIndex = dictionary.indexOf(randomWord.lowercase()
      .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    dictionary[accessIndex] = "" //Capitalitzem la paraula per a que coincideixi exactament amb el format de les paraules en el arxiu de lectura.

    //Disminuim el nombre total de paraules del joc.
    totalWords--

    //Creem diccionari amb ocurrències de cada caràcter per veure els que estan duplicats en cada intent.
    setLetterToCount(letterToCount, randomWord)

    println(randomWord) //debug

    //Setejem intents inicials per a la nova partida.
    numTries = 0

    println("Intenta adivinar la palabra (son 5 letras!) ")

    //Bucle de la partida.
    do{
      //Restaurem els valors inicials d'ocurrències de caràcters per al nou intent.
      for (c in randomWord) {
        letterToCount[c] = randomWord.count{it == c}
      }

      //println(letterToCount) //debug

      println("Te quedan ${6 - (numTries)} intentos")

      guessWord = insertWord()

      correctLetters = printWord(guessWord, randomWord, letterToCount, colors)

      numTries++

      playerWins = playerWins(correctLetters)

      if (playerWins) {
        println("Enhorabuena has ganado!")

        //Incrementem el comptador de paraules encertades y el de la ratxa actual.
        continuousGuessedWords++
        numberOfTotalGuessedWords++

        if (continuousGuessedWords > bestContinuousGuessedWords) bestContinuousGuessedWords = continuousGuessedWords
        //Buidem el diccionari d'ocurrències per a que no s'acomulin a la següent partida.
        letterToCount.clear()
      }
      else {
        if (numTries == 6) {
          //Reiniciem la ratxa d eparaules encertades.
          continuousGuessedWords = 0
          printLoseMessage(randomWord)
        }
      }
      println("=================================")
    }while(numTries < 6 && !playerWins) //Si l'usuari no endevina la paraula ho segueix intentant mentre no se superi el màxim d'intents.

    //Actualitzem el nombre de partides guanyades.
    currentNumberOfPlays++

    //Buidem el diccionari d'ocurrències per a que no s'acomulin a la següent partida.
    letterToCount.clear()

    //Calculem nombre de paraules encertades respecte tot el diccionari de paraules.
    wordsPercentage = (numberOfTotalGuessedWords/109.0)*100.0

    showGameStatistics(currentNumberOfPlays, numberOfTotalGuessedWords, wordsPercentage, totalWords, continuousGuessedWords, bestContinuousGuessedWords)

    //Si el jugador ha endevinat la paraula actualitzem el comptador d'intents corresponent a la partida i actualitzem els valors de les mitjanes.
    if (playerWins) {
      calculateGameHistogram(medianOfTries, numOfTriesAccomulate, numTries, numberOfTotalGuessedWords)
    }

    showGameHistogram(medianOfTries, numOfTriesAccomulate, colors, numberOfTotalGuessedWords)

    userOption = processUserInput(totalWords)

  }while(userOption == 1 && totalWords > 0) //Seguim jugant mentre el jugador ho indiqui i quedin paraules per jugar.

  println("Apagando WORDLE...")
}