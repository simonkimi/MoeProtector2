package ink.z31.moe_protector.game

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import ink.z31.moe_protector.network.GameSender
import ink.z31.moe_protector.network.HmException
import ink.z31.moe_protector.network.Http
import ink.z31.moe_protector.network.bean.init.InitDataBean
import ink.z31.moe_protector.network.bean.login.LoginServerListBean
import ink.z31.moe_protector.network.bean.login.LoginTokenBean
import ink.z31.moe_protector.network.bean.login.LoginVersionBean
import ink.z31.moe_protector.util.Encode
import ink.z31.moe_protector.util.FileUtil
import java.util.*


object FirstLogin {
    private const val TAG = "GameLogin"
    private const val ANDROID = 0
    const val IOS = 1

    lateinit var username: String
    lateinit var password: String
    lateinit var userId: String

    lateinit var channel: String
    lateinit var resUrl: String
    lateinit var urlVersion: String
    lateinit var loginServer: String
    lateinit var hmLoginServer: String
    lateinit var newVersionId: String

    var token = ""
    var serviceIndex: Int = 0

    fun init(username: String, password: String, serviceIndex: Int) {
        this.username = username
        this.password = password
        this.serviceIndex = serviceIndex
        this.token = ""
    }


    suspend fun login(): LoginServerListBean {
        for (i in 0..1) {
            if (token.isEmpty()) {
                checkVersion()
                token = getLoginToken()
            }
            if (checkToken()) {
                break
            }
            throw Exception("请求Token发生未知错误")
        }
        return getServerList()
    }


    private suspend fun checkVersion() {
        when (serviceIndex) {
            ANDROID -> {
                channel = "100011"
                resUrl = "http://login.jr.moefantasy.com/index/getInitConfigs/"
                urlVersion = "http://version.jr.moefantasy.com/index/checkVer/4.1.0/100011/2&version=4.1.0&channel=100016&market=2"
            }
            IOS -> {
                channel = "100015"
                resUrl = "http://loginios.jr.moefantasy.com/index/getInitConfigs/"
                urlVersion = "http://version.jr.moefantasy.com/index/checkVer/4.1.0/100015/2&version=4.1.0&channel=100015&market=2"
            }
        }
        // 检查Version
        val versionString = Http.get(urlVersion)
        val version = Gson().fromJson(versionString, LoginVersionBean::class.java)
        loginServer = version.loginServer
        hmLoginServer = version.hmLoginServer
        newVersionId = version.version.newVersionId
        checkInitVersion(version.version.DataVersion)
        GameSender.init(
                version = newVersionId,
                channel = channel
        )
    }

    private suspend fun checkToken(): Boolean {
        val url = "${hmLoginServer}1.0/get/userInfo/@self"
        val checkToken = Gson().fromJson(Http.post(url, """{"access_token": "$token"}"""), LoginTokenBean::class.java)
        return checkToken.error == 0
    }

    private suspend fun getLoginToken(): String {
        val loginJson = """
            {"platform": "0","appid": "0","app_server_type": "0","password": "$password","username": "$username"}
        """.trimIndent()
        val url = "${hmLoginServer}1.0/get/login/@self"
        val tokenBean = Gson().fromJson(Http.post(url, loginJson), LoginTokenBean::class.java)
        if (tokenBean.error != 0) {
            throw HmException("-113")
        }
        return tokenBean.access_token
    }

    private suspend fun getServerList(): LoginServerListBean {
        val url = "${loginServer}index/hmLogin/$token/${getUrlEnd()}"
        val serverInfo = Gson().fromJson(Http.get(url), LoginServerListBean::class.java)
        userId = serverInfo.userId
        return serverInfo
    }
    
    private suspend fun checkInitVersion(newVersion: String) {
        try {
            val initData = FileUtil.readFile("init.json")
            if (initData.isNotEmpty()) {
                GameConstant.init(initData)
                if (GameConstant.dataVersion.toLong() < newVersion.toLong()) {
                    throw Exception("需要更新, 因为版本号低了")
                }
            } else {
                throw Exception("需要更新, 因为读文件为空")
            }
        } catch (e: Exception) {
            Log.e(TAG, "需要更新, 因为: $e")
            e.printStackTrace()
            try {
                val newInit = Http.get(resUrl)
                FileUtil.writeFile("init.json", newInit)
                GameConstant.init(newInit)
            } catch (e: Exception) {
                Log.e(TAG, "更新基础数据失败")
                throw e
            }
        }
    }

    fun getUrlEnd(): String {
        val key = "ade2688f1904e9fb8d2efdb61b5e398a"
        val time = Date().time * 1000
        val md5: String = Encode.stringToMD5(time.toString() + key)
        return "&t=$time&e=$md5&gz=1&market=2&channel=$channel&version=$newVersionId"
    }
}


object SecondLogin {
    private var host = ""

    fun init(host: String) {
        this.host = host
    }

    suspend fun login() {
        //登录发送用户数据
        val random = Random(FirstLogin.userId.toLong())
        val udid = (1..15).joinToString("") { random.nextInt(10).toString() }
        val userPhoneData = mapOf(
                "client_version" to FirstLogin.newVersionId,
                "udid" to udid,
                "phone_type" to Build.MODEL.replace(" ", "%20"),
                "phone_version" to Build.VERSION.RELEASE,
                "ratio" to "1920*1080",
                "service" to "CHINA%20MOBILE",
                "source" to "android",
                "affiliate" to "WIFI"
        ).map { "${it.key}=${it.value}" }.joinToString("&") + FirstLogin.getUrlEnd()
        Http.get(url = "${host}index/login/${FirstLogin.userId}?&$userPhoneData")
    }
}








