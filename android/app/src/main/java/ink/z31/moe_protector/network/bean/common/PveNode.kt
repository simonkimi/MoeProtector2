package ink.z31.moe_protector.network.bean.common

data class PveNode(
        val id: String,
        val roundabout: String,
        val flag: String,
        val nextNode: List<Int>,
        val nodeType: String,
        val buff: List<Int>,
        val gain: Map<String, Int>?,
        val loss: Map<String, Int>?
)
