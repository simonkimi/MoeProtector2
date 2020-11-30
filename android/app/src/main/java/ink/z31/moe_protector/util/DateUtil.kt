package ink.z31.moe_protector.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    val GMTDate: String
        get() {
            val cal = Calendar.getInstance()
            val greenwichDate = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US)
            greenwichDate.timeZone = TimeZone.getTimeZone("GMT")
            return greenwichDate.format(cal.time)
        }

    fun timeStamp2Date(seconds: String, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val sdf = SimpleDateFormat(format, Locale.CHINA)
        return sdf.format(Date((seconds + "000").toLong()))
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    fun timeStamp(): String {
        val time = System.currentTimeMillis()
        return (time / 1000).toString()
    }
}