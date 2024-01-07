package ski.mashiro.annotation

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * @author mashirot
 * 2024/1/8 2:02
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Logger {
    companion object {
        val <reified T> T.log: KLogger
            inline get() = KotlinLogging.logger { T::class.java.name }
    }
}
