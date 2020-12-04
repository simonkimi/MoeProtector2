package ink.z31.moe_protector.network.bean.challenge

import ink.z31.moe_protector.network.bean.common.PveBuff
import ink.z31.moe_protector.network.bean.common.PveLevel
import ink.z31.moe_protector.network.bean.common.PveNode

data class PveDataBean(
        val pveLevel: List<PveLevel>,
        val pveNode: List<PveNode>,
        val pveBuff: List<PveBuff>
)
