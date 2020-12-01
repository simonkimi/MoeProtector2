package ink.z31.moe_protector.bridge

import com.google.gson.Gson
import ink.z31.moe_protector.game.FirstLogin
import ink.z31.moe_protector.network.bean.login.LoginServerListBean
import io.flutter.plugin.common.BasicMessageChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirstLoginBridge : BasicMessageChannel.MessageHandler<String> {
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
            FirstLogin.username = login.username
            FirstLogin.password = login.password
            FirstLogin.serviceIndex = login.service
            FirstLogin.token = ""

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val data = FirstLogin.login()
                        withContext(Dispatchers.Main) {
                            reply.reply(Gson().toJson(FirstLoginReply(
                                    data = data
                            )))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            reply.reply(Gson().toJson(FirstLoginReply(
                                    code = -1,
                                    errMsg = e.toString(),
                                    data = null
                            )))
                        }
                    }
                }
            }
        }
    }
}