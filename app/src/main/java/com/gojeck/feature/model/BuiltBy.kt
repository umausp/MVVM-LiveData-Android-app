package com.gojeck.feature.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BuiltBy(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("username")
    val username: String?
)
