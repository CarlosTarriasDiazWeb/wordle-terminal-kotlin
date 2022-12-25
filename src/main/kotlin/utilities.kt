import java.io.File

/**
 * Emplena el diccionari de paraules a partir d'un fitxer extern.
 * @param fileName String: Path relatiu al arxiu de tipus text amb les paraules.
 * @param dictionary ArrayList<String>: Estructura buïda que contindrá totes les paraules del joc.
 */
fun loadFile(fileName: String, dictionary: ArrayList<String>) {
  if (fileName.isNotEmpty()) {
    val path = "src/main/kotlin/$fileName"
    val lines: List<String> = File(path).readLines()
    lines.forEach { line -> dictionary.add(line) }
  }
}

/**
 * Escull una paraula de forma aleatòria del diccionari generat.
 * @param dictionary ArrayList<String>: Conté totes les paraules del joc.
 * @return String : Paraula aleatòria del diccionari.
 */
fun getRandomWord(dictionary: ArrayList<String>): String = dictionary[dictionary.indices.random()].uppercase()
