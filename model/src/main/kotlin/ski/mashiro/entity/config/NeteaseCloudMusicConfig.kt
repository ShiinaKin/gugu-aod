package ski.mashiro.entity.config

/**
 * @author mashirot
 */
data class NeteaseCloudMusicConfig(
    val phoneNumber: Int?,
    val password: String?,
    val passwordMd5: String?,
    var cookie: String,
    val cloudMusicApiUrl: String
)