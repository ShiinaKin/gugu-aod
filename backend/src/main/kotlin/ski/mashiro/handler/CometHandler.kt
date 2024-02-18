package ski.mashiro.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean
import ski.mashiro.common.GlobalBean.IO_SCOPE
import ski.mashiro.common.GlobalBean.roomConfig
import ski.mashiro.common.GlobalBean.songRequestConfig
import ski.mashiro.const.CometConsts.INFO_CONTENT_IDX
import ski.mashiro.const.CometConsts.INFO_MEDAL_IDX
import ski.mashiro.const.CometConsts.INFO_STATUS_IDX
import ski.mashiro.const.CometConsts.INFO_USER_IDX
import ski.mashiro.const.CometConsts.MEDAL_ANCHORMAN_IDX
import ski.mashiro.const.CometConsts.MEDAL_ANCHORMAN_UID_IDX
import ski.mashiro.const.CometConsts.MEDAL_LEVEL_IDX
import ski.mashiro.const.CometConsts.MEDAL_NAME_IDX
import ski.mashiro.const.CometConsts.MEDAL_ROOM_ID_IDX
import ski.mashiro.const.CometConsts.TIMESTAMP_IDX
import ski.mashiro.const.CometConsts.USER_IS_ROOM_MANAGER_IDX
import ski.mashiro.const.CometConsts.USER_UID_IDX
import ski.mashiro.const.CometConsts.USER_USERNAME_IDX
import ski.mashiro.entity.bilibili.Comet
import ski.mashiro.service.impl.NeteaseCloudMusicServiceImpl
import java.util.*

/**
 * @author mashirot
 */
object CometHandler {
    private val log = KotlinLogging.logger { this::class.java.name }

    fun handle(map: HashMap<*, *>) {
        val comet = trans2Comet(map["info"] as List<*>)
        log.trace { "receiveComet: $comet" }
        parseComet(comet)
    }

    private fun parseComet(comet: Comet) {
        if (!comet.content.startsWith(songRequestConfig.prefix) || comet.content[2] != ' ' || comet.content.length < 4) {
            return
        }
        log.debug { "songReqComet: $comet" }
        IO_SCOPE.launch {
            songRequest(comet)
        }
    }

    private suspend fun songRequest(comet: Comet) {
        if (GlobalBean.musicList.size >= songRequestConfig.waitListMaxSize) {
            log.debug { "musicList reached maximum size" }
            return
        }
        if (GlobalBean.systemConfig.seasonMode && GlobalBean.musicList.isEmpty()) {
            GlobalBean.seasonInProgress.value = false
            log.debug { "seasonInProgress is: ${GlobalBean.seasonInProgress.value}" }
        }
        val isAdmin = comet.isAnchorman || comet.isRoomManager
        if (!isAdmin && GlobalBean.systemConfig.seasonMode && GlobalBean.seasonInProgress.value) {
            log.debug { "the season is in progress" }
            return
        }
        if (!isAdmin) {
            if (Objects.nonNull(songRequestConfig.medalName)) {
                if (Objects.isNull(comet.medalName) || comet.medalName != songRequestConfig.medalName) {
                    log.debug { "cometSender: ${comet.username} wearing the wrong medal" }
                    return
                }
                if (Objects.isNull(comet.medalLevel) || comet.medalLevel!! < songRequestConfig.medalLevel!!) {
                    log.debug { "cometSender: ${comet.username} not reaching the required level for the medal" }
                    return
                }
            }
        }
        if (Objects.nonNull(GlobalBean.uidCache.getIfPresent(comet.uid))) {
            log.debug { "cometSender: ${comet.username} is cooling" }
            return
        }
        val keyword = comet.content.substring(3)
        if (StringUtils.isBlank(keyword)) {
            return
        }
        runCatching {
            val musicWithOutUrl = NeteaseCloudMusicServiceImpl.getMusicByKeyword(keyword)
            val matched = withContext(Dispatchers.IO) {
                !isAdmin && GlobalBean.keywordBlackList.any { musicWithOutUrl.name.contains(it) }
            }
            if (matched) {
                log.debug { "musicId: ${musicWithOutUrl.id} is in black list, musicName: ${musicWithOutUrl.name}" }
                return
            }
            if (Objects.nonNull(GlobalBean.musicCache.getIfPresent(musicWithOutUrl.id))) {
                log.debug { "musicId: ${musicWithOutUrl.id} is cooling, musicName: ${musicWithOutUrl.name}" }
                return
            }
            if (GlobalBean.systemConfig.seasonMode && GlobalBean.seasonInProgress.value) {
                log.debug { "double check of seasonInProgress is true" }
                return
            }
            GlobalBean.musicList.add(comet.username to musicWithOutUrl)
            log.debug { "${comet.username} booking success, musicName: ${musicWithOutUrl.name}" }
            if (isAdmin) {
                return
            }
            GlobalBean.uidCache.put(comet.uid, comet.uid)
            GlobalBean.musicCache.put(musicWithOutUrl.id, musicWithOutUrl)
            if (
                GlobalBean.systemConfig.seasonMode && !GlobalBean.seasonInProgress.value
                && GlobalBean.musicList.size >= GlobalBean.systemConfig.singleSeasonMusicNum
            ) {
                GlobalBean.seasonInProgress.value = true
                log.debug { "seasonInProgress is: ${GlobalBean.seasonInProgress.value}" }
            }
        }.getOrElse {
            log.warn { "getMusic Failed by keyword: $keyword, cometSender: ${comet.username}, completeContent: ${comet.content}" }
        }
    }

    private fun trans2Comet(info: List<*>): Comet {
        val content = info[INFO_CONTENT_IDX] as String

        val user = info[INFO_USER_IDX] as List<*>
        val uid = user[USER_UID_IDX].toString().toLong()
        val username = user[USER_USERNAME_IDX] as String
        val isRoomManager = user[USER_IS_ROOM_MANAGER_IDX] == 1
        val isAnchorman = uid == roomConfig.anchormanUID

        val status = info[INFO_STATUS_IDX] as List<*>
        val timestamp = status[TIMESTAMP_IDX] as Long

        val medal = info[INFO_MEDAL_IDX] as List<*>
        if (medal.isEmpty()) {
            return Comet(
                uid, username, content, isAnchorman, isRoomManager, timestamp
            )
        }

        val medalName = medal[MEDAL_NAME_IDX] as String
        val medalAnchorman = medal[MEDAL_ANCHORMAN_IDX] as String
        val medalAnchormanUID = medal[MEDAL_ANCHORMAN_UID_IDX].toString().toLong()
        val medalRoomId = medal[MEDAL_ROOM_ID_IDX].toString().toLong()
        val medalLevel = medal[MEDAL_LEVEL_IDX] as Int

        return Comet(
            uid,
            username,
            content,
            medalName,
            medalAnchorman,
            medalAnchormanUID,
            medalRoomId,
            medalLevel,
            isAnchorman,
            isRoomManager,
            timestamp
        )
    }

}