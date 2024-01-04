package ski.mashiro.exception

class WebSocketException(
    override val message: String
) : RuntimeException(message)