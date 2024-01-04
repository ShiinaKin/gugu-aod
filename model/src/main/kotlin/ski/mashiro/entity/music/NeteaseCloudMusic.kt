package ski.mashiro.entity.music

import org.apache.commons.lang3.time.DurationFormatUtils

/**
 * @author mashirot
 */
data class NeteaseCloudMusic(
    val id: Long,
    val name: String,
    val singer: String,
    val duration: String,
    var url: String?
) {
    constructor(
        id: Long,
        name: String,
        singer: String,
        duration: Long,
        url: String?
    ): this(
        id,
        name,
        singer,
        DurationFormatUtils.formatDurationHMS(duration),
        url,
    )
}
