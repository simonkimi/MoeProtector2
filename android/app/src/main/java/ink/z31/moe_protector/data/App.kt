package ink.z31.moe_protector.data

import android.content.Context
import io.flutter.app.FlutterApplication

class App : FlutterApplication() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}