package ski.mashiro

import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.launch
import ski.mashiro.common.GlobalBean
import ski.mashiro.config.LoggerConfig
import ski.mashiro.file.ConfigFileOperation
import ski.mashiro.service.impl.WebSocketServiceImpl
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * @author mashirot
 */
object BackendMain {
    fun init() {
        LoggerConfig.initLogger()
        ConfigFileOperation.loadConfig()
        GlobalBean.uidCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachUserCoolDown).toJavaDuration()).build()
        GlobalBean.musicCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.parse(GlobalBean.songRequestConfig.eachSongCoolDown).toJavaDuration()).build()
    }

    fun resetMusicList() {
        GlobalBean.musicList.clear()
        if (GlobalBean.systemConfig.seasonResetCoolDown) {
            GlobalBean.uidCache.invalidateAll()
            GlobalBean.musicCache.invalidateAll()
        }
    }

    fun onClose() {
        ConfigFileOperation.saveConfig()
        disconnect2Room()
    }

    private fun disconnect2Room() {
        GlobalBean.IO_SCOPE.launch {
            WebSocketServiceImpl.disconnect2Room()
        }
    }
}

