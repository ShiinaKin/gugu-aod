package ski.mashiro.file

import androidx.compose.runtime.toMutableStateList
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.io.FileUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.common.GlobalBean.CONFIG_FOLDER
import ski.mashiro.common.GlobalBean.YAML_MAPPER
import ski.mashiro.entity.config.NeteaseCloudMusicConfig
import ski.mashiro.entity.config.RoomConfig
import ski.mashiro.entity.config.SongRequestConfig
import ski.mashiro.entity.config.SystemConfig
import java.io.File
import kotlin.text.Charsets.UTF_8

/**
 * @author mashirot
 * 2024/1/6 20:02
 */
object ConfigFileOperation {
    private val log = KotlinLogging.logger { this::class.java.name }

    val systemConfigFile = File(CONFIG_FOLDER, "systemConfig.yml")
    private val roomConfigFile = File(CONFIG_FOLDER, "roomConfig.yml")
    private val songRequestConfigFile = File(CONFIG_FOLDER, "songRequestConfig.yml")
    private val neteaseCloudMusicConfigFile = File(CONFIG_FOLDER, "neteaseCloudMusicConfig.yml")

    fun loadConfig() {
        if (!validationFolder()) {
            initConfig()
            return
        }
        val roomConfigYaml = FileUtils.readFileToString(roomConfigFile, UTF_8)
        GlobalBean.roomConfig = YAML_MAPPER.readValue(roomConfigYaml, RoomConfig::class.java)
        val songRequestConfigYaml = FileUtils.readFileToString(songRequestConfigFile, UTF_8)
        GlobalBean.songRequestConfig = YAML_MAPPER.readValue(songRequestConfigYaml, SongRequestConfig::class.java)
        val neteaseCloudMusicConfigYaml = FileUtils.readFileToString(neteaseCloudMusicConfigFile, UTF_8)
        GlobalBean.neteaseCloudMusicConfig =
            YAML_MAPPER.readValue(neteaseCloudMusicConfigYaml, NeteaseCloudMusicConfig::class.java)
    }

    fun loadSystemConfig() {
        val systemConfigYaml = FileUtils.readFileToString(systemConfigFile, UTF_8)
        GlobalBean.systemConfig = YAML_MAPPER.readValue(systemConfigYaml, SystemConfig::class.java)
        GlobalBean.keywordBlackList = GlobalBean.systemConfig.keywordBlackList.toMutableStateList()
        GlobalBean.seasonMode = GlobalBean.systemConfig.seasonMode
    }

    fun saveConfig(): Boolean =
        if (saveRoomConfig() && saveSongRequestConfig() && saveNeteaseCloudMusicConfig() && saveSystemConfig()) {
            log.info { "保存配置成功" }
            true
        } else {
            log.warn { "保存配置失败" }
            false
        }

    fun saveRoomConfig(): Boolean =
        runCatching {
            val roomConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.roomConfig)
            FileUtils.writeStringToFile(roomConfigFile, roomConfigYaml, UTF_8)
            true
        }.getOrElse {
            log.warn { "保存roomConfig失败, $it" }
            false
        }


    fun saveSongRequestConfig(): Boolean =
        runCatching {
            val songRequestConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.songRequestConfig)
            FileUtils.writeStringToFile(songRequestConfigFile, songRequestConfigYaml, UTF_8)
            true
        }.getOrElse {
            log.warn { "保存songRequestConfig失败, $it" }
            false
        }

    fun saveNeteaseCloudMusicConfig(): Boolean =
        runCatching {
            val neteaseCloudMusicConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.neteaseCloudMusicConfig)
            FileUtils.writeStringToFile(neteaseCloudMusicConfigFile, neteaseCloudMusicConfigYaml, UTF_8)
            true
        }.getOrElse {
            log.warn { "保存neteaseCloudMusicConfig失败, $it" }
            false
        }

    private fun saveSystemConfig(): Boolean =
        runCatching {
            val systemConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.systemConfig)
            FileUtils.writeStringToFile(systemConfigFile, systemConfigYaml, UTF_8)
            true
        }.getOrElse {
            log.warn { "保存systemConfig失败, $it" }
            false
        }

    private fun initConfig() {
        if (!CONFIG_FOLDER.exists()) {
            if (!CONFIG_FOLDER.mkdirs()) {
                log.warn { "创建config文件夹失败" }
                return
            }
        }
        if (!roomConfigFile.exists()) {
            if (!initRoomConfig()) {
                log.warn { "创建roomConfig文件失败" }
            }
        }
        if (!songRequestConfigFile.exists()) {
            if (!initSongRequestConfig()) {
                log.warn { "创建songRequestConfig文件失败" }
            }
        }
        if (!neteaseCloudMusicConfigFile.exists()) {
            if (!initNeteaseCloudMusicConfig()) {
                log.warn { "创建neteaseCloudMusicConfig文件失败" }
            }
        }
        if (!systemConfigFile.exists()) {
            if (!initSystemConfig()) {
                log.warn { "创建systemConfig文件失败" }
            }
        }
    }

    private fun initRoomConfig(): Boolean {
        val defaultRoomConfig = RoomConfig(0, 0, "")
        GlobalBean.roomConfig = defaultRoomConfig
        if (!roomConfigFile.createNewFile()) {
            return false
        }
        FileUtils.writeStringToFile(
            roomConfigFile,
            YAML_MAPPER.writeValueAsString(defaultRoomConfig),
            UTF_8
        )
        return true
    }

    private fun initSongRequestConfig(): Boolean {
        val defaultSongRequestConfig = SongRequestConfig("点鸽", null, null, "10s", "10s", 100)
        GlobalBean.songRequestConfig = defaultSongRequestConfig
        if (!songRequestConfigFile.createNewFile()) {
            return false
        }
        FileUtils.writeStringToFile(
            songRequestConfigFile,
            YAML_MAPPER.writeValueAsString(defaultSongRequestConfig),
            UTF_8
        )
        return true
    }

    private fun initNeteaseCloudMusicConfig(): Boolean {
        val defaultNeteaseCloudMusicConfig = NeteaseCloudMusicConfig(
            null,
            null,
            null,
            "",
            "https://请.自行搭建.Ovo"
        )
        GlobalBean.neteaseCloudMusicConfig = defaultNeteaseCloudMusicConfig
        if (!neteaseCloudMusicConfigFile.createNewFile()) {
            return false
        }
        FileUtils.writeStringToFile(
            neteaseCloudMusicConfigFile,
            YAML_MAPPER.writeValueAsString(defaultNeteaseCloudMusicConfig),
            UTF_8
        )
        return true
    }

    private fun initSystemConfig(): Boolean {
        if (!systemConfigFile.createNewFile()) {
            return false
        }
        FileUtils.writeStringToFile(
            systemConfigFile,
            YAML_MAPPER.writeValueAsString(GlobalBean.systemConfig),
            UTF_8
        )
        return true
    }

    private fun validationFolder(): Boolean {
        if (CONFIG_FOLDER.exists() && CONFIG_FOLDER.isDirectory) {
            if (roomConfigFile.exists()
                && songRequestConfigFile.exists()
                && neteaseCloudMusicConfigFile.exists()
                && systemConfigFile.exists()
            ) {
                return true
            }
        }
        return false
    }
}