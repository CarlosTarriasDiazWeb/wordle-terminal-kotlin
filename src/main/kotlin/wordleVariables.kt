
//============Colors de les lletres (codi ANSI)==========//
val colors = mutableMapOf(
  "reset" to "\u001B[0m",
  "green" to "\u001B[32m",
  "yellow" to "\u001B[33m",
  "white" to "\u001b[47m" //Per pintar els blocs en l'historial de mitjanes d'intents.
)

//================Variables de la partida===============//
//Variables per controlar estat de la partida.
var numTries: Int = 0 //Emmagatzema el nombre d'intents, màxim 6.
var playerWins: Boolean = false //Controla si l'usuari guanya o no la partida.
var guessWord: String = "" //Paraula que escriu l'usuari a cada intent de la partida.
var correctLetters: Int = 0 //Comptador de lletres correctes, les que es pinten en verd.
val letterToCount = mutableMapOf<Char, Int>() //Diccionari d'ocurrències de cada caràcter.
var userOption: Int = -1 //Recull si l'usuari vol continuar jugant o no.
var randomWord: String = "" //Paraula aleatòria escollida del diccionari.

//Variables per calcular les estadístiques del joc.
var continuousGuessedWords = 0 //Recull la ratxa actual de paraules encertades.
var bestContinuousGuessedWords = 0 //Recull la millor ratxa de paraules encertades.
var numberOfTotalGuessedWords = 0 //Recull el nombre de paraules encertades respecte al total.
var wordsPercentage: Double = 0.0 //Percentatge de paraules encertades respecte al total.
var currentNumberOfPlays = 0 //Recull el nombre total de partides jugades.
val medianOfTries = DoubleArray(6){0.0} //Recull la mitjana d'intents del total de partides.
val numOfTriesAccomulate = IntArray(6){0} //A cada posició i-1 recull el total de paraules que s'han encertat amb i intents.
var totalWords = 109 //Recull el total de paraules totals que es podran endevinar al joc. Cada paraula només pot sortir una sola vegada.
