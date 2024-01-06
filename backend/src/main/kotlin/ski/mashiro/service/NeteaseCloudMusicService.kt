package ski.mashiro.service

import ski.mashiro.entity.music.NeteaseCloudMusic

/**
 * @author mashirot
 */
interface NeteaseCloudMusicService {

    suspend fun login()

    suspend fun getLoginStatus(): Boolean

    suspend fun getMusicByKeyword(keyword: String): NeteaseCloudMusic

    suspend fun listMusicByKeyword(keyword: String): List<NeteaseCloudMusic>

    suspend fun getSongStatusById(songId: Long): Boolean

    suspend fun getMusicById(music: NeteaseCloudMusic): NeteaseCloudMusic

}