import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.ArrayList

internal class UtilitiesKtTest {

  private val dictionary = ArrayList<String>()
  private val letterToCount:MutableMap<Char,Int> = mutableMapOf()
  private val colors = mutableMapOf(
    "reset" to "\u001B[0m",
    "green" to "\u001B[32m",
    "yellow" to "\u001B[33m",
    "white" to "\u001b[47m"
  )
  private val medianOfTries = DoubleArray(6){0.0}
  private val numOfTriesAccomulate = intArrayOf(0,2,3,5,1,1)
  private val maxWords = 109
  private val word = "GORRA"

  //loadFile() Tests
  @Test
  fun checkIfDictionaryIsNotEmptyWhenLoaded() {
    loadFile("test1.txt", dictionary)
    assertTrue(dictionary.size > 0)
  }
  @Test
  fun checkIfAllWordOfDictionaryAreValid() {
    loadFile("test1.txt", dictionary)
    assertTrue(dictionary.all { it.length == 5 })
  }
  @Test
  fun checkIfAllWordsOfDictionaryAreLoaded() {
    loadFile("test1.txt", dictionary)
    assertTrue(dictionary.size == maxWords)
  }
  @Test
  fun checkIfDictionaryIsEmptyIfNoPathProvided() {
    loadFile("", dictionary)
    assertFalse(dictionary.size > 0)
  }
  @Test
  fun checkIfDictionaryIsEmptyIfPathIsIncorrect() {
    loadFile("dictionary.txt", dictionary)
    assertFalse(dictionary.size > 0)
  }

  //getRandomWord() Tests
  @Test
  fun checkIfRandomWordIsValid() {
    loadFile("test1.txt", dictionary)
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isEmpty() || randomWord.length == 5) //En aquest cas, té sentit que sigui buida ja que una paraula buida ja no es pot utilitzar més!
  }

  //setLetterToCount() Tests
  @Test
  fun checkIfMapIsEmptyIfWordIsEmpty() {
    setLetterToCount(letterToCount, "")
    assertTrue(letterToCount.isEmpty())
  }
  @Test
  fun checkIfMapIsCorrect() {
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
    val wordTwo = "GORRAS"
    setLetterToCount(letterToCount, wordTwo)
    assertTrue(letterToCount.isEmpty())
  }

  // printWord() Tests
  @Test
  fun checkIfCorrectLettersIsFour() {
    val wordGuess = "GORRI"
    setLetterToCount(letterToCount,word)
    assertEquals(printWord(wordGuess, word, letterToCount, colors), 4)
  }
  @Test
  fun checkIfCorrectLettersIsValid() {
    val wordGuess = "XERNA"
    setLetterToCount(letterToCount,word)
    assertTrue(printWord(wordGuess, word, letterToCount, colors) != 5)
  }
  @Test
  fun checkIfCorrectLettersIsFiveIfGuessWordIsCorrect() {
    val wordGuess = "GORRA"
    setLetterToCount(letterToCount,word)
    assertEquals(printWord(wordGuess, word , letterToCount, colors), 5)
  }

  //playerWins() Tests
  @Test
  fun checkIfWinsIfCorrectLettersEqualsFive() {
    assertTrue(playerWins(5))
  }
  @Test
  fun checkIfLosesIfCorrectLettersIsNotFive() {
    assertFalse(playerWins(2))
  }

  //calculateGameHistogram Tests
  @Test
  fun checkIfNumberOfTotalGuessedWordsIsZeroStatisticsAreEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 3, 0)
    assertTrue(medianOfTries.all{it.toInt() == 0 })
  }
  @Test
  fun checkIfTriesIsInvalidStatisticsAreEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 0, 5)
    assertTrue(medianOfTries.all{it.toInt() == 0 })
  }
  @Test
  fun checkIfParamsAreInvalidStatisticsIsEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 0, 0)
    assertTrue(medianOfTries.all{it.toInt() == 0 })
  }
  @Test
  fun checkIfParamsAreValidStatisticsIsNotEmpty() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 2, 5)
    assertTrue(medianOfTries.any{it.toInt() != 0 })
  }

}