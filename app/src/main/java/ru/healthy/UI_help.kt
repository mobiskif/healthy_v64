package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.Box
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier

@androidx.compose.runtime.Composable
fun myHelp(model: myViewModel) {
    ScrollableColumn {
        androidx.compose.foundation.layout.Box(modifier = mod_card {}) {
            Column(modifier = mod_padd) {
                //Text("Как это работает", style = typography.body1)
                androidx.compose.foundation.layout.Box(modifier = mod_list()) {
                    Row {
                        Column(modifier = mod_padd) {
                            Text("Программа лишь выполняет запросы в Регистратуры (к серверам поликлиник) и отображает их ответы.", style = typography.body2)
                            Text("\nКлиники ведут себя по-разному: некоторые включают запись в 8:00 и выключают в 20:00, стоматологии часто показывают талоны информационно, без возможности записи и т.п.", style = typography.body2)
                            Text("\nОтложенные ранее талоны видны по нажатию иконки \"Календарь\" внутри \"Выбрать специальность\". Нажмите на талон, чтобы отменить.", style = typography.body2)
                            Text("\nЗапись возможна, лишь когда ФИО и дата совпадают с данными карточки, ранее заведенной в Регистратуре выбранной поликлиники. Иногда нужно сходить/позвонить и свериться.", style = typography.body2)
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.clickable(onClick = { model.isAdmin = !model.isAdmin })) {
            Text("              ")
        }
    }
}
