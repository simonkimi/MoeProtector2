package ink.z31.moe_protector.game

import com.google.gson.Gson
import ink.z31.moe_protector.network.GameSender
import ink.z31.moe_protector.network.HmException
import ink.z31.moe_protector.network.Http
import ink.z31.moe_protector.network.bean.login.LoginServerListBean
import ink.z31.moe_protector.network.bean.login.LoginTokenBean
import ink.z31.moe_protector.network.bean.login.LoginVersionBean
import ink.z31.moe_protector.network.bean.login.ServerList
import ink.z31.moe_protector.util.Encode
import java.util.*


object FirstLogin {
    lateinit var username: String
    lateinit var password: String
    var serviceIndex = 0

    lateinit var channel: String
    lateinit var resUrl: String
    lateinit var urlVersion: String
    lateinit var loginServer: String
    lateinit var hmLoginServer: String
    lateinit var newVersionId: String

    var token = ""


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
            0 -> {
                channel = "100016"
                resUrl = "http://login.jr.moefantasy.com/index/getInitConfigs/"
                urlVersion = "http://version.jr.moefantasy.com/index/checkVer/4.1.0/100016/2&version=4.1.0&channel=100016&market=2"
            }
            1 -> {
                channel = "100015"
                resUrl = "http://loginios.jr.moefantasy.com/index/getInitConfigs/"
                urlVersion = "http://version.jr.moefantasy.com/index/checkVer/4.1.0/100015/2&version=4.1.0&channel=100015&market=2"
            }
        }
        // 检查Version
        val version = Gson().fromJson(Http.get(urlVersion), LoginVersionBean::class.java)
        loginServer = version.loginServer
        hmLoginServer = version.hmLoginServer
        newVersionId = version.version.newVersionId
        GameSender.channel = channel
        GameSender.version = newVersionId
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
        return Gson().fromJson(Http.get(url), LoginServerListBean::class.java)
    }

    private fun getUrlEnd(): String {
        val key = "ade2688f1904e9fb8d2efdb61b5e398a"
        val time = Date().time * 1000
        val md5: String = Encode.stringToMD5(time.toString() + key)
        return "&t=$time&e=$md5&gz=1&market=2&channel=$channel&version=$newVersionId"
    }
}











