package ink.z31.moe_protector.network.interceptor

import android.os.Build
import ink.z31.moe_protector.util.CommonUtil
import ink.z31.moe_protector.util.DateUtil
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val uri = request.url.toUri()

        if (uri.host.contains("passport")) {  // 新api登录的header
            val gmtDate: String = DateUtil.GMTDate
            val stringToSign = "${request.method}\n$gmtDate\n${uri.path}"
            val newStringToSign = CommonUtil.encryptionHMAC(stringToSign)
            val authorization = "HMS 881d3SlFucX5R5hE:$newStringToSign"
            builder.header("Authorization", authorization)
            builder.header("Date", gmtDate)
            builder.header("Content-Type", "application/json")
        } else {
            builder.header(
                "User-Agent",
                String.format(
                    "Dalvik/2.1.0 (Linux; U; Android %s; %s Build/LMY48Z)",
                    Build.VERSION.RELEASE,
                    Build.MODEL
                )
            )
            builder.header("Accept-Encoding", "identity")
        }
        return chain.proceed(builder.build())
    }
}