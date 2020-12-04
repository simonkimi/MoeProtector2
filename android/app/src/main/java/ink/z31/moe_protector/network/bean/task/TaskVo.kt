package ink.z31.moe_protector.network.bean.task

import ink.z31.moe_protector.network.bean.common.Condition

data class TaskVo(
        var taskCid: String,
        var title: String,
        var condition: MutableList<Condition>
)
