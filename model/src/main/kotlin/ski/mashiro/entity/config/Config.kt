package ski.mashiro.entity.config

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author mashirot
 */
data class Config(
    var roomId: Long,
    @JsonIgnore
    var anchormanUID: Long?,
    val uid: Long,
    val buvId: String = "850221AA-6657-6657-6657-91043281097552522infoc",
    @JsonIgnore
    var key: String?,
    val cookie: String,
    val ua: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
)