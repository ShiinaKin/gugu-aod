package ski.mashiro.component.player

import ski.mashiro.enum.PlayerStatusEnum

/**
 * @author mashirot
 * 2024/1/5 22:42
 */
interface GuGuMediaPlayerListener {
    fun onStatusChanged(status: PlayerStatusEnum)
    fun updateProgress(duration: Long, curTime: Long, progress: Float)
}