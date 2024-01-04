package ski.mashiro.factory

import okhttp3.OkHttpClient
import java.time.Duration

/**
 * @author mashirot
 */
object OkHttpClientFactory {

    fun getOkHttpClient() = OkHttpClient.Builder()
        .readTimeout(Duration.parse("PT10S"))
        .writeTimeout(Duration.parse("PT10S"))
        .connectTimeout(Duration.parse("PT10S"))
        .build()

}