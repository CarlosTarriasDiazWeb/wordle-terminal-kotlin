import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PrintUtilitiesKtTest {

  private val word = "GORRA"
  private val colors = mutableMapOf(
    "reset" to "\u001B[0m",
    "green" to "\u001B[32m",
    "yellow" to "\u001B[33m",
    "white" to "\u001b[47m"
  )

  private val medianOfTries = doubleArrayOf(20.0,10.0,30.0,40.0,0.0,0.0)
  private val numOfTriesAccomulate = intArrayOf(0,2,3,5,1,1)

  //printLoseMessage() Tests
  @Test
  fun checkIfMessageIsInvalidIfWordIsEmpty() {
    assertFalse(printLoseMessage(""))
  }

  @Test
  fun checkIfMessageIsValidIfWordIsCorrect() {
    assertTrue(printLoseMessage(word))
  }

  //printChar() Tests
  @Test
  fun checkIfCharIsPrintedIfAllParametersAreValid() {
    assertTrue(printChar(colors["green"], colors["reset"], word[0]))
  }

  @Test
  fun checkIfCharIsNotPrintedIfColorsAreInvalidOne() {
    assertFalse(printChar("", colors["reset"], word[0]))
  }

  @Test
  fun checkIfCharIsNotPrintedIfColorsAreInvalidTwo() {
    assertFalse(printChar(null, null, word[0]))
  }

  //showGameStatistics() Tests
  @Test
  fun checkIfStatisticsAreNotShownIfNoWordHasBeenGuessed() {
    assertFalse(showGameStatistics(10,0,0.0,107,0,0))
  }

  @Test
  fun checkIfStatisticsAreShownIfAtLeastOneWordHasBeenGuessed() {
    assertTrue(showGameStatistics(10,4,2.9,101,2,1))
  }

  //showGameHistogram() Tests
  @Test
  fun checkIfHistogramIsNotShownIfNoWordHasBeenGuessed() {
    assertFalse(showGameHistogram(medianOfTries, numOfTriesAccomulate, colors, 0))
  }

  @Test
  fun checkIfHistogramIsShownIfAtLeastOneWordHasBeenGuessed() {
    assertTrue(showGameHistogram(medianOfTries, numOfTriesAccomulate, colors, 1))
  }
 }
