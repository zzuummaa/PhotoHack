package ru.zuma.photohack.nlp

import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import com.google.gson.JsonObject
import android.provider.MediaStore.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Entity


val JSON = MediaType.parse("application/json; charset=utf-8")

class NLPService {
    private val baseURL = "http://10.1.38.120:8000/"
    private val gson = Gson()

    fun predictSituation(message: String) : ArrayList<PredictSituatuionResult> {
        val client = OkHttpClient()

        val json = gson.toJson(PredictSituationRequest(message))
        val request = Request.Builder()
            .url(baseURL + "predict")
            .post(RequestBody.create(JSON, json))
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Log.e(javaClass.simpleName, "Status code ${response.code()} from: ${request.url()}")
            return ArrayList()
        }

        return Gson().fromJson(json, object : TypeToken<ArrayList<PredictSituatuionResult>>() {}.type)
    }
}