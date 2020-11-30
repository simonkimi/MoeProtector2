package ink.z31.moe_protector

import ink.z31.moe_protector.bridge.FirstLoginBridge
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.StringCodec

class MainActivity : FlutterActivity() {
    companion object {
        const val CHANNEL = "ink.z31.moe_protector"
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        buildBasicMessageChannel(flutterEngine, "first_login", FirstLoginBridge())
    }

    private fun buildBasicMessageChannel(flutterEngine: FlutterEngine,
                                         name: String,
                                         msg: BasicMessageChannel.MessageHandler<String>) {
        BasicMessageChannel(flutterEngine.dartExecutor.binaryMessenger, "$CHANNEL/$name", StringCodec.INSTANCE)
                .setMessageHandler(msg)
    }
}
