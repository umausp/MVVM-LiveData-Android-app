package com.gojeck.androidtesting.utils

import com.gojeck.utils.ctx
import java.io.InputStream

object ReadJsomnFromAsset {

    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = ctx.assets.open("yourFile.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
