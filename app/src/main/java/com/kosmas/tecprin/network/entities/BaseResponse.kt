package com.kosmas.tecprin.network.entities

import com.google.gson.annotations.SerializedName

open class BaseResponse
{
    @SerializedName("message")
    var message = ""
        private set
}
