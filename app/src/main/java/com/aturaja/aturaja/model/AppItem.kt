package com.aturaja.aturaja.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class AppItem(
    val icon: Drawable,
    var status: Boolean ?= null,
    val detailApp: DetailItem
    )

@Parcelize
data class DetailItem(
    val name: String,
    val packageName: String
): Parcelable