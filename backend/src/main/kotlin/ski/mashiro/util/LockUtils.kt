package ski.mashiro.util

import java.util.concurrent.ConcurrentHashMap

/**
 * @author mashirot
 */
object LockUtils {

    private val concurrentMap = ConcurrentHashMap<String, Byte>(2)

    fun tryLock(key: String): Boolean {
        if (concurrentMap.containsKey(key)) {
            return false
        }
        concurrentMap[key] = 1
        return true
    }

    fun releaseLock(key: String) {
        concurrentMap.remove(key)
    }

}