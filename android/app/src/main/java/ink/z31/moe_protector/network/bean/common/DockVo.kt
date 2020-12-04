package ink.z31.moe_protector.network.bean.common

data class DockVo(
        val id: Int,
        val locked: Int,
        val shipId: String?,
        val endTime: String?
)
