package ski.mashiro.enum

/**
 * @author mashirot
 * 2024/1/5 21:58
 */
enum class PlayerStatusEnum(
    val content: String,
    val description: String
) {
    IDLE("IDLE", "空闲"),
    PLAYING("Playing", "播放"),
    PAUSED("Pause", "暂停"),
    FINISHED("Finish", "结束"),
    STOPPED("Stop", "停止"),
}