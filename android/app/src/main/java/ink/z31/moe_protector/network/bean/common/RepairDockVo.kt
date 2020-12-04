package ink.z31.moe_protector.network.bean.common

data class RepairDockVo(
        var id: Int,
        var locked: Int,
        var shipId: String?,
        var endTime: String?
)
