package ski.mashiro.factory

import okhttp3.Request
import ski.mashiro.common.GlobalBean.neteaseCloudMusicConfig
import ski.mashiro.common.GlobalBean.roomConfig

/**
 * @author mashirot
 */
object RequestBuilderFactory {

    fun getReqBuilderWithUA() = Request.Builder().addHeader("User-Agent", roomConfig.ua)

    fun getReqBuilderWithBiliCookieAndUA() =
        Request.Builder()
            .addHeader("User-Agent", roomConfig.ua)
            .apply {
                if (roomConfig.cookie.isNotBlank()) addHeader("Cookie", roomConfig.cookie)
            }

    fun getReqBuilderWithNeteaseCloudMusicCookieAndUA() =
        Request.Builder()
            .addHeader("User-Agent", roomConfig.ua)
            .apply {
                if (neteaseCloudMusicConfig.cookie.isNotBlank()) addHeader(
                    "Cookie",
                    neteaseCloudMusicConfig.cookie
                )
            }

}