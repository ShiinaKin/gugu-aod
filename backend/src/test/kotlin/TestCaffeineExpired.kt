import com.github.benmanes.caffeine.cache.Caffeine
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

/**
 * @author mashirot
 * 2024/2/16 20:12
 */
class TestCaffeineExpired {
    @Test
    fun testInvalidateAll() {
        val cache = Caffeine.newBuilder().build<Long, String>()
        cache.put(6657, "test")
        assertTrue(cache.getIfPresent(6657) != null)
        cache.invalidateAll()
        assertTrue(cache.getIfPresent(6657) == null)
    }
}