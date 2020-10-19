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

val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp)
)

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
    body1 body2 button caption
    h1 h2 h3 h4 h5 h6
    overline subtitle1 subtitle2
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

private val LightPalette = lightColors(
        //primary = Color(android.graphics.Color.parseColor("#c2185b")),
        primary = Color(getColor(context!!, R.color.primaryColor)), //цвет Button и подписей
        primaryVariant = Color(getColor(context!!, R.color.primaryDarkColor)), //цвет StatusBar
        secondary = Color(getColor(context!!, R.color.secondaryColor)), //цвет FAB и RadioButton
        secondaryVariant = Color(getColor(context!!, R.color.secondaryDarkColor)), //цвет Switch
        surface = Color(getColor(context!!, R.color.secondaryLightColor))
        //background = Color(getColor(context!!, R.color.secondaryLightColor)) //цвет полотна приложения
)

private val DarkPalette = darkColors(
        primary = Color.Gray, //цвет Button и подписей
        primaryVariant = Color.DarkGray, //цвет StatusBar
        secondary = Color.LightGray, //цвет FAB и RadioButton
        surface = Color.LightGray //в темной теме цвет ActionBar
)

@Composable
fun myTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
            colors = if (dark) {DarkPalette} else {LightPalette},
            typography = typography,
            shapes = shapes,
            content = content
    )
}