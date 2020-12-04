package ink.z31.moe_protector.network.bean.challenge

import ink.z31.moe_protector.network.bean.common.PveLevel
import ink.z31.moe_protector.network.bean.common.PveNode

data class PeventBean(
        val pveEventLevel: List<PveLevel>?,
        val pveNode: List<PveNode>?
)
