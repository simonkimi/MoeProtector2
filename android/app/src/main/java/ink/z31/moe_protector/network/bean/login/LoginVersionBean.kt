package ink.z31.moe_protector.network.bean.login

data class LoginVersionBean(
        val eid: String?,
        val version: Version,
        val loginServer: String,
        val hmLoginServer: String
)

data class Version(
        val newVersionId: String,
        val DataVersion: String
)