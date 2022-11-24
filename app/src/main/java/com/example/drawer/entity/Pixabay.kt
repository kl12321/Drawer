package com.example.drawer.entity

import android.os.Bundle
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pixabay(
    @Json(name = "id") val id:String,
    @Json(name="user") val author:String,
    @Json(name="previewURL")val previewURL:String,
    @Json(name="largeImageURL")val LargerURL:String,
    @Json(name="downloads")val downloads:Int,
    @Json(name="imageSize") val pictureSize:Int,
    @Json(name="imageWidth") val width:Int,
    @Json(name = "imageHeight") val height:Int
) : Parcelable
