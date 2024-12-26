package com.example.multipart.data

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetroProgressCallback(
    private val callback: ProgressCallback,
) : Callback<ResponseBody> {
    override fun onResponse(
        call: Call<ResponseBody?>,
        response: Response<ResponseBody?>,
    ) {
        val body = response.body()
        if (body == null) {
            onFailure(call, RuntimeException("File response is null"))
        } else {
            callback.onSuccess(body.string())
        }
    }

    override fun onFailure(
        call: Call<ResponseBody?>,
        t: Throwable,
    ) {
        callback.onError(t)
    }
}
