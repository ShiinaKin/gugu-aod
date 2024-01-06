package ski.mashiro.component.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import org.apache.commons.lang3.time.DurationFormatUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.const.LockConsts
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.enum.PlayerStatusEnum
import ski.mashiro.service.impl.NeteaseCloudMusicServiceImpl
import ski.mashiro.util.LockUtils

/**
 * @author mashirot
 * 2024/1/5 22:30
 */
object GuGuMediaPlayerController : GuGuMediaPlayerListener {
    private const val INIT_TIME_STR = "00:00"
    var curMusicInfo by mutableStateOf<Pair<String, NeteaseCloudMusic>?>(null)
    var durationStr by mutableStateOf(INIT_TIME_STR)
    var curTimeStr by mutableStateOf(INIT_TIME_STR)
    var progress by mutableStateOf(0F)
    private val mediaPlayer: GuGuMediaPlayer by lazy {
        GuGuMediaPlayer().apply { listener = this@GuGuMediaPlayerController }
    }
    private var duration = 0L
    private var hasMusic by mutableStateOf(false)
    private var paused by mutableStateOf(false)
    private var playerStatus = PlayerStatusEnum.IDLE

    fun start() {
        if (playerStatus == PlayerStatusEnum.PAUSED) {
            mediaPlayer.start()
        }
    }

    fun pause() {
        if (playerStatus == PlayerStatusEnum.PLAYING) {
            mediaPlayer.pause()
        }
    }

    fun setVolume(volume: Int) = mediaPlayer.setVolume(volume)

    fun mute() = mediaPlayer.mute()

    fun jump2Position(position: Float) {
        if (duration == 0L) {
            return
        }
        mediaPlayer.jump2Position(position)
    }

    fun hasMusic() = hasMusic

    fun isPaused() = paused

    fun autoPlayNext() {
        if (playerStatus != PlayerStatusEnum.IDLE
            && playerStatus != PlayerStatusEnum.FINISHED
            && playerStatus != PlayerStatusEnum.STOPPED
        ) {
            return
        }
        playNext()
    }

    fun playNext() {
        if (LockUtils.tryLock(LockConsts.PLAYING_LOCK)) {
            mediaPlayer.stop()
            if (GlobalBean.musicList.isNotEmpty()) {
                GlobalBean.IO_SCOPE.launch {
                    curMusicInfo = GlobalBean.musicList.removeFirst()
                    val music = NeteaseCloudMusicServiceImpl.getMusicById(curMusicInfo!!.second)
                    mediaPlayer.setMusic(music)
                    mediaPlayer.play()
                }
            }
        }
    }

    override fun onStatusChanged(status: PlayerStatusEnum) {
        playerStatus = status
        hasMusic = playerStatus == PlayerStatusEnum.PLAYING || playerStatus == PlayerStatusEnum.PAUSED
        paused = playerStatus == PlayerStatusEnum.PAUSED
        when (status) {
            PlayerStatusEnum.FINISHED -> {
                autoPlayNext()
            }

            PlayerStatusEnum.STOPPED -> {
                durationStr = INIT_TIME_STR
                curTimeStr = INIT_TIME_STR
                duration = 0L
                progress = 0F
            }

            else -> {}
        }
    }

    override fun updateProgress(duration: Long, curTime: Long, progress: Float) {
        durationStr = DurationFormatUtils.formatDuration(duration, "mm:ss")
        curTimeStr = DurationFormatUtils.formatDuration(curTime, "mm:ss")
        this.duration = duration
        this.progress = progress
    }
}