package ink.z31.moe_protector.game

import com.google.gson.Gson
import ink.z31.moe_protector.network.bean.init.InitDataBean
import ink.z31.moe_protector.network.bean.init.ShipCardWu
import ink.z31.moe_protector.network.bean.init.ShipEquipmnt

object GameConstant {
    lateinit var shipCardMap: Map<Int, ShipCardWu>
    lateinit var shipCardWuMap: Map<Int, ShipCardWu>
    lateinit var shipEquipment: Map<Int, ShipEquipmnt>
    var errCode: MutableMap<String, String> = mutableMapOf("-9999" to "服务器正在维护")
    lateinit var dataVersion: String

    fun init(initJson: String) {
        val initBean = Gson().fromJson(initJson, InitDataBean::class.java)
        shipCardMap = initBean.shipCard.associateBy { it.cid }
        shipCardWuMap = initBean.shipCardWu.associateBy { it.cid }
        shipEquipment = initBean.shipEquipmnt.associateBy { it.cid }
        errCode.putAll(initBean.errorCode)
        dataVersion = initBean.DataVersion
    }

    fun getShipName(cid: Int): String {
        return shipCardWuMap[cid]?.title ?: "未知"
    }

    fun getShipNameFucked(cid: Int): String {
        return shipCardMap[cid]?.title ?: "未知"
    }

    fun getShipStar(cid: Int): Int {
        return shipCardWuMap[cid]?.star ?: 0
    }

    fun getShipIndex(cid: Int): Int {
        return shipCardWuMap[cid]?.shipIndex?.toInt() ?: 1
    }

    fun getErrMsg(code: String): String {
        return errCode[code] ?: "未知错误"
    }
}