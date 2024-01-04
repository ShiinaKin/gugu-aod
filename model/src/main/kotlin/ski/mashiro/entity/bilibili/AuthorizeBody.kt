package ski.mashiro.entity.bilibili

import ski.mashiro.const.AuthorizeBodyConsts
import ski.mashiro.const.AuthorizeBodyConsts.PROTOCOL_VERSION
import ski.mashiro.const.AuthorizeBodyConsts.WEB

/**
 * @author mashirot
 */
data class AuthorizeBody(
    val uid: Long?,
    val roomid: Long,
    val protover: Int,
    val buvid: String?,
    val platform: String,
    val type: Int,
    val key: String?
) {
    constructor(roomId: Long) : this(
        uid = null,
        roomid = roomId,
        buvid = null,
        key = null
    )

    constructor(uid: Long?, roomid: Long, buvid: String?, key: String?) : this(
        uid = uid,
        roomid = roomid,
        protover = PROTOCOL_VERSION,
        buvid = buvid,
        platform = WEB,
        type = AuthorizeBodyConsts.TYPE,
        key = key
    )
}
