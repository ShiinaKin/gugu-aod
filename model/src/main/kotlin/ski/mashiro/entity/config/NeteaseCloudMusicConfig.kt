package ski.mashiro.entity.config

/**
 * @author mashirot
 */
data class NeteaseCloudMusicConfig(
    var phoneNumber: Long?,
    val password: String?,
    var passwordMD5: String?,
    var cookie: String,
    var cloudMusicApiUrl: String,
)