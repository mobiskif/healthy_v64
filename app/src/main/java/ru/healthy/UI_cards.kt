package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
fun myUsrReadFields(item: Map<String, String>) {
    Text("${item["F"]} ${item["I"]} ${item["O"]}", modifier = tmod, style = tstyle)
    Text("Дата рождения: ${item["D"]}", modifier = tmod, style = tstyle)
    Text("Район: ${item["R"]}", modifier = tmod, style = tstyle)
    //Text("${item["id"]}", modifier = tmod)
}

@Composable
fun myLpuReadFields(item: Map<String, String>) {
    if ("${item["ErrorDescription"]}".length>4) {
        Text("${item["ErrorDescription"]}", modifier = tmod, style = tstyle)
    } else {
        Text("${item["Name"]}", modifier = tmod, style = tstyle)
        Text("Система: ${item["Description"]}", modifier = tmod)
    }
}

@Composable
fun mySpecFields(item: Map<String, String>) {
    if ("${item["ErrorDescription"]}".length>4) {
        Text("${item["ErrorDescription"]}", modifier = tmod, style = tstyle)
    } else {
        Text("${item["NameSpesiality"]}", modifier = tmod, style = tstyle)
        Text("Талонов: ${item["CountFreeParticipantIE"]}", modifier = tmod, style = tstyle)
        Text("Резерв: ${item["CountFreeTicket"]}", modifier = tmod)
    }
}

@Composable
fun myDoctorFields(item: Map<String, String>) {
    if ("${item["ErrorDescription"]}".length>4) {
        Text("${item["ErrorDescription"]}", modifier = tmod, style = tstyle)
    } else {
        Text("${item["Name"]}", modifier = tmod, style = tstyle)
        Text("Талонов: ${item["CountFreeTicket"]}", modifier = tmod, style = tstyle)
        Text("Участок: ${item["AriaNumber"]}", modifier = tmod)
        //Text("${item["NearestDate"]}".dropLast(9), modifier = tmod)
        //Text("${item["LastDate"]}".dropLast(9), modifier = tmod)
    }
}

@Composable
fun myTalonFields(item: Map<String, String>) {
    if ("${item["ErrorDescription"]}".length>4) {
        Text("${item["ErrorDescription"]}", modifier = tmod, style = tstyle)
    } else {
        Text("Талон № ${item["IdAppointment"]}")
        Text ("${if (!item["Name"].isNullOrEmpty()) item["Name"] else "" }")
        Text("${item["NameSpesiality"]} ${item["VisitEnd"]} ${item["VisitStart"]}", modifier = tmod, style = tstyle)
        //if (!item["Name"].isNullOrEmpty()) Text("${item["Name"]}", modifier = tmod, style = tstyle)
    }
}

@Composable
fun my10UsrEditFields(model: AppViewModel) {
    val estyle = TextStyle(fontSize = 18.sp)
    var F by state { TextFieldValue("${model.current_usr["F"]}") }
    myDistrictSpinner(model, model.current_usr, estyle)
    myLPUSpinner(model, model.current_usr, estyle)
    TextField (
        textStyle = estyle,
        value = F,
        onValueChange = { F = it},
        label = { Text("Фамилия") }
        ,modifier = Modifier.padding(0.dp, padd/2)
    )
    TextButton( content={Text("Найти")}, onClick = {
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

    TextField(
        textStyle = estyle,
        value = F,
        onValueChange = { F = it},
        label = { Text("Фамилия") }
        ,modifier = Modifier.padding(0.dp, padd/2)
    )
    TextField(
        textStyle = estyle,
        value = I,
        onValueChange = { I = it },
        label = { Text("Имя") }
        ,modifier = Modifier.padding(0.dp, padd/2)
    )
    TextField(
        textStyle = estyle,
        value = O,
        onValueChange = { O = it },
        label = { Text("Отчество") }
        ,modifier = Modifier.padding(0.dp, padd/2)
    )
    TextField(
        textStyle = estyle,
        value = D,
        onValueChange = { D = it},
        label = { Text("Дата рождения") },
        placeholder = { Text(text = "1986-04-26") }
        ,modifier = Modifier.padding(0.dp, padd/2)
    )
    Spacer(modifier = Modifier.preferredHeightIn(padd/2))
    myDistrictSpinner(model, model.current_usr, estyle)
    Spacer(modifier = Modifier.preferredHeightIn(padd*2))
    Row {
        Button(content={ Text("Сохранить") }, onClick = {
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
        TextButton(content={ Text("Удалить") }, onClick = {
            model.deleteCurrentUser()
            model.current_state.postValue("Выбрать пациента")
        })
    }

}

@Composable
fun myUsrEditCardBox(model: AppViewModel) {
    /*
    val index = (colors.size * Math.random()).toInt()
    Box(
        modifier = Modifier.padding(padd).fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally).background(
                color = colors[index % colors.size], shape = RoundedCornerShape(padd)
            )
    ) {
        Column(modifier = Modifier.padding(padd)) {
            myUsrEditFields(model)
        }
    }
     */
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        myUsrEditFields(model)
    }
}

@Composable
fun my10UsrEditCardBox(model: AppViewModel) {
    val index = (colors.size * Math.random()).toInt()
    Box(
        modifier = Modifier.padding(padd).fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally).background(
                color = colors[index % colors.size], shape = RoundedCornerShape(padd)
            )
    ) {
        Column(modifier = Modifier.padding(padd)) {
            my10UsrEditFields(model)
        }
    }
}

@Composable
fun myTalonCardBox(model: AppViewModel) {
    model.talonList.value?.forEach { it ->
        val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = colors[index % colors.size], shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    var usr = model.current_usr.toMutableMap()
                    usr["IdAppointment"] = it["IdAppointment"].toString()
                    usr["VisitStart"] = it["VisitStart"].toString()
                    usr["VisitEnd"] = it["VisitEnd"].toString()
                    model.current_usr = usr
                    model.current_state.postValue("Взять талон")
                })
        ) {
            Column(modifier = Modifier.padding(padd)) {
                myTalonFields(it)
            }
        }
    }
}

