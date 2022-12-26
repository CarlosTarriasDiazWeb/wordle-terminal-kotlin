import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.ArrayList

internal class UtilitiesKtTest {

  private val dictionary = ArrayList<String>()
  private val letterToCount:MutableMap<Char,Int> = mutableMapOf<Char,Int>()
  private val colors = mutableMapOf(
    "reset" to "\u001B[0m",
    "green" to "\u001B[32m",
    "yellow" to "\u001B[33m",
    "white" to "\u001b[47m" //Per pintar els blocs en l'historial de mitjanes d'intents.
  )
  private val medianOfTries = DoubleArray(6){0.0} //array que recoge la media de intento del total de partidas.
  private val numOfTriesAccomulate = intArrayOf(0,2,3,5,1,1) //array que en cada posicion i-1 el total de palabras que se han acertado con i intentos.

  //loadFile() Tests
  @Test
  fun checkIfDictionaryIsNotEmpty() {
    loadFile("test1.txt", dictionary)
    assertTrue(dictionary.size > 0)
  }

  @Test
  fun checkIfDictionaryIsEmptyIfNoFileProvided() {
    loadFile("", dictionary)
    assertFalse(dictionary.size > 0)
  }

  //getRandomWord() Tests
  @Test
  fun checkIfRandomWordIsValid() {
    loadFile("test1.txt", dictionary)
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isNotEmpty() && randomWord.length == 5)
  }

  //setLetterToCount() Tests
  @Test
  fun checkIfMapIsEmptyIfWordIsEmpty() {
    setLetterToCount(letterToCount, "")
    assertTrue(letterToCount.isEmpty())
  }
  @Test
  fun checkIfMapIsCorrect() {
    val word = "GORRA"
    val testMap = mutableMapOf(
            'G' to 1,
            'O' to 1,
            'R' to 2,
            'A' to 1
    )
    setLetterToCount(letterToCount, word)
    assertEquals(letterToCount.keys, testMap.keys)
  }

  @Test
  fun checkIfMapIsEmptyIfWordIsInvalid() {
    val word = "GORRAS"
    setLetterToCount(letterToCount, word)
    assertTrue(letterToCount.isEmpty())
  }

  // printWord() Tests
  @Test
  fun checkIfCorrectLettersIsFour() {
    var word = "GORRA"
    var wordGuess = "GORRI"
    setLetterToCount(letterToCount,"GORRA")
    assertEquals(printWord(wordGuess, word, letterToCount, colors), 4)
  }
  @Test
  fun checkIfCorrectLettersIsValid() {
    val word = "GORRA"
    val wordGuess = "XERNA"
    setLetterToCount(letterToCount,word)
    assertTrue(printWord(wordGuess, word, letterToCount, colors) <=5)
  }
  @Test
  fun checkIfCorrectLettersIsFiveIfGuessWordIsCorrect() {
    val word = "GORRA"
    val wordGuess = "GORRA"
    setLetterToCount(letterToCount,"GORRA")
    assertEquals(printWord(wordGuess, word , letterToCount, colors), 5)
  }

  //calculateGameHistogram Tests
  @Test
  fun checkIfNumberOfTotalGuessedWordsIsZeroStatisticsAreEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 3, 0)
    assertTrue(medianOfTries.all(){it.toInt() == 0 })
  }
  @Test
  fun checkIfTriesIsInvalidStatisticsAreEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 0, 5)
    assertTrue(medianOfTries.all(){it.toInt() == 0 })
  }
  @Test
  fun checkIfParamsAreInvalidStatisticsIsEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 0, 0)
    assertTrue(medianOfTries.all(){it.toInt() == 0 })
  }
  @Test
  fun checkIfParamsAreValidStatisticsIsNotEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 2, 5)
    assertTrue(medianOfTries.any(){it.toInt() != 0 })
  }

}