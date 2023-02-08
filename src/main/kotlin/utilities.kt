import org.intellij.lang.annotations.Language
import java.io.File
import java.nio.file.Path
import java.util.Scanner
import kotlin.io.path.*

/**
 * Emplena el diccionari de paraules a partir d'un fitxer extern.
 * @param language String: Representa l'idioma de les paraules amb les que juguem.
 * @param dictionary ArrayList<String>: Estructura buïda que contindrá totes les paraules del joc.
 */
fun loadFile(language: String, dictionary: ArrayList<String>) {
  if (language.isNotEmpty()) {
    val fileName = if (language == "Espanyol") "test1.txt" else if (language == "Angles") "test2.txt" else ""
    val path = "src/main/kotlin/$fileName"
    val file = File(path)
    if (file.isFile && file.exists()) {
      val lines: List<String> = file.readLines()
      lines.forEach { line -> dictionary.add(line) }
      println(dictionary.size)
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
 * Emplena el diccionari amb el nombre de vegades que apareix cada caràcter en la paraula seleccionada.
 * @param letterToCount MutableMap<Char,Int>: Diccionari buit que tindrá les ocurrències de cada caràcter.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 */
fun setLetterToCount(letterToCount:MutableMap<Char,Int>, randomWord: String) {
  if (randomWord.length == 5) {
    for (c in randomWord) {
      if (!letterToCount.containsKey(c)) {
        letterToCount[c] = 1
      }
      else {
        letterToCount[c] = letterToCount.getValue(c) + 1
      }
    }
  }
}

/**
 * Processa la paraula que l'usuari introduirà com a resposta al intent.
 * @return String : Paraula introduïda per l'usuari.
 */
fun insertWord() : String {
  var userLetter: String
  var guessWord = ""
  repeat(5) { index->
    println("Introduce la letra ${index + 1}")
    userLetter = readln().uppercase() //cal pasar-ho ja que la paraula generada està en majúscules.
    while (userLetter.isEmpty()){ //Mentre l'usuari no introdueixi res li anem demanant la lletra.
      println("Tienes que escribir algo...")
      userLetter = readln().uppercase()
    }
    if (userLetter.length > 1) { //Per controlar que només es recolli només un caràcter cada cop que demanem input al usuari.
      println("Has introducido más de un carácter. Se recogerá sólo el primero.")
      userLetter = userLetter.slice(0..0)
    }
    println("Ha introducido la letra $userLetter")
    guessWord += userLetter
  }

  return guessWord
}

/**
 * Imprimeix per terminal la paraula que ha introduït el usuari amb els colors adients segons les regles del joc i conta el nombre de caràcters que coincideixen amb la paraula generada.
 * @param guessWord String: Paraula que ha introduït l'usuari.
 * @param randomWord String: Paraula aleatòria seleccionada de la partida actual.
 * @param letterToCount MutableMap<Char,Int>: Diccionari que té les ocurrències de cada caràcter.
 * @param colors MutableMap<String, String>: Diccionari que conté els colors que amb què es pintaran els caràcters.
 * @return Int : Nombre de caràcters correctes respecte la paraula original.
 */
fun printWord(guessWord: String, randomWord: String, letterToCount: MutableMap<Char, Int>, colors: MutableMap<String, String>): Int {
  var correctLetters = 0
  for (i in guessWord.indices) {
    //Per controlar el nombre de vegades que apareix un caràcter concret a la paraula tenim en compte el seu valor al diccionari
    if (guessWord[i] == randomWord[i] && letterToCount.getValue(guessWord[i]) > 0){
      printChar(colors["green"], colors["reset"], guessWord[i])
      correctLetters++
      //Controlem duplicitat del caràcter
      letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
    }
    else if (guessWord[i] in randomWord && letterToCount.getValue(guessWord[i]) > 0) {
      printChar(colors["yellow"], colors["reset"], guessWord[i])
      //Controlem duplicitat del caràcter
      letterToCount[guessWord[i]] = letterToCount.getValue(guessWord[i]) - 1
    }
    else {
      print("${guessWord[i]}")
    }
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

    //Si existeixen usuaris, càrreguem el primer usuari del primer fitxers de la carpeta users.
    if (usersFiles[0].readLines().isNotEmpty()) { //Comprovem que hi hagi contigut al fitxer.
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
fun changeLanguage(language: String, dictionary: ArrayList<String>): String{
  dictionary.clear()
  val newLanguage : String
  if (language == "Espanyol") {
    newLanguage = "Angles"
    loadFile(newLanguage, dictionary)
    return newLanguage
  }
  newLanguage = "Espanyol"
  loadFile(newLanguage, dictionary)
  return newLanguage
}

fun changeUser(folderRoute: String, currentUser: String, newUser: String): String {
  val folder = File(folderRoute)
  var lines: List<String>
  if (!folder.exists() || folder.listFiles().size == 0) {
    println("Problema con el archivo de usuarios. Cancelando operación...")
    return currentUser
  }

  for (file in folder.listFiles()) {
    if (file.length() > 0) {
      lines = file.readLines()
      for (line in lines) {
        if (line == newUser) {
          println("Usuario ${newUser} encontrado. Actualizando el sistema...")
          return newUser
        }
      }
    }
    else {
      continue
    }

  }
  println("Problema con los archivos de usuarios. Cancelando operación...")
  return currentUser
}