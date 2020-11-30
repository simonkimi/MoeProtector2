package ink.z31.moe_protector.network.bean.login

data class LoginServerListBean(
        val userId: String,
        val defaultServer: String,
        val serverList: List<ServerList>
)

data class ServerList (
        val id: String,
        val host: String,
        val name: String
)
