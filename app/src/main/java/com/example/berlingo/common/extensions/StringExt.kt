package com.example.berlingo.common.extensions

import androidx.compose.ui.graphics.Color
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.google.android.gms.maps.model.LatLng
val logger: BaseLogger = FactoryLogger.getLoggerCompose("StringExt")

fun String.replaceWhiteSpaceWithPlus(): String {
    return this.replace(" ", "+")
}

fun String.getDepartureTime(): String? {
    return if (this.length >= 16 && this[10] == 'T') this.substring(11, 16) else null
}

fun String.decodePolyline(): List<LatLng> {
    val encoded = this
    val poly = mutableListOf<LatLng>()
    var index = 0
    var lat = 0
    var lng = 0

    while (index < encoded.length) {
        var shift = 0
        var result = 0
        do {
            val b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            val b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(p)
    }

    return poly
}

fun String.getLineProductColor(): Color {
    logger.debug("line prod color:$this")
    return when (this) {
        "bus" -> Color.Magenta
        "subway" -> Color.Blue
        "suburban" -> Color.Green
        "regional" -> Color.Red
        else -> Color.Transparent
    }
}

fun String.getLineProductIcon(): Int {
    logger.debug("line prod icon:$this")
    return when (this) {
        "bus" -> R.drawable.icon_bus // TODO fix this
        "subway" -> R.drawable.icon_ubahn
        "suburban" -> R.drawable.icon_sbahn
        "regional" -> R.drawable.icon_bahn
        else -> 0
    }
}

fun String.getLineNameIcon(): Int {
    return when (this) {
        "S1" -> R.drawable.icon_sbahn_s1
        "S2" -> R.drawable.icon_sbahn_s2
        "S3" -> R.drawable.icon_sbahn_s3
        "S5" -> R.drawable.icon_sbahn_s5
        "S7" -> R.drawable.icon_sbahn_s7
        "S8" -> R.drawable.icon_sbahn_s8
        "S9" -> R.drawable.icon_sbahn_s9
        "S25" -> R.drawable.icon_sbahn_s25
        "S26" -> R.drawable.icon_sbahn_s26
        "S41" -> R.drawable.icon_sbahn_s41
        "S42" -> R.drawable.icon_sbahn_s42
        "S45" -> R.drawable.icon_sbahn_s45
        "S46" -> R.drawable.icon_sbahn_s46
        "S47" -> R.drawable.icon_sbahn_s47
        "S75" -> R.drawable.icon_sbahn_s75
        "S85" -> R.drawable.icon_sbahn_s85
        "RE3" -> R.drawable.icon_rebahn_re3
        "RE4" -> R.drawable.icon_rebahn_re4
        "RE8" -> R.drawable.icon_rebahn_re8
        "RB14" -> R.drawable.icon_rebahn_rb14
//        "184" -> R.drawable.icon_rebahn_rb14
//        "S" -> R.drawable.icon_sbahn_s4
//        "S" -> R.drawable.icon_sbahn_s4
//        "S" -> R.drawable.icon_sbahn_s4
//        "S" -> R.drawable.icon_sbahn_s4
        else -> R.drawable.icon_search
    }
}
