package ski.mashiro.factory

import okhttp3.Request
import ski.mashiro.common.GlobalBean.config
import ski.mashiro.common.GlobalBean.neteaseCloudMusicConfig

/**
 * @author mashirot
 */
object RequestBuilderFactory {

    fun getReqBuilderWithUA() = Request.Builder().addHeader("User-Agent", config.ua)

    fun getReqBuilderWithBiliCookieAndUA() =
        Request.Builder()
            .addHeader("User-Agent", config.ua)
            .apply {
                if (config.cookie.isNotBlank()) addHeader("Cookie", config.cookie)
            }

    fun getReqBuilderWithNeteaseCloudMusicCookieAndUA() =
        Request.Builder()
            .addHeader("User-Agent", config.ua)
            .apply {
                if (neteaseCloudMusicConfig.cookie.isNotBlank()) addHeader(
                    "Cookie",
                    neteaseCloudMusicConfig.cookie
                )
            }

}