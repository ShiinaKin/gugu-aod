package ski.mashiro.common

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.benmanes.caffeine.cache.Cache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.WebSocket
import ski.mashiro.entity.config.Config
import ski.mashiro.entity.config.NeteaseCloudMusicConfig
import ski.mashiro.entity.config.SongRequestConfig
import ski.mashiro.entity.music.NeteaseCloudMusic

/**
 * @author mashirot
 */
object GlobalBean {
    val JSON_MAPPER = JsonMapper().registerKotlinModule()
    val YAML_MAPPER = YAMLMapper().registerKotlinModule()
    val MAIN_SCOPE = CoroutineScope(Dispatchers.Main)
    lateinit var config: Config
    lateinit var neteaseCloudMusicConfig: NeteaseCloudMusicConfig
    lateinit var songRequestConfig: SongRequestConfig
    lateinit var uidCache: Cache<Long, Long>
    lateinit var musicCache: Cache<Long, NeteaseCloudMusic>
    lateinit var musicList: MutableList<NeteaseCloudMusic>
    var webSocket: WebSocket? = null
}