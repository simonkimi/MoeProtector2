package ink.z31.moe_protector.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val startTime = System.nanoTime()
        Log.d(TAG, "发送数据 ${request.url} on ${chain.connection()}")
        val response = chain.proceed(request)
        val endTime = System.nanoTime()
        Log.d(
            TAG, String.format(
                "接收数据 %s in %.1fms%n",
                response.request.url, (endTime - startTime) / 1e6
            )
        )
        return response
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }
}