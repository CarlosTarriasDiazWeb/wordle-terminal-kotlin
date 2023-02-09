import java.io.File
import java.util.Scanner
import kotlin.io.path.*

/**
 * Emplena el diccionari de paraules a partir d'un fitxer extern.
 * @param language String: Representa l'idioma de les paraules amb les que juguem.
 * @param dictionary ArrayList<String>: Estructura buïda que contindrá totes les paraules del joc.
 */
fun loadFile(language: String, dictionary: ArrayList<String>) {
  val languageToFile = mapOf(
    "ES" to "test1.txt",
    "EN" to "test2.txt"
  )
  if (language.isNotEmpty()) {
    val fileName = languageToFile[language]
    val path = "src/main/kotlin/$fileName"
    val file = File(path)
    if (file.isFile && file.exists() && file.length() > 0) {
      val lines: List<String> = file.readLines()
      lines.forEach { line -> dictionary.add(line) }
      //println(dictionary.size) //debug
    }
    else {
      println("Problema al cargar el diccionario de palabras. Cancelando operación...")
    }
  }
}

/**
 * Escull una paraula de forma aleatòria del diccionari generat.
 * @param dictionary ArrayList<String>: Conté totes les paraules del joc.
 * @return String : Paraula aleatòria del diccionari.
 */
fun getRandomWord(dictionary: ArrayList<String>): String = dictionary[dictionary.indices.random()].uppercase()

/**
 * Processa la paraula que l'usuari introduirà com a resposta.
 * @return String : Paraula introduïda per l'usuari.
 */
fun insertWord(): String {
  var guessWord = readln().trim().uppercase()
  while (guessWord.isEmpty() || guessWord.length > 5 || guessWord.length < 5){ //Mentre la longitud de la paraula no sigui vàlida li anem demanant la paraula al usuari.
    println("La palabra tiene que tener 5 letras exactamente. Vuélvelo a intentar, por favor:")
    guessWord = readln().trim().uppercase()
  }
  return guessWord
}

/**
 * Imprimeix per terminal la paraula que ha introduït el usuari amb els colors adients segons les regles del joc i compta el nombre de caràcters que coincideixen amb la paraula generada.
 * @param guessWord String: Paraula que ha introduït l'usuari.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 * @param colors MutableMap<String, String>: Diccionari que conté els colors que amb què es pintaran els caràcters.
 * @return Int : Nombre de caràcters correctes respecte la paraula original.
 */
