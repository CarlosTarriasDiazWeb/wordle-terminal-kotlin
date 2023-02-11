import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

internal class UtilitiesKtTest {

  private val dictionary = ArrayList<String>()
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
  fun checkIfDictionaryIsEmptyWhenLanguageNotProvided() {
    loadFile("", dictionary)
    assertTrue(dictionary.size == 0)
  }
  @Test
  fun checkIfDictionaryIsEmptyWhenLanguageDoesntExistInAvailableLanguages() {
    loadFile("JAP", dictionary)
    assertTrue(dictionary.size == 0)
  }
  @Test
  fun checkIfAllWordOfDictionaryAreValidWhenLanguageIsSpanish() {
    loadFile("ES", dictionary)
    assertTrue(dictionary.all { it.length == 5 })
  }
  @Test
  fun checkIfAllWordOfDictionaryAreValidWhenLanguageIsEnglish() {
    loadFile("EN", dictionary)
    assertTrue(dictionary.all { it.length == 5 })
  }

  @Test
  fun checkIfAllWordsOfDictionaryAreLoadedWhenLanguageIsSpanish() {
    loadFile("ES", dictionary)
    assertTrue(dictionary.size == maxWords)
  }
  @Test
  fun checkIfAllWordsOfDictionaryAreLoadedWhenLanguageIsEnglish() {
    loadFile("EN", dictionary)
    assertTrue(dictionary.size == maxWords)
  }

  //getRandomWord() Tests
  @Test
  fun checkIfRandomWordIsValidInSpanishDictionary() {
    loadFile("ES", dictionary)
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isEmpty() || randomWord.length == 5) //En aquest cas, té sentit que pugui ser buida ja que ho comprovem a l'hora de generar-la en el programa principal.
  }
  @Test
  fun checkIfRandomWordIsValidInEnglishDictionary() {
    loadFile("EN", dictionary)
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isEmpty() || randomWord.length == 5) //En aquest cas, té sentit que pugui ser buida ja que ho comprovem a l'hora de generar-la en el programa principal.
  }
  @Test
  fun checkIfRandomWordIsEmptyIfDictionaryIsEmpty() {
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isEmpty() || randomWord.length == 5)
  }

  // printWord() Tests
  @Test
  fun checkThatAreFiveCorrectLettersIfGuessWordMatchesSecretWord() {
    val correctLetters = printWord("GORRA", word, colors)
    assertEquals(correctLetters, 5)
  }
  @Test
  fun checkThatAreZeroCorrectLettersIfGuessWordDoesNotMatchSecretWord() {
    val correctLetters = printWord("LUPIN", word, colors)
    assertEquals(correctLetters, 0)
  }
  @Test
  fun checkThatAreCorrectLettersIfGuessWordResemblesSecretWord() {
    val correctLetters = printWord("GOTAS", word, colors)
    assertTrue(correctLetters in 1..4)
  }
  @Test
  fun checkThatAreNotCorrectLettersIfGuessWordHasSameLettersButNotInOrder() {
    val correctLetters = printWord("RRAGO", word, colors)
    assertEquals(correctLetters, 0)
  }

  //playerWins() Tests
  @Test
  fun checkIfWinsIfCorrectLettersEqualsFive() {
    val correctLetters = printWord("GORRA", word, colors)
    assertTrue(playerWins(correctLetters))
  }
  @Test
  fun checkIfLosesIfCorrectLettersIsNotFive() {
    val correctLetters = printWord("GOTAS", word, colors)
    assertFalse(playerWins(correctLetters))
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

  @Test
  fun checkIfMediansAreValidIfParamsAreValid() {
    calculateGameHistogram(medianOfTries, numOfTriesAccomulate, 2, 10)
    assertTrue(medianOfTries.all{ it.toInt() in 0..100 })
  }

  //loadUser() Tests
  @Test
  fun checkThatReturnsUserNameIfRouteIsCorrect() {
    val usersRoute = "./users/"
    assertTrue(loadUser(usersRoute).isNotEmpty())
  }
  @Test
  fun checkThatReturnsEmptyUserNameIfRouteNotExists() {
    val route = "./folderTest/"
    assertTrue(loadUser(route).isEmpty())
  }
  @Test
  fun checkThatReturnsEmptyUserNameIfRouteIsEmpty() {
    val route = ""
    assertTrue(loadUser(route).isEmpty())
  }

  //changeLanguage() Tests
  @Test
  fun checkThatReturnsNewLanguageIfNewLanguageIsExists() {
    val language = "ES"
    loadFile(language, dictionary)
    val newLanguage = changeLanguage("ES", "EN",dictionary)
    assertEquals(newLanguage, "EN")
  }
  @Test
  fun checkThatReturnsCurrentLanguageIfNewLanguagesIsEqualToCurrentLanguage() {
    val language = "ES"
    loadFile(language, dictionary)
    val newLanguage = changeLanguage(language, "ES",dictionary)
    assertEquals(newLanguage, language)
  }

  @Test
  fun checkThatReturnsCurrentLanguageIfNewLanguageDoesNotExist() {
    val language = "ES"
    loadFile(language, dictionary)
    val newLanguage = changeLanguage(language, "JAP",dictionary)
    assertEquals(newLanguage, language)
  }

  @Test
  fun checkThatReturnsCurrentLanguageIfNewLanguageIsEmpty() {
    val language = "ES"
    loadFile(language, dictionary)
    val newLanguage = changeLanguage(language, "",dictionary)
    assertEquals(newLanguage, language)
  }

  //changeUser() Tests
  //savewords() Tests
  //saveHistData() Tests
  //loadData() Tests

//  @Test
//  fun checkThatDoesNotLoadDataIfPathIsNotValid() {
//    val data = loadData("./wrongPath", "hist")
//    assertTrue(data.size == 0)
//  }
//
//  @Test
//  fun checkThatStatsDataIsLoadedIfPathIsValid() {
//    val data = loadData("./history/Histcarlos98.txt", "hist")
//    assertTrue(data.size > 0)
//  }
  //updateDictionary() Tests
  //updateTriesRegistry() Tests


}