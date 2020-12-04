package ink.z31.moe_protector.network.bean.init

data class ShipCardWu(
        val cid: Int,
        val star: Int,
        val title: String,
        val country: String,
        val type: Int,
        val shipIndex: String,
        val equipmentType: List<Int>
)