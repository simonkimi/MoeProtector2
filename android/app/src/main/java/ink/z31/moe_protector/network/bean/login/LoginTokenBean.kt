package ink.z31.moe_protector.network.bean.login

data class LoginTokenBean(
        val error: Int,
        val errmsg: String?,
        val access_token: String
)