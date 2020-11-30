package ink.z31.moe_protector.bridge

import com.google.gson.Gson
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodChannel


class FirstLoginBridge : BasicMessageChannel.MessageHandler<String> {
    data class FirstLoginModel(
            val username: String,
            val password: String,
            val service: Int
    )

    data class FirstLoginReply(
            val code: Int,
            val errMsg: String?,
            val data: String
    )

    override fun onMessage(message: String?, reply: BasicMessageChannel.Reply<String>) {
        message?.let {
            val login = Gson().fromJson(it, FirstLoginModel::class.java)
            reply.reply(Gson().toJson(FirstLoginReply(
                    data = "${login.username}|${login.password}|${login.service}",
                    errMsg = null,
                    code = 0
            )))
        }
    }
}