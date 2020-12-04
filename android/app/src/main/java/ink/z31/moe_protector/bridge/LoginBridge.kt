package ink.z31.moe_protector.bridge

import com.google.gson.Gson
import ink.z31.moe_protector.game.FirstLogin
import ink.z31.moe_protector.game.SecondLogin
import ink.z31.moe_protector.network.GameSender
import ink.z31.moe_protector.network.bean.login.LoginServerListBean
import ink.z31.moe_protector.util.BridgeUtil
import io.flutter.plugin.common.BasicMessageChannel
import kotlinx.coroutines.*


class SecondLoginBridge : BasicMessageChannel.MessageHandler<String> {
    data class SecondLoginMsg(
            val host: String
    )

    override fun onMessage(message: String?, reply: BasicMessageChannel.Reply<String>) {
        message?.let {
            val msg = Gson().fromJson(it, SecondLoginMsg::class.java)
            GameSender.setHost(msg.host)
            SecondLogin.init(msg.host)
        }
    }
}


class FirstLoginBridge : BasicMessageChannel.MessageHandler<String> {
    companion object {
        private const val TAG = "LoginBridge"
    }

    data class FirstLoginModel(
            val username: String,
            val password: String,
            val service: Int
    )

    data class FirstLoginReply(
            val code: Int = 0,
            val errMsg: String? = null,
            val data: LoginServerListBean?
    )

    override fun onMessage(message: String?, reply: BasicMessageChannel.Reply<String>) {
        message?.let {
            val login = Gson().fromJson(it, FirstLoginModel::class.java)

            FirstLogin.init(
                    username = login.username,
                    password = login.password,
                    serviceIndex = login.service
            )

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val data = FirstLogin.login()
                        BridgeUtil.msg(reply, FirstLoginReply(
                                data = data
                        ))
                    } catch (e: Exception) {
                        BridgeUtil.msg(reply, FirstLoginReply(
                                code = -1,
                                errMsg = e.toString(),
                                data = null
                        ))
                    }
                }
            }
        }
    }
}