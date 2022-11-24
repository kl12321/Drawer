package com.example.drawer.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Results(
    @Json(name = "total")val total:String,
    @Json(name = "totalHits")val totalHits:Int,
    @Json(name = "hits")val hits:List<Pixabay>
) {
}