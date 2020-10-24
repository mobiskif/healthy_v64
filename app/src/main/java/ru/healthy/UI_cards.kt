package ru.healthy

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
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
        Text(
            "${it["NameSpesiality"]} ${it["VisitEnd"]} ${it["VisitStart"]}",
            modifier = tmod,
            style = tstyle
        )
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
    if (!model.talonList.value.isNullOrEmpty())
        LazyColumnFor(model.talonList.value!!) { it ->
            //model.talonList.value?.forEach { it ->
            val onclick = {
                var usr = model.current_usr.toMutableMap()
                usr["IdAppointment"] = it["IdAppointment"].toString()
                usr["VisitStart"] = it["VisitStart"].toString()
                usr["VisitEnd"] = it["VisitEnd"].toString()
                model.current_usr = usr
                model.current_state.postValue("Взять талон")
            }
            Box(modifier = mod_card(onclick)) {
                Column(modifier = Modifier.padding(padd)) {
                    myTalonFields(it)
                }
            }
        }
}

@Composable
fun myDoctorCardBox(model: AppViewModel) {
    if (!model.doctorList.value.isNullOrEmpty())
        LazyColumnFor(model.doctorList.value!!) { it ->
            //model.doctorList.value?.forEach { it ->
            val onclick = {
                var usr = model.current_usr.toMutableMap()
                usr["IdDoc"] = it["IdDoc"].toString()
                usr["DoctorName"] = it["Name"].toString()
                model.current_usr = usr
                model.readTalonList()
                model.current_state.postValue("Выбрать талон")
            }
            Box(modifier = mod_card(onclick)) {
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
    if (!model.historyList.value.isNullOrEmpty())
        LazyColumnFor(model.historyList.value!!) { it ->
            //model.historyList.value?.forEach { it ->
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
            Box(modifier = mod_card(onclick)) {
                Column(modifier = Modifier.padding(padd)) {
                    myTalonFields(it)
                }
            }
        }
}

@Composable
fun mySpecCardBox(model: AppViewModel) {
    val item = model.lpuInfo.value
    Text(
        "${item?.get("Address")} ${item?.get("Phone")} ${item?.get("Email")}",
        modifier = Modifier.padding(padd)
    )
    if (!model.specList.value.isNullOrEmpty())
        LazyColumnFor(model.specList.value!!) { it ->
            //model.specList.value?.forEach { it ->
            //val index = (colors.size * Math.random()).toInt()
            val onclick = {
                var usr = model.current_usr.toMutableMap()
                usr["IdSpesiality"] = it["IdSpesiality"].toString()
                usr["NameSpesiality"] = it["NameSpesiality"].toString()
                model.current_usr = usr
                model.readDocList()
                model.current_state.postValue("Выбрать врача")
            }
            Box(modifier = mod_card(onclick)) {
                Column(modifier = Modifier.padding(padd)) {
                    if ("${it["ErrorDescription"]}".length > 4) {
                        Text("${it["ErrorDescription"]}", modifier = tmod, style = tstyle)
                    } else {
                        Text("${it["NameSpesiality"]}", modifier = tmod, style = tstyle)
                        Text(
                            "Талонов: ${it["CountFreeParticipantIE"]}",
                            modifier = tmod,
                            style = tstyle
                        )
                        Text("Резерв: ${it["CountFreeTicket"]}", modifier = tmod)
                    }
                }
            }
        }
}


/*
val mod_card = Modifier
    .padding(8.dp)
    .fillMaxWidth()
    //.wrapContentWidth(Alignment.CenterHorizontally)
    //.background(color = Color.Green, shape = shapes.small)
    .border(1.dp, Color.LightGray, shapes.small)
 */

@Composable
fun myText(text: String) {
    Text(text, modifier = tmod, style = tstyle)
}


@Composable
fun myLpuCardBox(model: AppViewModel) {
    if (!model.lpuList.value.isNullOrEmpty())
        LazyColumnFor(model.lpuList.value!!) { it ->
            var onclick = {
                var usr = model.current_usr.toMutableMap()
                usr["iL"] = it["IdLPU"].toString()
                usr["L"] = it["Name"].toString()
                model.current_usr = usr
                model.readPatID()
                model.current_state.postValue("Проверка пациента")
            }
            Box(modifier = mod_card(onclick)) {
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
    if (!model.usrList.value.isNullOrEmpty())
        LazyColumnFor(model.usrList.value!!) {
            var onclick = {
                model.current_usr = it
                model.readLpuList()
                model.current_state.postValue("Выбрать клинику")
            }
            Box(modifier = mod_card(onclick)) {
                Column(modifier = Modifier.padding(padd)) {
                    Text("${it["F"]} ${it["I"]} ${it["O"]}")
                    Text("Дата рождения: ${it["D"]}")
                    Text("Район: ${it["R"]}")
                    IconButton(onClick = {
                        model.current_usr = it
                        model.current_state.postValue("Изменить")
                    }) { Icon(Icons.Filled.Edit, tint = Color.LightGray) }
                }
            }
        }

}

@Composable
fun myUsrCardBox2(model: AppViewModel) {
    if (!model.usrList.value.isNullOrEmpty())
        LazyColumnFor(model.usrList.value!!) {
            var onclick = {
                model.current_usr = it
                model.readLpuList()
                model.current_state.postValue("Выбрать клинику")
            }
            Box(modifier = mod_card(onclick)) {
                Column(modifier = mod_padd) {
                    Text("${it["F"]} ${it["I"]} ${it["O"]}", style = typography.body1)
                    /*-- myFieldList() --*/
                    val mod_col = Modifier.background( color = MaterialTheme.colors.secondary, shape = shapes.small )
                    Box(modifier = mod_list()) {
                        Row {
                            Column {
                                IconButton(onClick = {
                                    model.current_usr = it
                                    model.current_state.postValue("Изменить")
                                }) { Icon(Icons.Filled.Edit, tint = Color.LightGray) }
                            }
                            Column(modifier = mod_padd) {
                                Text("Дата рождения: ${it["D"]}", style = typography.body2)
                                Text("Район: ${it["R"]}", style = typography.body2)
                            }
                        }
                    }
                    /*---*/
                }
            }
        }
}

@Composable
fun myTalonGetCardBox(model: AppViewModel) {
    Box(modifier = mod_card({})) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Взять талон") }, onClick = {
                    model.getTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(
                    content = { Text("Назад") },
                    onClick = { model.current_state.postValue("Выбрать талон") })
            }
        }
    }
}

@Composable
fun myTalonDelCardBox(model: AppViewModel) {
    Box(modifier = mod_card({})) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.current_usr)
            Row {
                Button(content = { Text("Отменить талон") }, onClick = {
                    model.delTalon()
                    model.current_state.postValue("Отложенные талоны")
                })
                TextButton(
                    content = { Text("Назад") },
                    onClick = { model.current_state.postValue("Отложенные талоны") })
            }
        }
    }
}

