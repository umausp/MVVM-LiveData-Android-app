package com.gojeck.feature.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BuiltBy(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("username")
    val username: String?
) : Parcelable
