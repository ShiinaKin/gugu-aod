package ski.mashiro.component.player

import org.junit.jupiter.api.Test
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent
import java.time.LocalDateTime

/**
 * @author mashirot
 * 2024/1/6 0:10
 */
class GuGuMediaPlayerTests {
    @Test
    fun testPlay() {
        val mediaPlayer = AudioPlayerComponent().mediaPlayer().apply {
            events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
                override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
                    super.mediaChanged(mediaPlayer, media)
                    println("mediaChanged")
                }

                override fun mediaPlayerReady(mediaPlayer: MediaPlayer) {
                    super.mediaPlayerReady(mediaPlayer)
                    println(LocalDateTime.now().toString() + "mediaPlayerReady")
                }

                override fun opening(mediaPlayer: MediaPlayer?) {
                    super.opening(mediaPlayer)
                    println(LocalDateTime.now().toString() + " opining")
                }

                override fun playing(mediaPlayer: MediaPlayer?) {
                    super.playing(mediaPlayer)
                    println(LocalDateTime.now().toString() + " playing")
                }

                override fun finished(mediaPlayer: MediaPlayer?) {
                    super.finished(mediaPlayer)
                    println(LocalDateTime.now().toString() + " finish")
                }

                override fun stopped(mediaPlayer: MediaPlayer?) {
                    super.stopped(mediaPlayer)
                    println(LocalDateTime.now().toString() + " stopped")
                }

                override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {
                    super.timeChanged(mediaPlayer, newTime)
                    println("timeChanged, newTime: $newTime")
                }

                override fun error(mediaPlayer: MediaPlayer?) {
                    super.error(mediaPlayer)
                    println("mediaPlayer error")
                }
            })
        }
        // flac url 无法播放
        mediaPlayer.media().play("C:\\Users\\MashiroT\\Desktop\\test.flac")
        // mediaPlayer.media().play("http://m8.music.126.net/20240106161239/24bb082a34197a392382d6064fc121cd/ymusic/065d/545d/545c/17df61601ec731958239c6dd877832e0.flac")
        mediaPlayer.audio().setVolume(25)
        Thread.currentThread().join()
    }
}