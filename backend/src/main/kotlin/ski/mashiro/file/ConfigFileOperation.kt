package ski.mashiro.file

import org.apache.commons.io.FileUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.common.GlobalBean.CONFIG_FOLDER
import ski.mashiro.common.GlobalBean.YAML_MAPPER
import ski.mashiro.entity.config.NeteaseCloudMusicConfig
import ski.mashiro.entity.config.RoomConfig
import ski.mashiro.entity.config.SongRequestConfig
import java.io.File
import kotlin.text.Charsets.UTF_8

/**
 * @author mashirot
 * 2024/1/6 20:02
 */
object ConfigFileOperation {
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

    fun saveConfig() {
        val roomConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.roomConfig)
        val songRequestConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.songRequestConfig)
        val neteaseCloudMusicConfigYaml = YAML_MAPPER.writeValueAsString(GlobalBean.neteaseCloudMusicConfig)
        runCatching {
            FileUtils.writeStringToFile(roomConfigFile, roomConfigYaml, UTF_8)
            FileUtils.writeStringToFile(songRequestConfigFile, songRequestConfigYaml, UTF_8)
            FileUtils.writeStringToFile(neteaseCloudMusicConfigFile, neteaseCloudMusicConfigYaml, UTF_8)
        }.getOrElse {
            println("保存文件失败, $it")
        }
    }

    private fun initConfig() {
        if (!CONFIG_FOLDER.exists()) {
            if (!CONFIG_FOLDER.mkdirs()) {
                println("创建config文件夹失败")
                return
            }
        }
        if (!roomConfigFile.exists()) {
            if (!initRoomConfig()) {
                println("创建roomConfig文件失败")
            }
        }
        if (!songRequestConfigFile.exists()) {
            if (!initSongRequestConfig()) {
                println("创建songRequestConfig文件失败")
            }
        }
        if (!neteaseCloudMusicConfigFile.exists()) {
            if (!initNeteaseCloudMusicConfig()) {
                println("创建neteaseCloudMusicConfig文件失败")
            }
        }
    }

    private fun initRoomConfig(): Boolean {
        val defaultRoomConfig = RoomConfig(850221, 0)
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
            "https://cloudmusic.sakurasou.io"
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

    private fun validationFolder(): Boolean {
        if (CONFIG_FOLDER.exists() && CONFIG_FOLDER.isDirectory) {
            if (roomConfigFile.exists() && songRequestConfigFile.exists() && neteaseCloudMusicConfigFile.exists()) {
                return true
            }
        }
        return false
    }
}