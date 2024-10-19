package com.kosmas.tecprin.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("OkHttp", String.format("Sending request %s", request.url))

        val response = chain.proceed(request)

        Log.d(
            "OkHttp", String.format(
                "Received response %s from %s", response.body,
                response.request.url
            )
        )

        return response
    }

}
