package ink.z31.moe_protector.network


import ink.z31.moe_protector.util.Encode
import ink.z31.moe_protector.network.interceptor.EncodeInterceptor
import ink.z31.moe_protector.network.interceptor.HeaderInterceptor
import ink.z31.moe_protector.network.interceptor.LoggingInterceptor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Http {
    private var cookieStore = arrayListOf<Cookie>()
    private val okHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val host = url.host
                    return if (host.contains("passport") || host.contains("login")) {
                        arrayListOf()
                    } else {
                        cookieStore
                    }
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    if (url.host.contains("login")) {
                        cookieStore = ArrayList(cookies)
                    }
                }

            })
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(32, 5, TimeUnit.SECONDS))
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(EncodeInterceptor())
            .build()

    private suspend fun httpExecute(request: Request): String {
        val okCall = okHttpClient.newCall(request)
        return suspendCoroutine {
            okCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.body != null) {
                        val io = response.body!!.byteStream()
                        val content = String(Encode.ioToByteArray(io))
                        val err = HmException.checkError(content)
                        if (err != null) {
                            it.resumeWithException(err)
                        } else {
                            it.resume(content)
                        }
                    } else {
                        it.resume("")
                    }
                }
            })
        }
    }

    suspend fun get(url: String): String {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        return httpExecute(request)
    }

    suspend fun post(url: String, data: String, isText: Boolean = true): String {
        val mediaType = if (isText) {
            "text/x-markdown; charset=utf-8".toMediaType()
        } else {
            "application/json; charset=utf-8".toMediaType()
        }
        val request = Request.Builder()
                .url(url)
                .post(data.toRequestBody(mediaType))
                .build()
        return httpExecute(request)
    }

    suspend fun post(url: String, data: Map<String, String>): String {
        val formBody = FormBody.Builder()
        data.forEach { formBody.add(it.key, it.value) }
        val request = Request.Builder()
                .url(url)
                .post(formBody.build())
                .build()
        return httpExecute(request)
    }


}