package ski.mashiro.service

/**
 * @author mashirot
 */
interface WebSocketService {

    fun connect2Room()
    suspend fun reconnect()
    fun disconnect2Room()

}