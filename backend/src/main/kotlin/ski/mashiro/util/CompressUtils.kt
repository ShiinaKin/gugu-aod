package ski.mashiro.util

import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

/**
 * @author mashirot
 */
object CompressUtils {

    fun zlibUncompressed(byteArray: ByteArray): String {
        val buffer = ByteArray(1024 * 5)
        val inflater = Inflater()
        inflater.setInput(byteArray)
        val endIdx = inflater.inflate(buffer)
        val jsonByte = buffer.copyOfRange(16, endIdx)
        return String(jsonByte)
    }

    fun brotliUncompressed(byteArray: ByteArray): String {
        BrotliCompressorInputStream(ByteArrayInputStream(byteArray)).use { brIn ->
            val bos = ByteArrayOutputStream()
            var readByte: Int
            while ((brIn.read().also { readByte = it }) != -1) {
                bos.write(readByte)
            }
            return String(bos.toByteArray())
        }
    }

}