package ski.mashiro.component.player

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ski.mashiro.common.GlobalBean
import ski.mashiro.const.LockConsts
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.enum.PlayerStatusEnum
import ski.mashiro.util.LockUtils
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent

/**
 * @author mashirot
 * 2024/1/5 21:33
 */
class GuGuMediaPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var music: NeteaseCloudMusic? = null
    private var duration = 0L
    private var curTime = 0L
    private var playerStatus = PlayerStatusEnum.IDLE
    private var job: Job? = null
    lateinit var listener: GuGuMediaPlayerListener

    init {
        mediaPlayer = AudioPlayerComponent().mediaPlayer().apply {
            events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
                override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                    super.mediaPlayerReady(mediaPlayer)
                    updateProgress()
                    setStatus(PlayerStatusEnum.PLAYING)
                }

                override fun finished(mediaPlayer: MediaPlayer?) {
                    super.finished(mediaPlayer)
                    setStatus(PlayerStatusEnum.FINISHED)
                }

                override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {
                    super.timeChanged(mediaPlayer, newTime)
                    curTime = newTime
                    updateProgress()
                }

                override fun error(mediaPlayer: MediaPlayer?) {
                    super.error(mediaPlayer)
                    println("mediaPlayer error")
                }
            })
        }
    }

    @Synchronized
    fun play() {
        if (playerStatus == PlayerStatusEnum.PLAYING) {
            mediaPlayer?.controls()?.pause()
        }
        music?.let {
            duration = it.duration
            curTime = 0L
        }
        job?.cancel()
        job = GlobalBean.IO_SCOPE.launch {
            try {
                mediaPlayer?.media()?.play(music?.url)
            } finally {
                LockUtils.releaseLock(LockConsts.PLAYING_LOCK)
            }
        }
    }

    fun start() {
        if (playerStatus != PlayerStatusEnum.PAUSED) {
            return
        }
        mediaPlayer?.controls()?.start()
        setStatus(PlayerStatusEnum.PLAYING)
    }

    fun pause() {
        if (playerStatus != PlayerStatusEnum.PLAYING) {
            return
        }
        mediaPlayer?.controls()?.pause()
        setStatus(PlayerStatusEnum.PAUSED)
    }

    fun stop() {
        mediaPlayer?.controls()?.stop()
        duration = 0L
        curTime = 0L
        setStatus(PlayerStatusEnum.STOPPED)
        setStatus(PlayerStatusEnum.IDLE)
    }

    fun setVolume(volume: Int) {
        mediaPlayer?.audio()?.setVolume(volume)
    }

    fun mute(): Boolean = mediaPlayer?.audio()?.mute()!!


    fun jump2Position(position: Float) {
        mediaPlayer?.controls()?.setPosition(position)
    }

    fun setMusic(music: NeteaseCloudMusic) {
        this.music = music
    }

    fun setStatus(status: PlayerStatusEnum) {
        playerStatus = status
        listener.onStatusChanged(status)
    }

    fun updateProgress() {
        listener.updateProgress(duration, curTime, mediaPlayer?.status()?.position()!!)
    }
}