package ski.mashiro.entity.config

/**
 * @author mashirot
 * 2024/1/9 17:09
 */
data class SystemConfig(
    var seasonMode: Boolean = false,
    var seasonResetCoolDown: Boolean = false,
    var singleSeasonMusicNum: Int = 8,
    var keywordBlackList: MutableSet<String> = mutableSetOf(),
    var logLevel: String = "info"
)
