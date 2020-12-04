package ink.z31.moe_protector.network.bean.common

data class PveExploreVo(
    val levels: MutableList<Levels>
) {
    data class Levels(
            val exploreId: String,
            val fleetId: String,
            val endTime: Int
    )
}
