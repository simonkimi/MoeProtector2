package ink.z31.moe_protector.data

import android.util.Log
import com.google.gson.Gson
import ink.z31.moe_protector.network.bean.challenge.PveDataBean
import ink.z31.moe_protector.network.bean.UserDataBean
import ink.z31.moe_protector.network.bean.challenge.PeventBean
import ink.z31.moe_protector.network.bean.common.*
import java.lang.Exception

object User {
    private const val TAG = "User"

    lateinit var userBaseData: UserDataBean

    // 用户数据
    fun parseCrazy(crazyData: String) {
        userBaseData = Gson().fromJson(crazyData, UserDataBean::class.java)
    }

    // ----------------- 用户数据 --------------
    fun updateUserResVo(userResVo: UserResVo) {
        this.userBaseData.userVo.oil = userResVo.oil
        this.userBaseData.userVo.ammo = userResVo.ammo
        this.userBaseData.userVo.steel = userResVo.steel
        this.userBaseData.userVo.aluminium = userResVo.aluminium
    }


    // ----------------- 装备信息 -----------------
    var equipmentMap: Map<Int, EquipmentVo> = mapOf()

    fun setEquipmentMap(equipmentVos: List<EquipmentVo>) {
        equipmentMap = equipmentVos.associateBy { it.equipmentCid }
    }

    fun equipmentMapSize(): Int {
        return equipmentMap.map { it.value.num }.sum()
    }

    // ----------------- 点数数据 -----------------
    val pveNode: MutableMap<String, PveNode> = mutableMapOf()
    val pveLevel: MutableMap<String, PveLevel> = mutableMapOf()
    val pveBuff: MutableMap<String, PveBuff> = mutableMapOf()

    fun parsePveNode(pveStr: String) {
        val pveBean = Gson().fromJson(pveStr, PveDataBean::class.java)
        pveNode.putAll(pveBean.pveNode.associateBy { it.id })
        pveLevel.putAll(pveBean.pveLevel.associateBy { it.id })
        pveBuff.putAll(pveBean.pveBuff.associateBy { it.id })
    }

    fun parsePeventNode(pveStr: String) {
        try {
            val pevent = Gson().fromJson(pveStr, PeventBean::class.java)
            pevent.pveEventLevel?.let {
                pveLevel.putAll(it.associateBy { node -> node.id })
            }
            pevent.pveNode?.let {
                pveNode.putAll(it.associateBy { node -> node.id })
            }
        } catch (e: Exception) {
            Log.e(TAG, "解析Pve数据失败")
        }
    }

    // -------------------用户舰队------------------
    var fleetMap: MutableMap<String, FleetVo> = mutableMapOf()

    fun setFleetMap(fleetVos: List<FleetVo>) {
        fleetMap = fleetVos.associateBy { it.id }.toMutableMap()
    }


    // -------------------用户船只------------------
    var shipMap: MutableMap<Int, UserShipVo> = mutableMapOf()

    fun setShipMap(userShipVos: List<UserShipVo>) {
        shipMap.putAll(userShipVos.associateBy { it.id })
    }
    
    fun addShipMap(userShipVo: UserShipVo) {
        shipMap[userShipVo.id] = userShipVo
    }
    
    fun delShipMap(id: Int): UserShipVo? {
        return shipMap.remove(id)
    }
    
    fun getShipHp(id: Int): Int {
        return shipMap[id]?.battleProps?.hp ?: 0
    }
    
    fun getShipMaxHp(id: Int): Int {
        return shipMap[id]?.battlePropsMax?.hp ?: 0
    }



}