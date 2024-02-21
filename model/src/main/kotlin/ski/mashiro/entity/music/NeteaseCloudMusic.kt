package ski.mashiro.entity.music

import org.apache.commons.lang3.time.DurationFormatUtils

/**
 * @author mashirot
 */
data class NeteaseCloudMusic(
    override val id: Long,
    override val name: String,
    override val singer: String,
    override val duration: Long,
    override val durationStr: String,
    override var coverImgUrl: String?,
    override var url: String?
) : Music() {
    constructor(
        id: Long,
        name: String,
        singer: String,
        duration: Long,
        coverImgUrl: String?,
        url: String?
    ) : this(
        id,
        name,
        singer,
        duration,
        DurationFormatUtils.formatDuration(duration, "mm:ss"),
        coverImgUrl,
        url,
    )
}
