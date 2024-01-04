package ski.mashiro.entity.config

data class SongRequestConfig(
    var prefix: String,
    var medalName: String?,
    var medalLevel: Int?,
    var eachUserCoolDown: String,
    var eachSongCoolDown: String,
    var waitListMaxSize: Int
)
