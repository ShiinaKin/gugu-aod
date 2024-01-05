package ski.mashiro

import com.github.benmanes.caffeine.cache.Caffeine
import ski.mashiro.common.GlobalBean
import ski.mashiro.entity.config.Config
import ski.mashiro.entity.config.NeteaseCloudMusicConfig
import ski.mashiro.entity.config.SongRequestConfig
import ski.mashiro.service.impl.WebSocketServiceImpl
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * @author mashirot
 */
object BackendMain {
    fun init() {
        GlobalBean.config = GlobalBean.YAML_MAPPER.readValue(
            """
            ua: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        """.trimIndent(),
            Config::class.java
        )

        GlobalBean.neteaseCloudMusicConfig = GlobalBean.YAML_MAPPER.readValue(
            """
            phoneNumber: null
            password: null
            passwordMd5: null
        """.trimIndent(),
            NeteaseCloudMusicConfig::class.java
        )

        GlobalBean.songRequestConfig = GlobalBean.YAML_MAPPER.readValue(
            """
            prefix: "点鸽"
            medalName: "打吊针"
            medalLevel: 1
            eachUserCoolDown: "10s"
            eachSongCoolDown: "10s"
            waitListMaxSize: 100
        """.trimIndent(),
            SongRequestConfig::class.java
        )

        GlobalBean.uidCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachUserCoolDown).toJavaDuration()).build()
        GlobalBean.musicCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachSongCoolDown).toJavaDuration()).build()
    }

    fun connect2Room() = WebSocketServiceImpl.connect2Room()

    fun disconnect2Room() = WebSocketServiceImpl.disconnect2Room()
}

