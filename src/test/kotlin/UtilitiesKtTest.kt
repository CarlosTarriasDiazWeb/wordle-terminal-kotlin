import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.ArrayList

internal class UtilitiesKtTest {

  private val dictionary = ArrayList<String>()

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

  @Test
  fun checkIfRandomWordIsValid() {
    loadFile("test1.txt", dictionary)
    val randomWord = getRandomWord(dictionary)
    assertTrue(randomWord.isNotEmpty() && randomWord.length == 5, "The obtained word is valid")
  }

}