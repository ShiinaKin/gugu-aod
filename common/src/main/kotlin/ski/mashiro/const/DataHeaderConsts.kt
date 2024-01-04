package ski.mashiro.const

/**
 * @author mashirot
 */
object DataHeaderConsts {
    const val TOTAL_LENGTH_IDX = 0
    const val HEADER_LENGTH_IDX = 4
    const val PROTOCOL_VERSION_IDX = 6
    const val DATA_TYPE_IDX = 8
    const val FIXED_IDX = 12

    const val HEADER_LENGTH: Short = 16
    const val HEADER_LENGTH_INT = 16
    const val UNCOMPRESSED_PROTOCOL: Short = 0
    const val HEARTBEAT_PROTOCOL: Short = 1
    const val COMPRESS_PROTOCOL: Short = 3
    const val FIXED_VAL = 1

    const val CLIENT_HEARTBEAT = 2
    const val SERVER_HEARTBEAT = 3
    const val SERVER_ADVICE = 5
    const val CLIENT_AUTHORIZE = 7
    const val SERVER_AUTHORIZE = 8
}