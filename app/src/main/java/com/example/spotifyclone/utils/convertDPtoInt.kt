package com.example.spotifyclone.utils

import android.util.DisplayMetrics
import android.util.TypedValue

fun convertDPtoInt(value: Float): Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value,
            DisplayMetrics()
        ).toInt()