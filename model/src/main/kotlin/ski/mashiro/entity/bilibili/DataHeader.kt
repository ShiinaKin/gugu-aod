package ski.mashiro.entity.bilibili

import ski.mashiro.const.DataHeaderConsts.FIXED_IDX
import ski.mashiro.const.DataHeaderConsts.FIXED_VAL
import ski.mashiro.const.DataHeaderConsts.HEADER_LENGTH
import ski.mashiro.const.DataHeaderConsts.HEADER_LENGTH_IDX
import ski.mashiro.const.DataHeaderConsts.PROTOCOL_VERSION_IDX
import ski.mashiro.const.DataHeaderConsts.TOTAL_LENGTH_IDX
import ski.mashiro.const.DataHeaderConsts.DATA_TYPE_IDX
import java.nio.ByteBuffer

/**
 * @author mashirot
 */
data class DataHeader(
    val totalLength: Int,
    val headerLength: Short,
    val protocolVersion: Short,
    val dataType: Int,
    val fixed: Int
) {
    lateinit var byteArray: ByteArray

    constructor(
        totalLength: Int,
        protocolVersion: Short,
        dataType: Int
    ) : this(
        totalLength, HEADER_LENGTH, protocolVersion, dataType, FIXED_VAL
    ) {
        byteArray = trans2ByteArray(totalLength, headerLength, protocolVersion, dataType, fixed)
    }

    companion object {
        fun trans2DataHeader(headerArray: ByteArray): DataHeader {
            val buffer = ByteBuffer.wrap(headerArray)
            return DataHeader(
                buffer.getInt(TOTAL_LENGTH_IDX),
                buffer.getShort(HEADER_LENGTH_IDX),
                buffer.getShort(PROTOCOL_VERSION_IDX),
                buffer.getInt(DATA_TYPE_IDX),
                buffer.getInt(FIXED_IDX)
            )
        }
    }

    private fun trans2ByteArray(
        totalLength: Int,
        headerLength: Short,
        protocolVersion: Short,
        dataType: Int,
        fixed: Int
    ): ByteArray = ByteBuffer.allocate(headerLength.toInt())
        .apply {
            putInt(TOTAL_LENGTH_IDX, totalLength)
            putShort(HEADER_LENGTH_IDX, headerLength)
            putShort(PROTOCOL_VERSION_IDX, protocolVersion)
            putInt(DATA_TYPE_IDX, dataType)
            putInt(FIXED_IDX, fixed)
        }.array()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataHeader

        return byteArray.contentEquals(other.byteArray)
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }
}