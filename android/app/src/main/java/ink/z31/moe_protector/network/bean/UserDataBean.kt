package ink.z31.moe_protector.network.bean

import ink.z31.moe_protector.network.bean.common.*
import ink.z31.moe_protector.network.bean.task.TaskVo

data class UserDataBean(
        var userVo: UserVo,
        var equipmentVo: MutableList<EquipmentVo>,
        var dockVo: MutableList<DockVo>,
        var repairDockVo: MutableList<RepairDockVo>,
        var fleetVo: MutableList<FleetVo>,
        var packageVo: MutableList<PackageVo>,
        var unlockShip: MutableList<String>,
        var unlockEquipment: MutableList<String>,
        var taskVo: MutableList<TaskVo>,
        var pveExploreVo: PveExploreVo,
        var friendVo: FriendVo,
        var marketingData: MarketingData,
        var currentPveVo: CurrentPveVo
) {
    data class CurrentPveVo(
            var pveId: Int,
            var pveLevelId: Int
    )

    data class FriendVo(
            var sign: String,
            var avatar_cid: String,
            var username: String
    )
}