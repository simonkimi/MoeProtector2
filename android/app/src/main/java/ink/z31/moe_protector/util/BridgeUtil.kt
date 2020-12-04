package ink.z31.moe_protector.util

import com.google.gson.Gson
import io.flutter.plugin.common.BasicMessageChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BridgeUtil {
    suspend fun <T> msg(reply: BasicMessageChannel.Reply<String>, msg: T) {
        withContext(Dispatchers.Main) {
            reply.reply(Gson().toJson(msg))
        }
    }
}