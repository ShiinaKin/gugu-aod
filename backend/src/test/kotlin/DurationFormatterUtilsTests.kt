import org.apache.commons.lang3.time.DurationFormatUtils
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * @author mashirot
 * 2024/1/5 14:37
 */
class DurationFormatterUtilsTests {
    @Test
    fun testFormatter() {
        val duration =
            DurationFormatUtils.formatDuration(Duration.parse("2h30m").toLong(DurationUnit.MILLISECONDS), "mm:ss")
        println(duration)
    }
}