package ru.healthy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getColor

/*
    body1 body2 button caption
    h1 h2 h3 h4 h5 h6
    overline subtitle1 subtitle2
*/

val padd = 8.dp

//val tmod = Modifier.padding(0.dp, 0.dp, 0.dp, padd)
val tmod = Modifier.padding(padd)
val tstyle = TextStyle(fontSize = 18.sp)
val colors = listOf(
    Color(0xFFffd7d7.toInt()),
    Color(0xFFffe9d6.toInt()),
    Color(0xFFfffbd0.toInt()),
    Color(0xFFe3ffd9.toInt()),
    Color(0xFFd0fff8.toInt())
)

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
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Color.Red
    ),
)

/*
val LightPalette = lightColors(
        //primary = Color(android.graphics.Color.parseColor("#c2185b")),
        primary = Color(ContextCompat.getColor(context!!, R.color.primaryColor)), //цвет Button и подписей
        primaryVariant = Color(ContextCompat.getColor(context!!, R.color.primaryDarkColor)), //цвет StatusBar
        secondary = Color(ContextCompat.getColor(context!!, R.color.secondaryColor)), //цвет FAB и RadioButton
        secondaryVariant = Color(ContextCompat.getColor(context!!, R.color.secondaryDarkColor)), //цвет Switch
        surface = Color(ContextCompat.getColor(context!!, R.color.secondaryLightColor))
        //background = Color(getColor(context!!, R.color.secondaryLightColor)) //цвет полотна приложения
)
*/

val LightPalette = lightColors(
    primary = Color(android.graphics.Color.parseColor("#2e7d32")),
    primaryVariant = Color(android.graphics.Color.parseColor("#005005")), //цвет StatusBar
    secondary = Color(android.graphics.Color.parseColor("#2e7d32")), //цвет FAB и RadioButton
    secondaryVariant = Color(android.graphics.Color.parseColor("#2e7d32")), //цвет Switch
    surface = Color(android.graphics.Color.parseColor("#ff7543"))
)

val DarkPalette = darkColors(
    primary = Color.Gray, //цвет Button и подписей
    primaryVariant = Color.DarkGray, //цвет StatusBar
    secondary = Color.Gray, //цвет FAB и RadioButton
    surface = Color.Gray, //в темной теме цвет ActionBar
    onSurface = Color.LightGray,
    onPrimary = Color.LightGray,
    onSecondary = Color.LightGray,
    onBackground = Color.LightGray
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

val mod_padd = Modifier.padding(8.dp)
@Composable
fun mod_back(): Modifier {
    return Modifier
    .background(color = MaterialTheme.colors.primary, shape = shapes.medium)
}
val mod_bord = Modifier.border(1.dp, Color.Red, shapes.small)

@Composable
fun mod_info(): Modifier {
    return Modifier
    //.padding(8.dp)
    //.fillMaxWidth()
    //.wrapContentWidth(Alignment.CenterHorizontally)
    //.background(color = MaterialTheme.colors.surface, shape = shapes.medium)
    //.border(0.dp, Color.Red, shapes.small)
}

@Composable
fun mod_list(): Modifier {
    return Modifier
        //.padding(8.dp)
        //.fillMaxWidth()
        //.wrapContentWidth(Alignment.CenterHorizontally)
        //.background(color = MaterialTheme.colors.surface, shape = shapes.medium)
        //.border(0.dp, MaterialTheme.colors.primary, shapes.small)
}

@Composable
fun mod_card(oncl: () -> Unit): Modifier {
    return Modifier
        .padding(padd)
        .fillMaxWidth()
        //.wrapContentWidth(Alignment.CenterHorizontally)
        //.background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
        .border(0.dp, MaterialTheme.colors.primary, shapes.small)
        .clickable(onClick = oncl)
}


@Composable
fun myTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (dark) {
            DarkPalette
        } else {
            LightPalette
        },
        typography = typography,
        shapes = shapes,
        content = content
    )
}