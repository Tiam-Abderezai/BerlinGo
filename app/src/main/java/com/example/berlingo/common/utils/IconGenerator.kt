package com.example.berlingo.common.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getLineNameIcon(lineName: String) {
    when (lineName[0]) {
        'S' -> DrawSBahnIcon(lineName)
//        'R' -> drawRegioIcon()
    }
//        "S1" -> R.drawable.icon_sbahn_s1
//        "S2" -> R.drawable.icon_sbahn_s2
//        "S3" -> R.drawable.icon_sbahn_s3
//        "S5" -> R.drawable.icon_sbahn_s5
//        "S7" -> R.drawable.icon_sbahn_s7
//        "S8" -> R.drawable.icon_sbahn_s8
//        "S9" -> R.drawable.icon_sbahn_s9
//        "S25" -> R.drawable.icon_sbahn_s25
//        "S26" -> R.drawable.icon_sbahn_s26
//        "S41" -> R.drawable.icon_sbahn_s41
//        "S42" -> R.drawable.icon_sbahn_s42
//        "S45" -> R.drawable.icon_sbahn_s45
//        "S46" -> R.drawable.icon_sbahn_s46
//        "S47" -> R.drawable.icon_sbahn_s47
//        "S75" -> R.drawable.icon_sbahn_s75
//        "S85" -> R.drawable.icon_sbahn_s85
//        "RE3" -> R.drawable.icon_rebahn_re3
//        "RE4" -> R.drawable.icon_rebahn_re4
//        "RE8" -> R.drawable.icon_rebahn_re8
//        "RB14" -> R.drawable.icon_rebahn_rb14
// //        "184" -> R.drawable.icon_rebahn_rb14
// //        "S" -> R.drawable.icon_sbahn_s4
// //        "S" -> R.drawable.icon_sbahn_s4
// //        "S" -> R.drawable.icon_sbahn_s4
// //        "S" -> R.drawable.icon_sbahn_s4
//        else -> R.drawable.icon_search
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DrawSBahnIcon(lineName: String) {
    Box(
        modifier = Modifier
            .width(32.dp)
            .height(16.dp)
            .aspectRatio(1.8f),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawOval(
                color = Color.Green,
                style = Fill,
            )
        }
        Text(
            text = lineName,
            color = Color.Black,
            fontSize = 8.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}
