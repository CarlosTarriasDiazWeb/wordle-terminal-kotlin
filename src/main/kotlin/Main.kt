import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

//  Autor: Carlos Tarrias Diaz
//  Projecte troncal M3 - Wordle

// DESCRIPCIÓ DEL JOC
/*
  El joc consisteix en endevinar una paraula concreta de cinc lletres en un màxim de sis intents.
  El jugador tindrà 6 intents per endevinar la paraula (la paraula pot estar formada en cada posició per totes les lletres de l'abecedari),
  en cas contrari haurà perdut la partida.
  S’ha d’anar mostrant, en tot moment, quines lletres s’han encertat en la posició, o s’han encertat, però no estan al lloc adient.
  Al joc original ho mostra en color verd i groc respectivament.
  El jugador al final del joc pot decidir si vol jugar una altra partida o no.
 */

/**
 * Punt d'entrada del programa.
 */
fun main() {
  // Càrrega de diccionari de paraules en espanyol (per defecte) mitjançant el fitxer corresponent.
  val dictionary = ArrayList<String>()
  loadFile(language, dictionary)

  //Càrrega del primer usuari registrat.
  var userLoaded = loadUser("./users/")

  //Sortim del programa si hi ha cap error carregant l'usuari.
  if (userLoaded.isEmpty()) exitProcess(-1)

  //Bucle principal del joc
  do {

    println("Se ha cargado el usuario $userLoaded")
    var userData = loadData("./history/Hist${userLoaded}.txt", "hist")

    //Es carreguen les dades del usuari per defecte o el nou usuari creat(seran dades a 0), segons el cas.
    //Càrrega d'estadístiques.
    continuousGuessedWords = userData[0][0].toInt()
    numberOfTotalGuessedWords = userData[0][1].toInt()
    bestContinuousGuessedWords = userData[0][2].toInt()
    currentNumberOfPlays = userData[0][3].toInt()
    wordsPercentage = userData[0][4].toDouble()
    //Càrrega d'intents acomulats.
    updateTriesRegistry(userData[1], numOfTriesAccomulate)
    totalWords = userData[2][0].toInt()

    //Càrrega de paraules encertades en espanyol.
    var wordsHistory = loadData("./savedata/${language}${userLoaded}.txt", "save")

    //Hem de tenir en compte que l'usuari pot haver jugat amb unes quantes paraules en partides anteriors i no volem que ens toquin una altra vegada.
    updateDictionary(wordsHistory, dictionary)

    println(dictionary) //debug

    var userOptionMenu: Int
    do {
      showMenu(language, userLoaded)
      userOptionMenu = readln().toInt()
      when(userOptionMenu) {
        1 -> { //Canvi de llenguatges (fa falta recarregar el nou diccionari amb les paraules que queden per jugar en l'idioma seleccionat).
          println("Introduce el lenguaje al que quieres cambiar:")
          println("Lenguajes disponibles: ES(Español) - EN(Inglés) ")
          val newLanguageSelected = readln().trim().uppercase()
          language = changeLanguage(language, newLanguageSelected, dictionary)
          wordsHistory = loadData("./savedata/${language}${userLoaded}.txt", "save")
          updateDictionary(wordsHistory, dictionary)
        }
        2 -> { //Canvi d'usuari (fa falta recarregar el diccionari corresponent i carregar les dades de guardat del usuari seleccionat).
          println("Introduce el nombre de usuario al que quieras cambiar:")
          val newUserSelected = readln().trim().lowercase()
          userLoaded = changeUser("./users/", userLoaded, newUserSelected)
          userData = loadData("./history/Hist${userLoaded}.txt", "hist")
          continuousGuessedWords = userData[0][0].toInt()
          numberOfTotalGuessedWords = userData[0][1].toInt()
          bestContinuousGuessedWords = userData[0][2].toInt()
          currentNumberOfPlays = userData[0][3].toInt()
          wordsPercentage = userData[0][4].toDouble()
          updateTriesRegistry(userData[1], numOfTriesAccomulate)
          totalWords = userData[2][0].toInt()
          wordsHistory = loadData("./savedata/${language}${userLoaded}.txt", "save")
          updateDictionary(wordsHistory, dictionary)
        }
        3 -> { //Mostra les paraules encertades en l'idioma actual.
          println("Mostrando histórico de palabras acertadas:")
          printHistory(wordsHistory)
        }
        0 -> println("Comenzando juego...")
        else -> println("No existe esta opción. Vuélvelo a intentar:")
      }
    }while(userOptionMenu != 0) //No sortim del menú de joc mentre no començem una partida.

    do {
      randomWord = getRandomWord(dictionary)
    }while(randomWord.isEmpty()) //Anem seleccionant una paraula del diccionari fins que escollim una que no estigui buïda.

    //Marquem la paraula com a buïda per no tornar a seleccionar-la
    val accessIndex = dictionary.indexOf(randomWord.lowercase()
      .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
    dictionary[accessIndex] = "" //Capitalitzem la paraula per a que coincideixi exactament amb el format de les paraules en el arxiu de lectura.

    //Disminuim el nombre total de paraules del joc.
    totalWords--

    println(randomWord) //debug

    //Setejem intents inicials per a la nova partida.
    numTries = 0
    println("Intenta adivinar la palabra (son 5 letras!) ")

    //Bucle de la partida.
    do{

      println("Te quedan ${6 - (numTries)} intentos")

      guessWord = insertWord()

      correctLetters = printWord(guessWord, randomWord, colors)

      numTries++

      playerWins = playerWins(correctLetters)

      if (playerWins) { //Si el jugador encerta la paraula.
        println("Enhorabuena has ganado!")

        //Incrementem el comptador de paraules encertades y el de la ratxa actual.
        continuousGuessedWords++
        numberOfTotalGuessedWords++

        if (continuousGuessedWords > bestContinuousGuessedWords) bestContinuousGuessedWords = continuousGuessedWords

      }
      else { //Si el jugador NO encerta la paraula.
        if (numTries == 6) {
          //Reiniciem la ratxa de paraules encertades.
          continuousGuessedWords = 0
          printLoseMessage(randomWord)
        }
      }
      println("=================================")
    }while(numTries < 6 && !playerWins) //Si l'usuari no endevina la paraula ho segueix intentant mentre no se superi el màxim d'intents.

    //Actualitzem el nombre de partides guanyades.
    currentNumberOfPlays++

    //Calculem nombre de paraules encertades respecte tot el diccionari de paraules.
    wordsPercentage = (numberOfTotalGuessedWords/totalWords.toDouble())*100.0

    showGameStatistics(currentNumberOfPlays, numberOfTotalGuessedWords, wordsPercentage, totalWords, continuousGuessedWords, bestContinuousGuessedWords)

    //Si el jugador ha endevinat la paraula actualitzem el comptador d'intents corresponent a la partida i actualitzem els valors de les mitjanes.
    if (playerWins) {
      calculateGameHistogram(medianOfTries, numOfTriesAccomulate, numTries, numberOfTotalGuessedWords)
      //Afegim la paraula encertada al historial de paraules encertades del usuari.
      saveWords("./savedata/${language}${userLoaded}.txt", wordsHistory, randomWord, accessIndex, numTries)
    }
    //Enregistrem els pàràmetres de la partida actualitzats al arxiu d'usuari corresponent.
    saveHistData(
      "./history/Hist${userLoaded}.txt",
      arrayOf(continuousGuessedWords, numberOfTotalGuessedWords, bestContinuousGuessedWords, currentNumberOfPlays, wordsPercentage),
      numOfTriesAccomulate,
      totalWords
    )

    showGameHistogram(medianOfTries, numOfTriesAccomulate, colors, numberOfTotalGuessedWords)

    userOption = processUserInput(totalWords)

  }while(userOption == 1 && totalWords > 0) //Seguim jugant mentre el jugador ho indiqui i quedin paraules per jugar.

  println("Apagando WORDLE...")
}