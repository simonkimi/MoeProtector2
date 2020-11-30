package ink.z31.moe_protector.bridge

import com.google.gson.Gson
import ink.z31.moe_protector.game.FirstLogin
import ink.z31.moe_protector.network.bean.login.ServerList
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodChannel
import java.lang.Exception
import kotlin.concurrent.thread


class FirstLoginBridge : BasicMessageChannel.MessageHandler<String> {
    data class FirstLoginModel(
            val username: String,
            val password: String,
            val service: Int
    )

    data class FirstLoginReply(
            val code: Int = 0,
            val errMsg: String? = null,
            val data: List<ServerList>?
    )

    override fun onMessage(message: String?, reply: BasicMessageChannel.Reply<String>) {
        message?.let {
            val login = Gson().fromJson(it, FirstLoginModel::class.java)
            FirstLogin.username = login.username
            FirstLogin.password = login.password
            FirstLogin.serviceIndex = login.service



            thread(start = true) {
                try {
                    reply.reply(Gson().toJson(FirstLoginReply(
                            data = FirstLogin.login()
                    )))
                } catch (e: Exception) {
                    e.printStackTrace()
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