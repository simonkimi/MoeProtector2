package ink.z31.moe_protector.util

import android.content.Context
import android.util.Log
import ink.z31.moe_protector.data.App
import java.io.*

object FileUtil {
    private const val TAG = "FileUtil"

    fun writeFile(fileName: String, data: String) {
        try {
            val output = App.context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(data)
            }
        } catch (e: IOException) {
            Log.e(TAG, "写文件${fileName}时发生错误")
            throw e
        }
    }

    fun readFile(fileName: String): String {
        val content = StringBuilder()
        try {
            val input = App.context.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "读文件${fileName}时发生错误")
        }
        return content.toString()
    }
}