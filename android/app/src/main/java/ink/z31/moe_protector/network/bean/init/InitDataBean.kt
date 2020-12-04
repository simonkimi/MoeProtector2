package ink.z31.moe_protector.network.bean.init

data class InitDataBean(
        val shipCard: List<ShipCardWu>,
        val shipCardWu: List<ShipCardWu>,
        val shipEquipmnt: List<ShipEquipmnt>,
        val errorCode: Map<String, String>,
        val DataVersion: String
)
