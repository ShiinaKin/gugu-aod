package ski.mashiro.entity.bilibili

/**
 * @author mashirot
 */
data class Comet(
    /**
     * 发送者uid
     */
    val uid: Long,
    /**
     * 发送者用户名
     */
    val username: String,
    /**
     * 弹幕内容
     */
    val content: String,
    /**
     * 勋章名称
     */
    val medalName: String?,
    /**
     * 勋章主播username
     */
    val medalAnchorman: String?,
    /**
     * 勋章主播uid
     */
    val medalAnchormanUID: Long?,
    /**
     * 勋章主播直播间
     */
    val medalRoomId: Long?,
    /**
     * 勋章等级
     */
    val medalLevel: Int?,
    /**
     * 是否主播弹幕
     */
    val isAnchorman: Boolean,
    /**
     * 是否房管弹幕
     */
    val isRoomManager: Boolean,
    val timestamp: Long
) {
    constructor(
        uid: Long,
        username: String,
        content: String,
        isAnchorman: Boolean,
        isRoomManager: Boolean,
        timestamp: Long
    ) : this(
        uid,
        username,
        content,
        null,
        null,
        null,
        null,
        null,
        isAnchorman,
        isRoomManager,
        timestamp
    )
}
