package ski.mashiro.entity.music

/**
 * @author mashirot
 * 2024/2/21 15:40
 */
abstract class Music {
    abstract val id: Long
    abstract val name: String
    abstract val singer: String
    abstract val duration: Long
    abstract val durationStr: String
    abstract var coverImgUrl: String?
    abstract var url: String?
}