package ink.z31.moe_protector.util

import android.util.Base64
import android.util.Log
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object CommonUtil {
    private const val TAG = "CommonUtil"
    private const val ENCODING = "UTF-8"
    private const val MAC_NAME = "HmacSHA1"
    fun encryptionHMAC(source: String): String {
        val secretKey: SecretKey = SecretKeySpec(
            "kHPmWZ4zQBYP24ubmJ5wA4oz0d8EgIFe".toByteArray(
                charset(
                    ENCODING
                )
            ), MAC_NAME
        )
        val mac = Mac.getInstance(MAC_NAME)
        mac.init(secretKey)
        mac.update(source.toByteArray(charset(ENCODING)))
        val b = mac.doFinal()
        return Base64.encodeToString(b, 2)
    }

    fun delay(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
    }

    fun randomInt(min: Int, max: Int): Int {
        return (min + Math.random() * (max - min + 1)).toInt()
    }
}