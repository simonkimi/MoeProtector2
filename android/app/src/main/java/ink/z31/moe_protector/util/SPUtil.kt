package ink.z31.moe_protector.util

import android.content.Context
import android.content.SharedPreferences
import ink.z31.moe_protector.data.App


object SPUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(file: String, key: String, default: T): T =
        App.context.getSharedPreferences(file, Context.MODE_PRIVATE).run {
            when (default) {
                is String -> getString(key, default) as T? ?: default
                is Int -> getInt(key, default) as T? ?: default
                is Boolean -> getBoolean(key, default) as T? ?: default
                is Long -> getLong(key, default) as T? ?: default
                else -> throw Exception("Unknown")
            }
        }

    fun set(file: String, block: SharedPreferences.Editor.() -> Unit) {
        val editor = App.context.getSharedPreferences(file, Context.MODE_PRIVATE).edit()
        editor.block()
        editor.apply()
    }
}