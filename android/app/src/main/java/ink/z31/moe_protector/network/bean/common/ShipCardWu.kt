package ink.z31.moe_protector.network.bean.common

data class ShipCardWu(
        var cid: Int,
        var star: Int,
        var title: String,
        var type: Int,
        var shipIndex: String,
        var equipmentType: MutableList<Int>
)
