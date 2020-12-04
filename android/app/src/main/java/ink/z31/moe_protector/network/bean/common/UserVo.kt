package ink.z31.moe_protector.network.bean.common

data class UserVo(
        var uid: String,
        var username: String,
        
        var level: Int,
        var shipNumTop: Int,
        var equipmentNumTop: Int,

        var oil: Int,
        var ammo: Int,
        var steel: Int,
        var aluminium: Int
)