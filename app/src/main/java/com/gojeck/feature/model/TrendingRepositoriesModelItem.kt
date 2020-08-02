package com.gojeck.feature.model


import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "trendingRepositoriesEntity")
data class TrendingRepositoriesModelItem(

    @PrimaryKey(autoGenerate = true) val uid: Int,

    @ColumnInfo(name = "author")
    @SerializedName("author")
    var author: String = "",

    @ColumnInfo(name = "avatar")
    @SerializedName("avatar")
    var avatar: String = "",

    @ColumnInfo(name = "currentPeriodStars")
    @SerializedName("currentPeriodStars")
    var currentPeriodStars: Int = 0,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String = "",

    @ColumnInfo(name = "forks")
    @SerializedName("forks")
    var forks: Int = 0,

    @ColumnInfo(name = "language")
    @SerializedName("language")
    val language: String?,

    @ColumnInfo(name = "languageColor")
    @SerializedName("languageColor")
    val languageColor: String?,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String = "",

    @ColumnInfo(name = "stars")
    @SerializedName("stars")
    var stars: Int = 0,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    var url: String = ""
){
//    fun getLanguageColor() : String{
//        return languageColor ?: "#ffcc0000"
//    }
}
