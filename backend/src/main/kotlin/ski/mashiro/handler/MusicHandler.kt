package ski.mashiro.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import ski.mashiro.common.GlobalBean
import ski.mashiro.common.GlobalBean.musicWaitingQueue
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author mashirot
 * 2024/2/21 15:33
 */
object MusicHandler {
    private val log = KotlinLogging.logger { this::class.java.name }
    private val customerThread by lazy {
        ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(), { r -> Thread(r, "music-handler-thread") },
            ThreadPoolExecutor.DiscardPolicy()
        )
    }

    fun handleSongRequestComet() = customerThread.execute {
        while (musicWaitingQueue.isNotEmpty()) {
            val info = musicWaitingQueue.poll()
            val (musicWithOutUrl, userInfo) = info
            val (username, uid, isAdmin) = userInfo
            if (GlobalBean.musicList.size >= GlobalBean.songRequestConfig.waitListMaxSize) {
                log.debug { "musicList reached maximum size" }
                continue
            }
            if (Objects.nonNull(GlobalBean.uidCache.getIfPresent(uid))) {
                log.debug { "cometSender: $username is cooling, uid: $uid" }
                continue
            }
            if (Objects.nonNull(GlobalBean.musicCache.getIfPresent(musicWithOutUrl.id))) {
                log.debug { "musicId: ${musicWithOutUrl.id} is cooling, musicName: ${musicWithOutUrl.name}" }
                continue
            }
            if (!isAdmin && GlobalBean.systemConfig.seasonMode && GlobalBean.seasonInProgress.value) {
                log.debug { "the season is in progress" }
                continue
            }
            GlobalBean.musicList.add(username to musicWithOutUrl)
            log.debug { "$username booking success, musicName: ${musicWithOutUrl.name}" }
            if (
                GlobalBean.systemConfig.seasonMode && !GlobalBean.seasonInProgress.value
                && GlobalBean.musicList.size >= GlobalBean.systemConfig.singleSeasonMusicNum
            ) {
                GlobalBean.seasonInProgress.value = true
                log.debug { "seasonInProgress is: ${GlobalBean.seasonInProgress.value}" }
            }
            if (isAdmin) {
                continue
            }
            GlobalBean.uidCache.put(uid, uid)
            GlobalBean.musicCache.put(musicWithOutUrl.id, musicWithOutUrl)
        }
    }
}