@Composable
fun myDoctorCardBox(model: AppViewModel) {
    model.doctorList.value?.forEach { it ->
        val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = colors[index % colors.size], shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    var usr = model.current_usr.toMutableMap()
                    usr["IdDoc"] = it["IdDoc"].toString()
                    usr["DoctorName"] = it["Name"].toString()
                    model.current_usr = usr
                    model.readTalonList()
                    model.current_state.postValue("Выбрать талон")
                })
        ) {
            Column(modifier = Modifier.padding(padd)) {
                myDoctorFields(it)
            }
        }
    }
}

@Composable
fun myHistCardBox(model: AppViewModel) {
    Text("Отложенные в ${model.current_usr["L"]} талоны:", modifier = Modifier.padding(padd))
    model.historyList.value?.forEach { it ->
        //val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = Color.Green, shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    var usr = model.current_usr.toMutableMap()
                    usr["IdAppointment"] = it["IdAppointment"].toString()
                    usr["VisitStart"] = it["VisitStart"].toString()
                    usr["VisitEnd"] = it["VisitEnd"].toString()
                    usr["Name"] = it["Name"].toString()
                    usr["NameSpesiality"] = it["NameSpesiality"].toString()
                    model.current_usr = usr
                    model.current_state.postValue("Отменить талон")
                })
        ) {
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
        val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = colors[index % colors.size], shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    var usr = model.current_usr.toMutableMap()
                    usr["IdSpesiality"] = it["IdSpesiality"].toString()
                    usr["NameSpesiality"] = it["NameSpesiality"].toString()
                    model.current_usr = usr
                    model.readDocList()
                    model.current_state.postValue("Выбрать врача")
                })
        ) {
            Column(modifier = Modifier.padding(padd)) {
                mySpecFields(it)
            }
        }
    }
}

@Composable
fun myLpuCardBox(model: AppViewModel) {
    model.lpuList.value?.forEach { it ->
        val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = colors[index % colors.size], shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    var usr = model.current_usr.toMutableMap()
                    usr["iL"] = it["IdLPU"].toString()
                    usr["L"] = it["Name"].toString()
                    model.current_usr = usr
                    model.readPatID()
                    model.current_state.postValue("Проверка пациента")
                })
        ) {
            Column(modifier = Modifier.padding(padd)) {
                myLpuReadFields(it)
            }
        }
    }
}

@Composable
fun myUsrCardBox(model: AppViewModel) {
    model.usrList.value?.forEach { it ->
        val index = (colors.size * Math.random()).toInt()
        Box(
            modifier = Modifier.padding(padd).fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally).background(
                    color = colors[index % colors.size], shape = RoundedCornerShape(padd)
                ).clickable(onClick = {
                    model.current_usr = it
                    model.readLpuList()
                    model.current_state.postValue("Выбрать клинику")
                })
        ) {
            Column(modifier = Modifier.padding(padd)) {
                myUsrReadFields(it)
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
    Box(
        modifier = Modifier.padding(padd).fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally).background(
                color = Color.Green, shape = RoundedCornerShape(padd)
            )
    ) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Взять талон") }, onClick = {
                    model.getTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(content = { Text("Назад") }, onClick = { model.current_state.postValue("Выбрать талон")})
            }
        }
    }
}

@Composable
fun myTalonDelCardBox(model: AppViewModel) {
    Box(
        modifier = Modifier.padding(padd).fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally).background(
                color = Color.Red, shape = RoundedCornerShape(padd)
            )
    ) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Отменить талон") }, onClick = {
                    model.delTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(content = { Text("Назад") }, onClick = { model.current_state.postValue("Отложенные талоны")})
            }
        }
    }
}

