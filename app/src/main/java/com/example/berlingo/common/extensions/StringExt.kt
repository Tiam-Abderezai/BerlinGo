package com.example.berlingo.common.extensions

import androidx.compose.ui.graphics.Color
import com.example.berlingo.R
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.google.android.gms.maps.model.LatLng
import java.util.TimeZone

val logger: BaseLogger = FactoryLogger.getLoggerCompose("StringExt")

fun String.replaceWhiteSpaceWithPlus(): String {
    return this.replace(" ", "+")
}

fun String.convertEpochTime(): String? {
    return if (this.length >= 16 && this[10] == 'T') this.substring(11, 16) else null
}

fun String.convertEpochDate(): String? {
    return if (this.length >= 16 && this[10] == 'T') this.substring(0, 10) else null
}

/*
decodePolyline() decodes a string-encoded polyline into a list of LatLng objects.
It's commonly used to decode polylines provided by mapping services like Google Maps.
The polyline encoding is a compact form of a list of coordinates.
 */
fun String.decodePolyline(): List<LatLng> {
    // Initialization:
    val encoded = this
    val poly = mutableListOf<LatLng>()
    var index = 0
    var lat = 0
    var lng = 0
    // Decoding loop:
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
        else -> R.drawable.icon_error
    }
}

fun String.getLineNameIcon(): Int {
    return when (this) {
        "U1" -> R.drawable.icon_ubahn_u1
        "U2" -> R.drawable.icon_ubahn_u2
        "U3" -> R.drawable.icon_ubahn_u3
        "U4" -> R.drawable.icon_ubahn_u4
        "U5" -> R.drawable.icon_ubahn_u5
        "U6" -> R.drawable.icon_ubahn_u6
        "U7" -> R.drawable.icon_ubahn_u7
        "U8" -> R.drawable.icon_ubahn_u8
        "U9" -> R.drawable.icon_ubahn_u9
//        "M1" -> R.drawable.icon_tram_m1
//        "M2" -> R.drawable.icon_tram_m2
//        "M4" -> R.drawable.icon_tram_m4
//        "M5" -> R.drawable.icon_tram_m5
//        "M6" -> R.drawable.icon_tram_m6
//        "M8" -> R.drawable.icon_tram_m8
//        "M10" -> R.drawable.icon_tram_m10
//        "M13" -> R.drawable.icon_tram_m13
//        "M17" -> R.drawable.icon_tram_m17
        "S1" -> R.drawable.icon_sbahn_s1
        "S2" -> R.drawable.icon_sbahn_s2
        "S3" -> R.drawable.icon_sbahn_s3
        "S4" -> R.drawable.icon_sbahn_s4
        "S5" -> R.drawable.icon_sbahn_s5
        "S6" -> R.drawable.icon_sbahn_s6
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
        "RE1" -> R.drawable.icon_rebahn_re1
        "RE2" -> R.drawable.icon_rebahn_re2
        "RE3" -> R.drawable.icon_rebahn_re3
        "RE4" -> R.drawable.icon_rebahn_re4
        "RE5" -> R.drawable.icon_rebahn_re5
        "RE6" -> R.drawable.icon_rebahn_re6
        "RE7" -> R.drawable.icon_rebahn_re7
        "RE8" -> R.drawable.icon_rebahn_re8
        "RE10" -> R.drawable.icon_rebahn_re10
        "RE11" -> R.drawable.icon_rebahn_re11
        "RE13" -> R.drawable.icon_rebahn_re13
        "RE14" -> R.drawable.icon_rebahn_re14
        "RE15" -> R.drawable.icon_rebahn_re15
        "RE18" -> R.drawable.icon_rebahn_re18
        "RB10" -> R.drawable.icon_rebahn_rb10
        "RB12" -> R.drawable.icon_rebahn_rb12
        "RB14" -> R.drawable.icon_rebahn_rb14
        "RB20" -> R.drawable.icon_rebahn_rb20
        "RB21" -> R.drawable.icon_rebahn_rb21
        "RB22" -> R.drawable.icon_rebahn_rb22
        "RB23" -> R.drawable.icon_rebahn_rb23
        "RB24" -> R.drawable.icon_rebahn_rb24
        "RB25" -> R.drawable.icon_rebahn_rb25
        "RB26" -> R.drawable.icon_rebahn_rb26
        "RB27" -> R.drawable.icon_rebahn_rb27
        "RB31" -> R.drawable.icon_rebahn_rb31
        "RB32" -> R.drawable.icon_rebahn_rb32
        "RB33" -> R.drawable.icon_rebahn_rb33
        "RB34" -> R.drawable.icon_rebahn_rb34
        "RB35" -> R.drawable.icon_rebahn_rb35
        "RB36" -> R.drawable.icon_rebahn_rb36
        "RB37" -> R.drawable.icon_rebahn_rb37
        "RB43" -> R.drawable.icon_rebahn_rb43
        "RB45" -> R.drawable.icon_rebahn_rb45
        "RB46" -> R.drawable.icon_rebahn_rb46
        "RB49" -> R.drawable.icon_rebahn_rb49
        "RB51" -> R.drawable.icon_rebahn_rb51
        "RB54" -> R.drawable.icon_rebahn_rb54
        "RB55" -> R.drawable.icon_rebahn_rb55
        "RB57" -> R.drawable.icon_rebahn_rb57
        "RB60" -> R.drawable.icon_rebahn_rb60
        "RB61" -> R.drawable.icon_rebahn_rb61
        "RB62" -> R.drawable.icon_rebahn_rb62
        "RB63" -> R.drawable.icon_rebahn_rb63
        "RB65" -> R.drawable.icon_rebahn_rb65
        "RB66" -> R.drawable.icon_rebahn_rb66
        "RB73" -> R.drawable.icon_rebahn_rb73
        "RB74" -> R.drawable.icon_rebahn_rb74
        "RB91" -> R.drawable.icon_rebahn_rb91
        "RB92" -> R.drawable.icon_rebahn_rb92
        "RB93" -> R.drawable.icon_rebahn_rb93
        "M11" -> R.drawable.icon_bus_m11
        "M19" -> R.drawable.icon_bus_m19
        "M21" -> R.drawable.icon_bus_m21
        "M27" -> R.drawable.icon_bus_m27
        "M29" -> R.drawable.icon_bus_m29
        "M32" -> R.drawable.icon_bus_m32
        "M36" -> R.drawable.icon_bus_m36
        "M37" -> R.drawable.icon_bus_m37
        "M41" -> R.drawable.icon_bus_m41
        "M43" -> R.drawable.icon_bus_m43
        "M44" -> R.drawable.icon_bus_m44
        "M45" -> R.drawable.icon_bus_m45
        "M46" -> R.drawable.icon_bus_m46
        "M48" -> R.drawable.icon_bus_m48
        "M49" -> R.drawable.icon_bus_m49
        "M76" -> R.drawable.icon_bus_m76
        "M77" -> R.drawable.icon_bus_m77
        "M82" -> R.drawable.icon_bus_m82
        "M85" -> R.drawable.icon_bus_m85
        "X7" -> R.drawable.icon_bus_x7
        "X10" -> R.drawable.icon_bus_x10
        "X11" -> R.drawable.icon_bus_x11
        "X21" -> R.drawable.icon_bus_x21
        "X33" -> R.drawable.icon_bus_x33
        "X34" -> R.drawable.icon_bus_x34
        "X37" -> R.drawable.icon_bus_x37
        "X49" -> R.drawable.icon_bus_x49
        "X54" -> R.drawable.icon_bus_x54
        "X69" -> R.drawable.icon_bus_x69
        "X71" -> R.drawable.icon_bus_x71
        "X76" -> R.drawable.icon_bus_x76
        "X83" -> R.drawable.icon_bus_x83
        "100" -> R.drawable.icon_bus_100
        "101" -> R.drawable.icon_bus_101
        "106" -> R.drawable.icon_bus_106
        "107" -> R.drawable.icon_bus_107
        "108" -> R.drawable.icon_bus_108
        "109" -> R.drawable.icon_bus_109
        "110" -> R.drawable.icon_bus_110
        "112" -> R.drawable.icon_bus_112
        "114" -> R.drawable.icon_bus_114
        "115" -> R.drawable.icon_bus_115
        "118" -> R.drawable.icon_bus_118
        "120" -> R.drawable.icon_bus_120
        "122" -> R.drawable.icon_bus_122
        "123" -> R.drawable.icon_bus_123
        "124" -> R.drawable.icon_bus_124
        "125" -> R.drawable.icon_bus_125
        "128" -> R.drawable.icon_bus_128
        "130" -> R.drawable.icon_bus_130
        "131" -> R.drawable.icon_bus_131
        "133" -> R.drawable.icon_bus_133
        "134" -> R.drawable.icon_bus_134
        "135" -> R.drawable.icon_bus_135
        "136" -> R.drawable.icon_bus_136
        "137" -> R.drawable.icon_bus_137
        "139" -> R.drawable.icon_bus_139
        "140" -> R.drawable.icon_bus_140
        "142" -> R.drawable.icon_bus_142
        "143" -> R.drawable.icon_bus_143
        "147" -> R.drawable.icon_bus_147
        "150" -> R.drawable.icon_bus_150
        "154" -> R.drawable.icon_bus_154
        "155" -> R.drawable.icon_bus_155
        "156" -> R.drawable.icon_bus_156
        "158" -> R.drawable.icon_bus_158
        "160" -> R.drawable.icon_bus_160
        "161" -> R.drawable.icon_bus_161
        "162" -> R.drawable.icon_bus_162
        "163" -> R.drawable.icon_bus_163
        "164" -> R.drawable.icon_bus_164
        "165" -> R.drawable.icon_bus_165
        "166" -> R.drawable.icon_bus_166
        "168" -> R.drawable.icon_bus_168
        "169" -> R.drawable.icon_bus_169
        "170" -> R.drawable.icon_bus_170
        "171" -> R.drawable.icon_bus_171
        "172" -> R.drawable.icon_bus_172
        "175" -> R.drawable.icon_bus_175
        "179" -> R.drawable.icon_bus_179
        "181" -> R.drawable.icon_bus_181
        "184" -> R.drawable.icon_bus_184
        "186" -> R.drawable.icon_bus_186
        "187" -> R.drawable.icon_bus_187
        "188" -> R.drawable.icon_bus_188
        "190" -> R.drawable.icon_bus_190
        "191" -> R.drawable.icon_bus_191
        "192" -> R.drawable.icon_bus_192
        "194" -> R.drawable.icon_bus_194
        "195" -> R.drawable.icon_bus_195
        "197" -> R.drawable.icon_bus_197
        "200" -> R.drawable.icon_bus_200
        "204" -> R.drawable.icon_bus_204
        "218" -> R.drawable.icon_bus_218
        "220" -> R.drawable.icon_bus_220
        "221" -> R.drawable.icon_bus_221
        "222" -> R.drawable.icon_bus_222
        "234" -> R.drawable.icon_bus_234
        "237" -> R.drawable.icon_bus_237
        "240" -> R.drawable.icon_bus_240
        "245" -> R.drawable.icon_bus_245
        "246" -> R.drawable.icon_bus_246
        "247" -> R.drawable.icon_bus_247
        "248" -> R.drawable.icon_bus_248
        "249" -> R.drawable.icon_bus_249
        "250" -> R.drawable.icon_bus_250
        "255" -> R.drawable.icon_bus_255
        "256" -> R.drawable.icon_bus_256
        "259" -> R.drawable.icon_bus_259
        "260" -> R.drawable.icon_bus_260
        "263" -> R.drawable.icon_bus_263
        "265" -> R.drawable.icon_bus_265
        "269" -> R.drawable.icon_bus_269
        "271" -> R.drawable.icon_bus_271
        "275" -> R.drawable.icon_bus_275
        "277" -> R.drawable.icon_bus_277
        "282" -> R.drawable.icon_bus_282
        "283" -> R.drawable.icon_bus_283
        "284" -> R.drawable.icon_bus_284
        "285" -> R.drawable.icon_bus_285
        "291" -> R.drawable.icon_bus_291
        "294" -> R.drawable.icon_bus_294
        "296" -> R.drawable.icon_bus_296
        "300" -> R.drawable.icon_bus_300
        "309" -> R.drawable.icon_bus_309
        "310" -> R.drawable.icon_bus_310
        "316" -> R.drawable.icon_bus_316
        "318" -> R.drawable.icon_bus_318
        "320" -> R.drawable.icon_bus_320
        "322" -> R.drawable.icon_bus_322
        "324" -> R.drawable.icon_bus_324
        "326" -> R.drawable.icon_bus_326
        "327" -> R.drawable.icon_bus_327
        "334" -> R.drawable.icon_bus_334
        "337" -> R.drawable.icon_bus_337
        "338" -> R.drawable.icon_bus_338
        "347" -> R.drawable.icon_bus_347
        "349" -> R.drawable.icon_bus_349
        "350" -> R.drawable.icon_bus_350
        "353" -> R.drawable.icon_bus_353
        "363" -> R.drawable.icon_bus_363
        "365" -> R.drawable.icon_bus_365
        "369" -> R.drawable.icon_bus_369
        "371" -> R.drawable.icon_bus_371
        "372" -> R.drawable.icon_bus_372
        "377" -> R.drawable.icon_bus_377
        "380" -> R.drawable.icon_bus_380
        "390" -> R.drawable.icon_bus_390
        "395" -> R.drawable.icon_bus_395
        "396" -> R.drawable.icon_bus_396
        "398" -> R.drawable.icon_bus_398
        "399" -> R.drawable.icon_bus_399
        "620" -> R.drawable.icon_bus_620
        "622" -> R.drawable.icon_bus_622
        "623" -> R.drawable.icon_bus_623
        "638" -> R.drawable.icon_bus_638
        "671" -> R.drawable.icon_bus_671
        "697" -> R.drawable.icon_bus_697
        "710" -> R.drawable.icon_bus_710
        "711" -> R.drawable.icon_bus_711
        "733" -> R.drawable.icon_bus_733
        "734" -> R.drawable.icon_bus_734
        "735" -> R.drawable.icon_bus_735
        "736" -> R.drawable.icon_bus_736
        "743" -> R.drawable.icon_bus_743
        "744" -> R.drawable.icon_bus_744
        "806" -> R.drawable.icon_bus_806
        "809" -> R.drawable.icon_bus_809
        "893" -> R.drawable.icon_bus_893
        "935" -> R.drawable.icon_bus_935
        "941" -> R.drawable.icon_bus_941
        "943" -> R.drawable.icon_bus_943
        "N1" -> R.drawable.icon_bus_n1
        "N2" -> R.drawable.icon_bus_n2
        "N3" -> R.drawable.icon_bus_n3
        "N5" -> R.drawable.icon_bus_n5
        "N6" -> R.drawable.icon_bus_n6
        "N7" -> R.drawable.icon_bus_n7
        "N7X" -> R.drawable.icon_bus_n7x
        "N8" -> R.drawable.icon_bus_n8
        "N9" -> R.drawable.icon_bus_n9
        "N10" -> R.drawable.icon_bus_n10
        "N12" -> R.drawable.icon_bus_n12
        "N16" -> R.drawable.icon_bus_n16
        "N18" -> R.drawable.icon_bus_n18
        "N20" -> R.drawable.icon_bus_n20
        "N22" -> R.drawable.icon_bus_n22
        "N23" -> R.drawable.icon_bus_n23
        "N24" -> R.drawable.icon_bus_n24
        "N25" -> R.drawable.icon_bus_n25
        "N26" -> R.drawable.icon_bus_n26
        "N30" -> R.drawable.icon_bus_n30
        "N33" -> R.drawable.icon_bus_n33
        "N34" -> R.drawable.icon_bus_n34
        "N35" -> R.drawable.icon_bus_n35
        "N36" -> R.drawable.icon_bus_n36
        "N39" -> R.drawable.icon_bus_n39
        "N40" -> R.drawable.icon_bus_n40
        "N42" -> R.drawable.icon_bus_n42
        "N43" -> R.drawable.icon_bus_n43
        "N50" -> R.drawable.icon_bus_n50
        "N52" -> R.drawable.icon_bus_n52
        "N53" -> R.drawable.icon_bus_n53
        "N56" -> R.drawable.icon_bus_n56
        "N58" -> R.drawable.icon_bus_n58
        "N60" -> R.drawable.icon_bus_n60
        "N61" -> R.drawable.icon_bus_n61
        "N62" -> R.drawable.icon_bus_n62
        "N64" -> R.drawable.icon_bus_n64
        "N65" -> R.drawable.icon_bus_n65
        "N68" -> R.drawable.icon_bus_n68
        "N69" -> R.drawable.icon_bus_n69
        "N70" -> R.drawable.icon_bus_n70
        "N77" -> R.drawable.icon_bus_n77
        "N81" -> R.drawable.icon_bus_n81
        "N84" -> R.drawable.icon_bus_n84
        "N88" -> R.drawable.icon_bus_n88
        "N90" -> R.drawable.icon_bus_n90
        "N91" -> R.drawable.icon_bus_n91
        "N94" -> R.drawable.icon_bus_n94
        "N95" -> R.drawable.icon_bus_n95
        "N96" -> R.drawable.icon_bus_n96
        "N97" -> R.drawable.icon_bus_n97
        else -> R.drawable.icon_error
    }
}

fun getDeviceTimeZone(): String {
    val timeZone = TimeZone.getDefault()
    val timeZoneId = timeZone.id // Time Zone ID, e.g., "America/New_York"
    val timeZoneDisplayName = timeZone.displayName // Display name, e.g., "Eastern Standard Time"

    return "Time Zone ID: $timeZoneId, Display Name: $timeZoneDisplayName"
}