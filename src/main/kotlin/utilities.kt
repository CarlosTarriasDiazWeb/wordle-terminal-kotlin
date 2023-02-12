import java.io.File
import java.util.Scanner
import kotlin.io.path.*

/**
 * Emplena el diccionari de paraules a partir d'un fitxer extern.
 * @param language String: Representa l'idioma de les paraules amb les que juguem.
 * @param dictionary ArrayList<String>: Estructura buïda que conté les paraules amb les que es jugarà al joc.
 */
fun loadFile(language: String, dictionary: ArrayList<String>) {
  //Correspondència idioma->arxiu de paraules.
  val languageToFile = mapOf(
    "ES" to "test1.txt",
    "EN" to "test2.txt"
  )

  //Si existeix la correspondència carreguem el diccionari corresponent.
  if (language.isNotEmpty() && languageToFile[language] != null) {
    val fileName = languageToFile[language]
    val path = "src/main/kotlin/$fileName"
    val file = File(path)
    if (file.isFile && file.exists() && file.length() > 0) { //Si el fitxer té contingut i existeix carreguem les paraules.
      val lines: List<String> = file.readLines()
      lines.forEach { line -> dictionary.add(line) }
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
fun getRandomWord(dictionary: ArrayList<String>): String {
  if (dictionary.isEmpty()) return ""
  return dictionary[dictionary.indices.random()].uppercase()
}

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

  //Llista auxiliar per pintar caràcters al final.
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

  //Després comprovem de les lletres que no coincideixen quines estan incloses a la paraula secreta.
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
 * nou(en cas que no hagi cap registrat) o un que ja consti en el fitxers d'usuaris.
 * @param folderRoute String: Ruta de la carpeta del fitxer d'usuaris.
 * @return String : Cadena que representa l'usuari
 */
fun loadUser(folderRoute: String) : String {
  if (folderRoute.isEmpty()) return ""
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
 * @param scanner Scanner: Flux de dades d'entrada que introdueix l'usuari.
 * @return String : Nom de l'usuari creat.
 */
fun createUser(userFileName: String, scanner: Scanner): String {
  if (userFileName.isEmpty()) {
    println("Error al crear el nuevo usuario. No se proporciona la ruta del archivo de usuarios del sistema.")
    return ""
  }
  println("Por favor, introduce el nombre del nuevo usuario para poder jugar:")

  var userName = scanner.next().trim()
  while (userName.isEmpty()) { //Anem demanant el nou usuari fins que tingui un valor.
    println("El nombre de usuario no puede estar vacío. Vuélvelo a intentar por favor:")
    userName = readln().trim()
  }

  //Afegim el nom introduït al fitxer d'usuaris.
  val userFile = File(userFileName)
  userFile.createNewFile()
  userFile.appendText("${userName}\n")
  return userName
}

/**
 * S'encarrega de cambiar l'idioma de les paraules del joc i carregar el diccionari corresponent.
 * @param language String: Representa l'idioma de les paraules amb les que juguem actualment.
 * @param newLanguage String: Nou idioma amb que volem jugar.
 * @param dictionary ArrayList<String>: Conté les paraules del joc.
 * @return String : El nou llenguatge escollit.
 */
fun changeLanguage(language: String, newLanguage: String, dictionary: ArrayList<String>): String{
  val languages = setOf("ES", "EN")

  //Si el llenguatge introduït no consta en els llenguatges disponibles no fem res.
  if (newLanguage.isEmpty() || newLanguage !in languages) {
    println("Ha habido un error al cambiar de lenguaje. Cancelando operación...")
    return language
  }

  if (newLanguage == language) {
    println("Se ha seleccionado el mismo lenguaje. Ningún cambio producido")
    return language
  }

  //Actualitzem diccionari amb les paraules que queden per jugar en el llenguatge seleccionat.
  dictionary.clear()
  loadFile(newLanguage, dictionary)
  return newLanguage
}

/**
 * Canvia l'usuari actual per un altre usuari que introduïm per terminal.
 * @param folderRoute String: Representa la ruta cap a la carpeta d'usuaris.
 * @param currentUser String: Usuari actual del joc.
 * @param newUser String: Usuari al que volem canviar.
 */
fun changeUser(folderRoute: String, currentUser: String, newUser: String): String {
  val folder = File(folderRoute)
  var lines: List<String>
  //Si la carpeta d'usuaris no existeix o no hi han fitxers d'usuari no fem res
  if (!folder.exists() || folder.listFiles().isNullOrEmpty()) {
    println("Problema con el archivo de usuarios. Cancelando operación...")
    return currentUser
  }

  //Cerquem pels arxius d'usuari l'usuari al que volem canviar.
  for (file in folder.listFiles()!!) {
    if (file.length() > 0) { //Si té contingut, llegim usuaris.
      lines = file.readLines()
      for (line in lines) {
        if (line == newUser) { //Si el trobem, fem el canvi.
          println("Usuario $newUser encontrado. Actualizando información...")
          return newUser
        }
      }
    }
    else continue //Seguim cercant...
  }
  println("No se ha encontrado el usuario seleccionado. Cancelando operación...")
  return currentUser
}

/**
 * Actualitza l'històric de paraules encertades del usuari que està jugant actualment.
 * @param userWordsRoute String : Ruta cap al arxiu d'històric de paraules del usuari concret.
 * @param wordsHistory MutableList<MutableList<String>> : Estructura que conté les diferents paraules encertades amb el seu índex i intent corresponent.
 * @param randomWord String : Nova paraula que ha encertat l'usuari.
 * @param accessIndex Int : Índex on es troba la paraula en el seu diccionari.
 * @param numTries Int : Número d'intents que ha costat encertar la paraula secreta actual.
 */
fun saveWords(userWordsRoute:String,
              wordsHistory:MutableList<MutableList<String>>,
              randomWord: String,
              accessIndex: Int,
              numTries: Int
)
{
  val userWordsFile = File(userWordsRoute)
  if (!userWordsFile.exists()) userWordsFile.createNewFile()
  //Netejem i sobreescrivim l'històric de paraules.
  userWordsFile.writeText("")
  for (word in wordsHistory) {
    userWordsFile.appendText("${word[0]},${word[1]},${word[2]}\n")
  }
  userWordsFile.appendText("$randomWord,$accessIndex,$numTries\n")
}

/**
 * Actualitza les dades estadístiques del jugador.
 * @param userSaveDataPath String : Ruta cap al arxiu d'estadñistiques del usuari.
 * @param userStats Array<Any> : Dades estadístiques després de jugar la partida (que volem escriure al fitxer).
 * @param numOfTriesAccomulate IntArray : Emmagatzema el total de paraules que s'han encertat per 1..6 intents respectivament.
 * @param totalWords Int : Nombre de paraules amb que pot jugar l'usuari.
 */
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
  //Escrivim la primera línia amb els camps estadístics.
  saveFile.appendText("${userStats[0]},${userStats[1]},${userStats[2]},${userStats[3]},${userStats[4]}\n")
  //Escrivim la segona línia amb els intents acomulats.
  for (i in numOfTriesAccomulate.indices) {
    if (i == numOfTriesAccomulate.lastIndex) saveFile.appendText("${numOfTriesAccomulate[i]}\n")
    else saveFile.appendText("${numOfTriesAccomulate[i]},")
  }
  //Escrivim la tercera línia amb les paraules totals restants del joc.
  saveFile.appendText("${totalWords}\n")
}

/**
 * Per carregar dades d'històric o dades estadístiques al joc i poder actualitzar l'estat del joc per a l'usuari actual.
 * @param userSaveDataPath String : Representa la ruta cap al arxiu d'històric o de dades del usuari actual.
 * @param option String : Per indicar si carreguem dades històriques o dades estadístiques.
 * @return MutableList<MutableList<String>> : Estructura que conté les dades que s'han llegit.
 */
fun loadData(userSaveDataPath: String, option:String): MutableList<MutableList<String>> {

  //Si la ruta no es válida no carreguem res.
//  if (!userSaveDataPath.contains("./savedata/") || !userSaveDataPath.contains("./history/")) return mutableListOf()

  val saveFile = File(userSaveDataPath)
  val userData = mutableListOf<MutableList<String>>()
  if (saveFile.exists()) { //Si tenim dades estadístiques o de historial mostrem missatge respectiu.
    when (option) {
      "hist" -> {
        println("Archivo de histórico encontrado, cargando datos...")
      }
      "save" -> {
        println("Archivo de guardado encontrado, cargando datos...")
      }
    }

  }
  else { //Si l'usuari no té fitxer de dades estadístiques creem un de nou amb dades inicialitzades a 0.
    println("No se ha encontrado archivo de $option. Creando archivo")
    saveFile.createNewFile()
    when (option) {
      "hist" -> {
        saveFile.appendText("0,0,0,0,0.0\n")
        saveFile.appendText("0,0,0,0,0,0\n")
        saveFile.appendText("218")
      }
      //No fa falta posar valors per defecte als arxius d'històric de paraules encertades, pot estar buit.
    }
  }

  //Retornem l'estructura amb les dades que s'han carregat per poder actualitzar l'estat del joc per l'usuari actual.
  for (line in saveFile.readLines()) {
    val dataList = line.split(",")
    userData.add(dataList.toMutableList())
  }
  return userData
}

/**
 * Actualitza el diccionari amb les paraules que es por jugar (és a dir, les que ja han sigut jugades les substituïm per "").
 * @param wordsData MutableList<MutableList<String>> : Estructura que conté les diferents paraules encertades amb el seu índex i intent corresponent.
 * @param dictionary ArrayList<String>: Conté les paraules del joc.
 */
fun updateDictionary(wordsData: MutableList<MutableList<String>>, dictionary: ArrayList<String>) {
  for (wordData in wordsData) {
    dictionary[wordData[1].toInt()] = ""
  }
}

/**
 * Actualitza les paraules encertades en l'intent corresponent per a la nova partida.
 * @param data MutableList<String> : Estructura que conté les paraules encertades en un intent concret de les partides anteriors.
 * @param tries IntArray : Conté les paraules encertades en un intent concret per a la nova partida.
 */
fun updateTriesRegistry(data:MutableList<String>, tries: IntArray) {
  for (i in data.indices) {
    tries[i] = data[i].toInt()
  }
}