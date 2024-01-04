package ski.mashiro.util

import okio.ByteString
import okio.ByteString.Companion.toByteString
import ski.mashiro.entity.bilibili.DataHeader
import java.nio.ByteBuffer

/**
 * @author mashirot
 */
object ReqUtils {

    fun getByteString(header: DataHeader, body: ByteArray): ByteString = splice(header, body).toByteString()

    private fun splice(header: DataHeader, body: ByteArray): ByteBuffer =
        ByteBuffer.allocate(header.totalLength)
            .apply {
                header.byteArray.copyInto(array())
                body.copyInto(array(), header.headerLength.toInt())
            }

}