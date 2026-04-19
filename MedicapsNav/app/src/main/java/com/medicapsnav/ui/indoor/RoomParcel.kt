package com.medicapsnav.ui.indoor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomParcel(
    val id: String,
    val name: String,
    val typeName: String,
    val emoji: String,
    val relativeX: Float,
    val relativeY: Float
) : Parcelable
