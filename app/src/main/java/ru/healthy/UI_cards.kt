package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun myTalonFields(it: Map<String, String>) {
    if ("${it["ErrorDescription"]}".length > 4) {
        Text("${it["ErrorDescription"]}", modifier = tmod, style = tstyle)
    } else {
        Text("Талон № ${it["IdAppointment"]}")
        Text("${if (!it["Name"].isNullOrEmpty()) it["Name"] else ""}")
        Text("${it["NameSpesiality"]} ${it["VisitEnd"]} ${it["VisitStart"]}", modifier = tmod, style = tstyle)
        //if (!item["Name"].isNullOrEmpty()) Text("${item["Name"]}", modifier = tmod, style = tstyle)
    }
}

@Composable
fun my10UsrEditFields(model: AppViewModel) {
    val estyle = TextStyle(fontSize = 18.sp)
    var F by state { TextFieldValue("${model.current_usr["F"]}") }
    myDistrictSpinner(model, model.current_usr, estyle)
    myLPUSpinner(model, model.current_usr, estyle)
    TextField(
            textStyle = estyle,
            value = F,
            onValueChange = { F = it },
            label = { Text("Фамилия") }, modifier = Modifier.padding(0.dp, padd / 2)
    )
    TextButton(content = { Text("Найти") }, onClick = {
        var usr = model.current_usr.toMutableMap()
        usr["F"] = F.text
        model.current_usr = usr
        model.read10UsrList()
        model.current_state.postValue("Выбрать пациента")
    })

}

@Composable
fun myUsrEditFields(model: AppViewModel) {
    val estyle = TextStyle(fontSize = 18.sp)
    var F by state { TextFieldValue("${model.current_usr["F"]}") }
    var I by state { TextFieldValue("${model.current_usr["I"]}") }
    var O by state { TextFieldValue("${model.current_usr["O"]}") }
    var D by state { TextFieldValue("${model.current_usr["D"]}") }

    Row {
        Button(content = { Text("Сохранить") }, onClick = {
            var usr = model.current_usr.toMutableMap()
            usr["F"] = F.text
            usr["I"] = I.text
            usr["O"] = O.text
            usr["D"] = D.text
            model.current_usr = usr
            model.updateUserInList()
            model.readLpuList()
            model.current_state.postValue("Выбрать клинику")
        })
        TextButton(content = { Text("Удалить") }, onClick = {
            model.deleteCurrentUser()
            model.current_state.postValue("Выбрать пациента")
        })
    }
    Spacer(modifier = Modifier.preferredHeightIn(padd))
    TextField(
            textStyle = estyle,
            value = F,
            onValueChange = { F = it },
            label = { Text("Фамилия") }, modifier = Modifier.padding(0.dp, padd / 2)
    )
    TextField(
            textStyle = estyle,
            value = I,
            onValueChange = { I = it },
            label = { Text("Имя") }, modifier = Modifier.padding(0.dp, padd / 2)
    )
    TextField(
            textStyle = estyle,
            value = O,
            onValueChange = { O = it },
            label = { Text("Отчество") }, modifier = Modifier.padding(0.dp, padd / 2)
    )
    TextField(
            textStyle = estyle,
            value = D,
            onValueChange = { D = it },
            label = { Text("Дата рождения") },
            placeholder = { Text(text = "1986-04-26") }, modifier = Modifier.padding(0.dp, padd / 2)
    )
    Spacer(modifier = Modifier.preferredHeightIn(padd))
    //myDistrictSpinner(model, model.current_usr, estyle)
}

@Composable
fun myUsrEditCardBox(model: AppViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        myUsrEditFields(model)
    }
}

@Composable
fun my10UsrEditCardBox(model: AppViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        my10UsrEditFields(model)
    }
}

@Composable
fun myTalonCardBox(model: AppViewModel) {
    model.talonList.value?.forEach { it ->
        val onclick = {
            var usr = model.current_usr.toMutableMap()
            usr["IdAppointment"] = it["IdAppointment"].toString()
            usr["VisitStart"] = it["VisitStart"].toString()
            usr["VisitEnd"] = it["VisitEnd"].toString()
            model.current_usr = usr
            model.current_state.postValue("Взять талон")
        }
        Box(modifier = myMod(onclick)) {
            Column(modifier = Modifier.padding(padd)) {
                myTalonFields(it)
            }
        }
    }
}

@Composable
fun myDoctorCardBox(model: AppViewModel) {
    model.doctorList.value?.forEach { it ->
        val onclick = {
            var usr = model.current_usr.toMutableMap()
            usr["IdDoc"] = it["IdDoc"].toString()
            usr["DoctorName"] = it["Name"].toString()
            model.current_usr = usr
            model.readTalonList()
            model.current_state.postValue("Выбрать талон")
        }
        Box(modifier = myMod(onclick)) {
            Column(modifier = Modifier.padding(padd)) {
                if ("${it["ErrorDescription"]}".length > 4) {
                    Text("${it["ErrorDescription"]}", modifier = tmod, style = tstyle)
                } else {
                    Text("${it["Name"]}", modifier = tmod, style = tstyle)
                    Text("Талонов: ${it["CountFreeTicket"]}", modifier = tmod, style = tstyle)
                    Text("Участок: ${it["AriaNumber"]}", modifier = tmod)
                    //Text("${item["NearestDate"]}".dropLast(9), modifier = tmod)
                    //Text("${item["LastDate"]}".dropLast(9), modifier = tmod)
                }
            }
        }
    }
}

