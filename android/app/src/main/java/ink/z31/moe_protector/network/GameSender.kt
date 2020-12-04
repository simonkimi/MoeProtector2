package ink.z31.moe_protector.network

object GameSender {
    private lateinit var channel: String
    private lateinit var version: String
    private lateinit var host: String
    
    fun init(version: String, channel: String) {
        this.version = version
        this.channel = channel
    }
    
    fun setHost(host: String) {
        this.host = host
    }


}