fun printWord(guessWord: String, randomWord: String, colors: MutableMap<String, String>): Int {
  var correctLetters = 0
  val remainingLetters = randomWord.split("").toMutableList() //Array auxiliar per decidir quines lletres seran d'un color concret.

  val letterToColor = mutableListOf<Array<String>>()
  for (i in guessWord.indices) {
    letterToColor.add(arrayOf(guessWord[i].toString(), "gray"))
  }
  //Primer comprovem les lletres que coincideixen en la mateixa posició que la paraula secreta.
  for (i in guessWord.indices) {
    if (guessWord[i] == randomWord[i]) {
      val indexOfChar = remainingLetters.indexOf(guessWord[i].toString())
      remainingLetters.removeAt(indexOfChar)
      letterToColor[i][1]= "green"
      correctLetters++
    }
  }

  //Després comprovem de les lletres que no coincideixen, quines estan incloses a la paraula secreta.
  for (i in guessWord.indices) {
    if (remainingLetters.contains(guessWord[i].toString()) && guessWord[i] != randomWord[i])
    {
      val indexOfChar = remainingLetters.indexOf(guessWord[i].toString())
      remainingLetters.removeAt(indexOfChar)
      letterToColor[i][1]= "yellow"
    }
  }

  //Pintem les lletres del color corresponent.
  for (pair in letterToColor) {
    if (pair[1] == "gray") print(pair[0])
    else printChar(colors[pair[1]], colors["reset"], pair[0])
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
 * @param medianOfTries DoubleArray: Emmagatzema la mitjana d'intents de tot el joc.
 * @param numOfTriesAccomulate IntArray: Emmagatzema el total de paraules que s'han encertat per 1..6 intents respectivament.
 * @param numTries Int: Nombre d'intents que ha necessitat el jugador per encertar la paraula.
 * @param numberOfTotalGuessedWords Int : Nombre de paraules encertades.
 */
fun calculateGameHistogram(medianOfTries: DoubleArray, numOfTriesAccomulate: IntArray, numTries: Int, numberOfTotalGuessedWords: Int) {
  if (numTries > 0) {
    //Incrementem en un el número d'intents específic.
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

/**
 * S'encarrega de retornar el usuari que jugarà al iniciar la partida (en cas que no el canviem en el menú). Pot ser un usuari
 * nou(en cas que no hagi cap registrat) o un que ja consti en el fitxers de usuaris.
 * @param folderRoute String: Ruta de la carpeta del fitxer d'usuaris.
 * @return String : Cadena que representa l'usuari
 */
fun loadUser(folderRoute: String) : String {
  val usersRoute = Path(folderRoute)
  if (usersRoute.exists()) {
    val usersFiles = usersRoute.listDirectoryEntries()

    //Si no hi han usuaris registrats (no hi ha cap fitxer), creem un de nou.
    if (usersFiles.isEmpty()) {
      println("No existen usuarios en el sistema. ")
      println("Procediendo a crear un nuevo usuario...")
      val scanner = Scanner(System.`in`)
      return createUser(folderRoute + "users.txt", scanner)
    }

    //Si existeixen usuaris, càrreguem el primer usuari del primer fitxer de la carpeta users.
    if (usersFiles[0].readLines().isNotEmpty()) { //Comprovem que hi hagi contingut al fitxer.
      return usersFiles[0].readLines()[0]
    }
    println("El archivo de usuarios existe pero no constan usuarios")
    return ""
  }
  else {
    println("Ha habido un problema a la hora de comprobar la carpeta de usuarios")
  }

  return ""
}

/**
 * S'encarrega d'afegir un nou usuari al sistema amb el nom que introdueixi l'usuari.
 * @param userFileName String: Nom del fitxer on s'emmagatzemen els diferents usuaris.
 * @return String : Nom de l'usuari creat.
 */
fun createUser(userFileName: String, scanner: Scanner): String {
  if (userFileName.isEmpty()) {
    println("Error al crear el nuevo usuario. No se proporciona la ruta del archivo de usuarios del sistema.")
    return ""
  }
  println("Por favor, introduce el nombre del nuevo usuario para poder jugar:")

  var userName = scanner.next().trim()
  while (userName.isEmpty()) {
    println("El nombre de usuario no puede estar vacío. Vuélvelo a intentar por favor:")
    userName = readln().trim()
  }
  val userFile = File(userFileName)
  userFile.createNewFile()
  userFile.appendText("${userName}\n")
  return userName
}

/**
 * Se encarrega de cambiar l'idioma de les paraules del joc i carregar el diccionari corresponent
 */
fun changeLanguage(language: String, newLanguage: String, dictionary: ArrayList<String>): String{
  val languages = setOf("ES", "EN")
  dictionary.clear()

  if (newLanguage.isEmpty() || newLanguage !in languages) {
    println("Ha habido un error al cambiar de lenguaje. Cancelando operación...")
    return language
  }

  loadFile(newLanguage, dictionary)
  return newLanguage
}

fun changeUser(folderRoute: String, currentUser: String, newUser: String): String {
  val folder = File(folderRoute)
  var lines: List<String>
  if (!folder.exists() || folder.listFiles().isEmpty()) {
    println("Problema con el archivo de usuarios. Cancelando operación...")
    return currentUser
  }

  for (file in folder.listFiles()) {
    if (file.length() > 0) {
      lines = file.readLines()
      for (line in lines) {
        if (line == newUser) {
          println("Usuario $newUser encontrado. Actualizando información...")
          return newUser
        }
      }
    }
    else continue
  }
  println("No se ha encontrado el usuario seleccionado. Cancelando operación...")
  return currentUser
}

fun saveWords(userWordsRoute:String,
              wordsHistory:MutableList<MutableList<String>>,
              randomWord: String,
              accessIndex: Int,
              numTries: Int
)
{
  val userWordsFile = File(userWordsRoute)
  if (!userWordsFile.exists()) userWordsFile.createNewFile()
  userWordsFile.writeText("")
  for (word in wordsHistory) {
    userWordsFile.appendText("${word[0]},${word[1]},${word[2]}\n")
  }
  userWordsFile.appendText("$randomWord,$accessIndex,$numTries\n")
}

fun saveHistData(userSaveDataPath: String,
             userStats: Array<Any>,
             numOfTriesAccomulate: IntArray,
             totalWords: Int
          )
{
  val saveFile = File(userSaveDataPath)
  if (!saveFile.exists()) saveFile.createNewFile()
  println("Guardando datos...")
  saveFile.writeText("")
  saveFile.appendText("${userStats[0]},${userStats[1]},${userStats[2]},${userStats[3]},${userStats[4]}\n")
  for (i in numOfTriesAccomulate.indices) {
    if (i == numOfTriesAccomulate.lastIndex) saveFile.appendText("${numOfTriesAccomulate[i]}\n")
    else saveFile.appendText("${numOfTriesAccomulate[i]},")
  }
  saveFile.appendText("${totalWords}\n")
}


fun loadData(userSaveDataPath: String, option:String): MutableList<MutableList<String>> {
  val saveFile = File(userSaveDataPath)
  val userData = mutableListOf<MutableList<String>>()
  if (saveFile.exists()) {
    when (option) {
      "hist" -> {
        println("Archivo de histórico encontrado, cargando datos...")
      }
      "save" -> {
        println("Archivo de guardado encontrado, cargando datos...")
      }
    }

  }
  else {
    println("No se ha encontrado archivo de $option. Creando archivo")
    saveFile.createNewFile()
    when (option) {
      "hist" -> {
        saveFile.appendText("0,0,0,0,0.0\n")
        saveFile.appendText("0,0,0,0,0,0\n")
        saveFile.appendText("218")
      }
    }
  }

  for (line in saveFile.readLines()) {
    val dataList = line.split(",")
    userData.add(dataList.toMutableList())
    println(dataList)
  }
  return userData
}

fun updateDictionary(wordsData: MutableList<MutableList<String>>, dictionary: ArrayList<String>) {
  for (wordData in wordsData) {
    dictionary[wordData[1].toInt()] = ""
  }
}

fun updateTriesRegistry(data:MutableList<String>, tries: IntArray) {
  for (i in data.indices) {
    tries[i] = data[i].toInt()
  }
}