package ski.mashiro.util

import java.util.concurrent.locks.ReentrantLock

/**
 * @author mashirot
 */
object LockUtils {

    private val lock = ReentrantLock()

    fun tryLock() = lock.tryLock()

    fun releaseLock() = lock.unlock()

}