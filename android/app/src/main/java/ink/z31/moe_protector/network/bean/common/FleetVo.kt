package ink.z31.moe_protector.network.bean.common

data class FleetVo(
        var id: String,
        var title: String,
        var ships: MutableList<Int>
)
