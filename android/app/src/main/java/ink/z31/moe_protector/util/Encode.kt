package ink.z31.moe_protector.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.GZIPInputStream
import java.util.zip.Inflater

object Encode {
    /**
     * io转byte类型
     * @param input input
     * @return byte
     * @throws IOException io
     */
    @Throws(IOException::class)
    fun ioToByteArray(input: InputStream): ByteArray {
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var n = 0
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
        }
        return output.toByteArray()
    }

    /**
     * zlib解压缩
     * @param data 要解压缩的二进制
     * @return 解压完成的二进制
     */
    fun zlibDecompress(data: ByteArray): ByteArray {
        var output: ByteArray
        val decompresser = Inflater()
        decompresser.reset()
        decompresser.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i = decompresser.inflate(buf)
                o.write(buf, 0, i)
            }
            output = o.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        decompresser.end()
        return output
    }


    /**
     * gzip解压
     * @param bytes 要解压的byte
     * @return String数据类型
     */
    fun gzipUncompress(bytes: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        val `in` = ByteArrayInputStream(bytes)
        try {
            val ungzip = GZIPInputStream(`in`)
            val buffer = ByteArray(256)
            var n: Int
            while (ungzip.read(buffer).also { n = it } >= 0) {
                out.write(buffer, 0, n)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bytes
    }

    fun stringToMD5(plainText: String): String {
        var secretBytes: ByteArray? = null
        secretBytes = try {
            MessageDigest.getInstance("md5").digest(
                plainText.toByteArray()
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("没有这个md5算法！")
        }
        var md5code = BigInteger(1, secretBytes).toString(16)
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }
}