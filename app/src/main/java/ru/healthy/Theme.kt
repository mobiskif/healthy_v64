package ru.healthy

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getColor

private val DarkColorPalette = darkColors(
    //primary = primaryColor,
    //primaryVariant = primaryDarkColor,
    //secondary = secondaryColor
)

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

// Set of Material typography styles to start with
val typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
)


fun HexToColor(hex: String): Color {
    var hex = hex
    hex = hex.replace("#", "")
    when (hex.length) {
        6 -> return Color(
            Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16)
        )
        8 -> return Color(
            Integer.valueOf(hex.substring(0, 2), 16),
            Integer.valueOf(hex.substring(2, 4), 16),
            Integer.valueOf(hex.substring(4, 6), 16),
            Integer.valueOf(hex.substring(6, 8), 16)
        )
    }
    return Color(0)
}

private val LightColorPalette = lightColors(
    //primary = Color(android.graphics.Color.parseColor("#c2185b")),
    primary = Color(getColor(cont!!,R.color.primaryColor)), //цвет Button и подписей
    //onPrimary = Color.Yellow,
    primaryVariant = Color(getColor(cont!!,R.color.primaryDarkColor)),
    secondary = Color(getColor(cont!!,R.color.secondaryColor)), //цвет FAB и RadioButton
    //onSecondary = Color.Yellow,
    secondaryVariant = Color(getColor(cont!!,R.color.secondaryDarkColor)), //цвет Switch
    //secondaryVariant = Color(R.attr.colorSecondaryVariant), //цвет Switch

    //background = Color.Red,
    //onBackground = Color.Yellow,
    //surface = Color.Red,
    //onSurface = Color.Yellow,
    //error = Color.Red,
    //onError = Color.Green
)

@Composable
fun Test2Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}