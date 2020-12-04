package ink.z31.moe_protector.network.bean.common

data class UserShipVo(
        var id: Int,
        var title: String,
        var level: Int,
        var exp: Int,
        var fleet_id: Int,
        var love: Int,
        var isLocked: Int,
        var shipCid: Int,
        var type: Int,

        var battleProps: BattleProps,
        var battlePropsMax: BattleProps
) {

    data class BattleProps(
            var hp: Int,
            var oil: Int,
            var ammo: Int,
            var aluminium: Int
    )
}