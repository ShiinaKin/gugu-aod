package ski.mashiro.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import okio.ByteString
import ski.mashiro.common.GlobalBean.JSON_MAPPER
import ski.mashiro.const.DataCmdConsts
import ski.mashiro.const.DataHeaderConsts
import ski.mashiro.const.DataHeaderConsts.HEADER_LENGTH_INT
import ski.mashiro.entity.bilibili.DataHeader
import ski.mashiro.util.CompressUtils
import java.util.*

/**
 * @author mashirot
 */
object MessageHandler {
    private val log = KotlinLogging.logger { this::class.java.name }

    fun handle(byteString: ByteString) {
        val jsonList = byteString2Json(byteString)
        jsonList.forEach jsonList@{ json ->
            if (json.isBlank()) {
                return@jsonList
            }
            try {
                val dataMap = JSON_MAPPER.readValue(json, HashMap::class.java)
                if (dataMap["cmd"] == DataCmdConsts.COMET) {
                    CometHandler.handle(dataMap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun byteString2Json(byteString: ByteString): List<String> {
        val byteArray = byteString.toByteArray()
        val header = DataHeader.trans2DataHeader(byteArray.copyOfRange(0, HEADER_LENGTH_INT))
        val bodyByteArray = byteArray.copyOfRange(HEADER_LENGTH_INT, byteArray.size)

        if (header.protocolVersion == DataHeaderConsts.UNCOMPRESSED_PROTOCOL) {
            return emptyList()
        }

        if (header.protocolVersion == DataHeaderConsts.HEARTBEAT_PROTOCOL) {
            if (header.dataType == DataHeaderConsts.SERVER_AUTHORIZE) {
                log.debug { "Authorized!!!" }
                return emptyList()
            }
            if (header.dataType == DataHeaderConsts.SERVER_HEARTBEAT) {
                log.debug { "Pong!!!" }
                return emptyList()
            }
        }

        val str = CompressUtils.brotliUncompressed(bodyByteArray)
        return splitJsonString2JsonList(str)
    }

    private fun splitJsonString2JsonList(str: String): MutableList<String> {
        val charArray = str.toCharArray()
        val num = str.length % 10
        var jsonStartIdx = str.indexOf('{', 0)
        val stack = ArrayDeque<Char>(num)
        val jsonStringList = ArrayList<String>(num.shr(1))
        charArray.forEachIndexed { idx, ch ->
            if (ch == '{') {
                stack.push(ch)
                return@forEachIndexed
            }
            if (ch == '}') {
                // 避免多个包的情况下, 后包仍携带DataHeader, 导致解压后乱码, 出现 '}' 的情况
                // 示例: }      
                if (stack.isEmpty()) {
                    return@forEachIndexed
                }
                stack.pop()
            }
            if (stack.isNotEmpty() || idx < jsonStartIdx) {
                return@forEachIndexed
            }
            jsonStringList.add(str.substring(jsonStartIdx, idx + 1))
            jsonStartIdx = str.indexOf('{', idx)
            if (jsonStartIdx == -1) {
                return jsonStringList
            }
        }
        return jsonStringList
    }

}