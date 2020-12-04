package ink.z31.moe_protector.network

import com.google.gson.Gson
import ink.z31.moe_protector.game.GameConstant
import ink.z31.moe_protector.network.bean.Eid
import kotlinx.coroutines.CancellationException

class HmException(private val code: String) : CancellationException() {

    companion object {
        @JvmStatic
        fun checkError(data: String): HmException? {
            if (data.substring(0, 10).contains("eid")) {
                val eid = Gson().fromJson(data, Eid::class.java)
                return HmException(eid.eid ?: "0")
            }
            return null;
        }
    }

    override fun toString(): String {
        return "Code: $code: $message"
    }

    override val message: String
        get() = GameConstant.getErrMsg(this.code)
}