@Composable
fun myHistCardBox(model: AppViewModel) {
    Text("Отложенные в ${model.current_usr["L"]} талоны:", modifier = Modifier.padding(padd))
    model.historyList.value?.forEach { it ->
        //val index = (colors.size * Math.random()).toInt()
        var onclick = {
            var usr = model.current_usr.toMutableMap()
            usr["IdAppointment"] = it["IdAppointment"].toString()
            usr["VisitStart"] = it["VisitStart"].toString()
            usr["VisitEnd"] = it["VisitEnd"].toString()
            usr["Name"] = it["Name"].toString()
            usr["NameSpesiality"] = it["NameSpesiality"].toString()
            model.current_usr = usr
            model.current_state.postValue("Отменить талон")
        }
        Box(modifier = myMod(onclick)) {
            Column(modifier = Modifier.padding(padd)) {
                myTalonFields(it)
            }
        }
    }
}

@Composable
fun mySpecCardBox(model: AppViewModel) {
    val item = model.lpuInfo.value
    Text("${item?.get("Address")} ${item?.get("Phone")} ${item?.get("Email")}", modifier = Modifier.padding(padd))
    model.specList.value?.forEach { it ->
        //val index = (colors.size * Math.random()).toInt()
        val onclick = {
            var usr = model.current_usr.toMutableMap()
            usr["IdSpesiality"] = it["IdSpesiality"].toString()
            usr["NameSpesiality"] = it["NameSpesiality"].toString()
            model.current_usr = usr
            model.readDocList()
            model.current_state.postValue("Выбрать врача")
        }
        Box(modifier = myMod(onclick)) {
            Column(modifier = Modifier.padding(padd)) {
                if ("${it["ErrorDescription"]}".length > 4) {
                    Text("${it["ErrorDescription"]}", modifier = tmod, style = tstyle)
                } else {
                    Text("${it["NameSpesiality"]}", modifier = tmod, style = tstyle)
                    Text("Талонов: ${it["CountFreeParticipantIE"]}", modifier = tmod, style = tstyle)
                    Text("Резерв: ${it["CountFreeTicket"]}", modifier = tmod)
                }
            }
        }
    }
}

@Composable
fun myMod(oncl: () -> Unit): Modifier {
    return Modifier.padding(padd)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = oncl)
}

@Composable
fun myText(text: String) {
    Text(text, modifier = tmod, style = tstyle)
}


@Composable
fun myLpuCardBox(model: AppViewModel) {
    model.lpuList.value?.forEach { it ->
        var onclick = {
            var usr = model.current_usr.toMutableMap()
            usr["iL"] = it["IdLPU"].toString()
            usr["L"] = it["Name"].toString()
            model.current_usr = usr
            model.readPatID()
            model.current_state.postValue("Проверка пациента")
        }
        Box(modifier = myMod(onclick)) {
            Column(modifier = Modifier.padding(padd)) {
                if ("${it["ErrorDescription"]}".length > 4) {
                    Text("${it["ErrorDescription"]}", modifier = tmod, style = tstyle)
                } else {
                    Text("${it["Name"]}", modifier = tmod, style = tstyle)
                    Text("Система: ${it["Description"]}", modifier = tmod)
                }
            }
        }
    }
}

@Composable
fun myUsrCardBox(model: AppViewModel) {
    var contcol = contentColorFor(color = MaterialTheme.colors.background)
    model.usrList.value?.forEach { it ->
        var onclick = {
            model.current_usr = it
            model.readLpuList()
            model.current_state.postValue("Выбрать клинику")
        }
        Box(modifier = myMod(onclick) ) {
            Column(modifier = Modifier.padding(padd)) {
                myText("${it["F"]} ${it["I"]} ${it["O"]}")
                myText("Дата рождения: ${it["D"]}")
                myText("Район: ${it["R"]}")
                IconButton(onClick = {
                    model.current_usr = it
                    model.current_state.postValue("Изменить")
                }) { Icon(Icons.Filled.Edit, tint = Color.LightGray) }
            }
        }
    }
}

@Composable
fun myTalonGetCardBox(model: AppViewModel) {
    Box(modifier = myMod({})) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Взять талон") }, onClick = {
                    model.getTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(content = { Text("Назад") }, onClick = { model.current_state.postValue("Выбрать талон") })
            }
        }
    }
}

@Composable
fun myTalonDelCardBox(model: AppViewModel) {
    Box(modifier = myMod({})) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Отменить талон") }, onClick = {
                    model.delTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(content = { Text("Назад") }, onClick = { model.current_state.postValue("Отложенные талоны") })
            }
        }
    }
}

