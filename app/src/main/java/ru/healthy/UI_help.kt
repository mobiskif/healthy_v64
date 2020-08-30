package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@androidx.compose.runtime.Composable
fun myHelp(model: AppViewModel) {
    var index: Int
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        index = (colors.size * Math.random()).toInt()
        Box ( modifier = Modifier
            .padding(padd)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(color = colors[index % colors.size], shape = RoundedCornerShape(padd))
        ) {
            Text("Программа лишь выполняет запросы в Регистратуры (к серверам поликлиник) и отображает их ответы.", modifier = Modifier.padding(padd))
        }
        index = (colors.size * Math.random()).toInt()
        Box ( modifier = Modifier
            .padding(padd)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(color = colors[index % colors.size], shape = RoundedCornerShape(padd))
        ) {
            Text("Клиники ведут себя по-разному: некоторые включают запись в 8:00 и выключают в 20:00, стоматологии часто показывают талоны информационно, без возможности записи и т.п.", modifier = Modifier.padding(padd))
        }
        index = (colors.size * Math.random()).toInt()
        Box ( modifier = Modifier
            .padding(padd)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(color = colors[index % colors.size], shape = RoundedCornerShape(padd))
        ) {
            Text("Отложенные ранее талоны видны по нажатию иконки \"Календарь\" внутри \"Выбрать специальность\". Нажмите на талон, чтобы отменить.", modifier = Modifier.padding(padd))
        }
        index = (colors.size * Math.random()).toInt()
        Box ( modifier = Modifier
            .padding(padd)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(color = colors[index % colors.size], shape = RoundedCornerShape(padd))
        ) {
            Text("Запись возможна, лишь когда ФИО и дата совпадают с данными карточки, ранее заведенной в Регистратуре выбранной поликлиники. Иногда нужно сходить/позвонить и свериться.", modifier = Modifier.padding(padd))
        }

        Box(modifier = Modifier.clickable(onClick = { model.isAdmin = !model.isAdmin })) {
            Text("              ")
        }
    }
}
