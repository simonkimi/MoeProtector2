package ink.z31.moe_protector.network.bean.common

data class MarketingData(
        val continueLoginAward: ContinueLoginAward
) {
    data class ContinueLoginAward(
            val canGetDay: Int
    )
}
