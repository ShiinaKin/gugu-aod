package ski.mashiro.service.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean.JSON_MAPPER
import ski.mashiro.common.GlobalBean.neteaseCloudMusicConfig
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.exception.NeteaseCouldMusicException
import ski.mashiro.factory.OkHttpClientFactory
import ski.mashiro.factory.RequestBuilderFactory
import ski.mashiro.service.NeteaseCloudMusicService
import java.util.*

/**
 * @author mashirot
 */
object NeteaseCloudMusicServiceImpl : NeteaseCloudMusicService {
    private val log = KotlinLogging.logger { this::class.java.name }

    override suspend fun login() {
        if (Objects.isNull(neteaseCloudMusicConfig.phoneNumber) ||
            (Objects.isNull(neteaseCloudMusicConfig.password) && Objects.isNull(neteaseCloudMusicConfig.passwordMD5))
        ) {
            throw NeteaseCouldMusicException("手机号为空或密码/密码MD5为空")
        }
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/login/cellphone?" +
                "phone=${neteaseCloudMusicConfig.phoneNumber}&" +
                "" + if (Objects.nonNull(neteaseCloudMusicConfig.passwordMD5))
                        "md5_password=${neteaseCloudMusicConfig.passwordMD5}"
                    else
                        "password=${neteaseCloudMusicConfig.password}"
        val loginReq = RequestBuilderFactory.getReqBuilderWithUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        val json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(loginReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        if ((respResult["code"] as Int) != 200) {
            val msg = respResult["msg"] as String
            throw NeteaseCouldMusicException(msg)
        }

        val cookie = "MUSIC_U=${respResult["token"]}"
        neteaseCloudMusicConfig.cookie = cookie
    }

    override suspend fun getMusicByKeyword(keyword: String): NeteaseCloudMusic {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/search?keywords=$keyword&limit=2"
        val searchReq = RequestBuilderFactory.getReqBuilderWithUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        val json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(searchReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        val result = respResult["result"] as HashMap<*, *>
        val musics = result["songs"] as List<*>
        if (musics.isEmpty()) {
            throw NeteaseCouldMusicException("未找到歌曲")
        }
        val music = musics[0] as HashMap<*, *>
        return trans2MusicEntity(music)
    }

    override suspend fun listMusicByKeyword(keyword: String): List<NeteaseCloudMusic> {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/search?keywords=$keyword"
        val searchReq = RequestBuilderFactory.getReqBuilderWithUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        val json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(searchReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        val result = respResult["result"] as HashMap<*, *>
        val musics = result["songs"] as List<*>
        if (musics.isEmpty()) {
            throw NeteaseCouldMusicException("未找到歌曲")
        }
        return musics.map {
            val music = it as HashMap<*, *>
            trans2MusicEntity(music)
        }
    }

    override suspend fun getMusicById(music: NeteaseCloudMusic): NeteaseCloudMusic {
        if (!getSongStatusById(music.id)) {
            throw NeteaseCouldMusicException("歌曲无法播放")
        }
        val level = "exhigh"
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/song/url/v1?id=${music.id}&level=$level"
        val urlReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        var json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(urlReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        var respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        var data = (respResult["data"] as List<*>)[0] as HashMap<*, *>
        music.url = data["url"] as String
        val albumUrl = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/song/detail?ids=${music.id}"
        val detailReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url(albumUrl)
            .build()
        log.debug { "reqUrl: $albumUrl" }
        json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(detailReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        data = (respResult["songs"] as List<*>)[0] as HashMap<*, *>
        val album = data["al"] as HashMap<*, *>
        music.coverImgUrl = album["picUrl"] as String
        return music
    }

    override suspend fun getSongStatusById(songId: Long): Boolean {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/check/music?id=$songId"
        val checkReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        val json = okHttpClient.newCall(checkReq).execute().run {
            body!!.string()
        }
        log.debug { "respJson: $json" }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        return respResult["success"] as Boolean
    }

    override suspend fun getLoginStatus(): Boolean {
        if (StringUtils.isBlank(neteaseCloudMusicConfig.cookie)) {
            return false
        }
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val url = "${neteaseCloudMusicConfig.cloudMusicApiUrl}/login/status"
        val urlReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url(url)
            .build()
        log.debug { "reqUrl: $url" }
        val json = withContext(Dispatchers.IO) {
            okHttpClient.newCall(urlReq).execute().run {
                body!!.string()
            }
        }
        log.debug { "respJson: $json" }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        val data = respResult["data"] as Map<*, *>
        return data["account"] != null
    }

    private fun trans2MusicEntity(music: Map<*, *>): NeteaseCloudMusic {
        val artists = music["artists"] as List<*>
        var singer = ""
        val iter = artists.iterator()
        while (iter.hasNext()) {
            val artist = iter.next() as Map<*, *>
            singer += artist["name"].toString()
            if (iter.hasNext()) {
                singer += ", "
            }
        }
        return NeteaseCloudMusic(
            music["id"].toString().toLong(),
            music["name"] as String,
            singer,
            music["duration"].toString().toLong(),
            null,
            null
        )
    }

}