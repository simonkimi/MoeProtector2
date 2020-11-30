package ink.z31.moe_protector.network.interceptor


import ink.z31.moe_protector.util.Encode
import ink.z31.moe_protector.network.HmException
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class EncodeInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val mediaType: MediaType? = response.body!!.contentType()

        response.body?.let {
            val io = it.byteStream()
            var content: ByteArray = Encode.ioToByteArray(io)
            // 解压
            if (content[0] == 120.toByte() && content[1] == 218.toByte()) {
                content = Encode.zlibDecompress(content)
            } else if (content[0] == 31.toByte() && content[1] == 139.toByte()) {
                content = Encode.gzipUncompress(content)
            }
            HmException.checkError(String(content))
            val responseBody = content.toResponseBody(mediaType)
            return response.newBuilder().body(responseBody).build()
        }
        return response
    }
}