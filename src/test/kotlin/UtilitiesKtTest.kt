import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

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

  //calculateGameHistogram() Tests
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
  fun checkThatReturnsNewLanguageIfNewLanguageExists() {
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
  @Test
  fun checkThatReturnsCurrentUserIfNewUserIsEqualToCurrentUser() {
    val user = "carlos98"
    val newUser = "carlos98"
    val resultUser = changeUser("./users/users.txt", user, newUser)
    assertEquals(resultUser, user)
  }
  @Test
  fun checkThatReturnsCurrentUserIfNewUserDoesNotExists() {
    val user = "carlos98"
    val newUser = "nonuser"
    val resultUser = changeUser("./users/users.txt", user, newUser)
    assertEquals(resultUser, user)
  }
  @Test
  fun checkThatReturnsCurrentUserIfRouteDoesNotExists() {
    val user = "carlos98"
    val newUser = "red20"
    val resultUser = changeUser("./users/nonexistent.txt", user, newUser)
    assertEquals(resultUser, user)
  }
  @Test
  fun checkThatReturnsCurrentUserIfNewuserIsEmpty() {
    val user = "carlos98"
    val newUser = ""
    val resultUser = changeUser("./users/users.txt", user, newUser)
    assertEquals(resultUser, user)
  }

  //saveWords() Tests
  @Test
  fun checkWordsHistHasContentWhenWordProvided() {
    val wordsHistoryTest = mutableListOf(mutableListOf("ACERA","0","2"), mutableListOf("ALBOR","1","4") )
    saveWords("./savedata/EStest.txt", wordsHistoryTest, "ATLAS", 4, 3)
    assertTrue(File("./savedata/EStest.txt").length() > 0)
  }

  //saveHistData() Tests
  @Test
  fun checkSavedDataHasContentWhenDataIsProvided() {
    val testUserStats: Array<Any> = arrayOf(2,2,2,3,0.12)
    val testNumOfTriesAccomulate = intArrayOf(1,1,0,0,0,0)
    saveHistData("./history/Histtest.txt", testUserStats, testNumOfTriesAccomulate, 216)
    assertTrue(File("./history/Histtest.txt").length() > 0)
  }

  //loadData() Tests
  @Test
  fun checkThatDefaultStatsDataIsLoadedIfHistFileNotExists() {
    val data = loadData("./history/Histuser.txt", "hist")
    assertTrue(data.size > 0)
  }
  @Test
  fun checkThatWordsHistDataIsEmptyIfPathNotExists() {
    val data = loadData("./history/ESuser98.txt", "save")
    assertTrue(data.size == 0)
  }

  //updateDictionary() Tests
  @Test
  fun checkIfWordsHistoryHasDataDictionaryHasSomePlayedWords() {
    loadFile("ES", dictionary)
    val testWordsData = mutableListOf(mutableListOf("BALDE","8","1"), mutableListOf("MANDA", "67", "1"))
    updateDictionary(testWordsData, dictionary)
    assertTrue(dictionary.any { it == "" })
  }
  @Test
  fun checkIfWordsHistoryHasNoDataDictionaryHasNotPlayedWords() {
    loadFile("ES", dictionary)
    val testWordsData = mutableListOf<MutableList<String>>()
    updateDictionary(testWordsData, dictionary)
    assertTrue(dictionary.all { it != "" })
  }
  @Test
  fun checkIfWordsHistoryHasDataDictionaryHasThatWordEmpty() {
    loadFile("ES", dictionary)
    val testWordsData = mutableListOf(mutableListOf("BALDE","8","1"))
    updateDictionary(testWordsData, dictionary)
    assertTrue(dictionary[8] == "")
  }
  @Test
  fun checkIfWordHistoryHasAllWordsDictionaryHasAllWordsEmpty() {
    val testDictionary = ArrayList<String>()
    testDictionary.add("Acera")
    testDictionary.add("Albor")
    val testWordsData = mutableListOf(mutableListOf("ACERA","0","2"), mutableListOf("ALBOR","1","4") )
    updateDictionary(testWordsData, testDictionary)
    assertTrue(testDictionary.all { it == ""})
  }

  //updateTriesRegistry() Tests
  @Test
  fun checkTriesAccomulateHasUpdatedContentOnceDataLoaded() {
    val testData = mutableListOf("2","3","1","1","0","0")
    val testTries = intArrayOf(1,3,1,1,0,0)
    var correctDataCounter = 0
    updateTriesRegistry(testData, testTries)
    for (i in testTries.indices) {
      if (testTries[i].toString() == testData[i]) correctDataCounter++
    }
    assertEquals(correctDataCounter, 6)
  }
  @Test
  fun checkTriesAccomulateIsTheSameIfDataLoadedIsEmpty() {
    val testData = mutableListOf<String>()
    val testTries = intArrayOf(1,3,1,1,0,0)
    val testTriesAux = testTries.clone()
    updateTriesRegistry(testData, testTries)
    assertTrue(testTries.contentEquals(testTriesAux))
  }

}