package ru.healthy

//import androidx.compose.foundation.*
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.state
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun myTalonFields(it: Map<String, String>) {
    Column(modifier = mod_padd) {
        Text("Талон: ${it["IdAppointment"]}", style = typography.body1)
        Box(modifier = mod_list().then(Modifier.wrapContentSize())) {
            Row {
                Column(modifier = mod_padd) {
                    Text("${it["NameSpesiality"]} \nДата: ${it["VisitEnd"]} \nВремя: ${it["VisitStart"]}", style = typography.body2)
                    if ("${it["ErrorDescription"]}".length > 4) Text("${it["ErrorDescription"]}", style = typography.body2)
                }
                Column (modifier = mod_padd.then(mod_back())) {
                    Box (modifier = mod_padd.then(Modifier.align(Alignment.End))) {
                        Text("${it["VisitStart"]}", style = typography.h4)
                    }
                }
            }
        }
    }
}

@Composable
fun myUsrEditCardBox(model: myViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        val estyle = TextStyle(fontSize = 18.sp)
        var F by state { TextFieldValue("${model.current_usr["F"]}") }
        var I by state { TextFieldValue("${model.current_usr["I"]}") }
        var O by state { TextFieldValue("${model.current_usr["O"]}") }
        var D by state { TextFieldValue("${model.current_usr["D"]}") }
        var R by state { TextFieldValue("${model.current_usr["R"]}") }

        Box(modifier = mod_card {}) {
            Column(modifier = mod_padd) {
                //Text("$F $I $O", style = typography.body1)
                Box(modifier = mod_list()) {
                    Row {
                        Column(modifier = mod_padd) {
                            Row {
                                Button(content = { Text("Сохранить") }, onClick = {
                                    var usr = model.current_usr.toMutableMap()
                                    usr["F"] = F.text
                                    usr["I"] = I.text
                                    usr["O"] = O.text
                                    usr["D"] = D.text
                                    model.current_usr = usr
                                    model.updateUserInList()
                                    //model.readLpuList()
                                    model.current_state.postValue("Выбрать пациента")
                                })
                                TextButton(content = { Text("Удалить") }, onClick = {
                                    model.deleteCurrentUser()
                                    model.current_state.postValue("Выбрать пациента")
                                })
                            }
                            Spacer(modifier = Modifier.preferredHeightIn(padd))
                            TextField(
                                //textStyle = estyle,
                                value = F,
                                onValueChange = { F = it },
                                label = { Text("Фамилия") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = I,
                                onValueChange = { I = it },
                                label = { Text("Имя") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = O,
                                onValueChange = { O = it },
                                label = { Text("Отчество") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = D,
                                onValueChange = { D = it },
                                label = { Text("Дата рождения") },
                                placeholder = { Text(text = "1986-04-26") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            Spacer(modifier = Modifier.preferredHeightIn(padd))
                            myDistrictSpinner(model, model.current_usr, estyle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun my10UsrEditCardBox(model: myViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
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
}

@Composable
fun myTalonCardBox(model: myViewModel) {
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
                myTalonFields(it)
            }
        }
}

@Composable
fun myDoctorCardBox(model: myViewModel) {
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
                Column(modifier = mod_padd) {
                    Text("${it["Name"]}", style = typography.body1)
                    Box(modifier = mod_list()) {
                        Row {
                            Column(modifier = mod_padd) {
                                Text("Талонов: ${it["CountFreeTicket"]}", style = typography.body2)
                                Text("Участок: ${it["AriaNumber"]}", style = typography.body2)
                                if ("${it["ErrorDescription"]}".length > 4) Text("${it["ErrorDescription"]}", style = typography.body2)
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun myHistCardBox(model: myViewModel) {
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
                myTalonFields(it)
            }
        }
}

@Composable
fun mySpecCardBox(model: myViewModel) {
    //val item = model.lpuInfo.value
    //Text("${item?.get("Address")} ${item?.get("Phone")} ${item?.get("Email")}", modifier = Modifier.padding(padd) )
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
                Column(modifier = mod_padd) {
                    Text("${it["NameSpesiality"]}", style = typography.body1)
                    Box(modifier = mod_list()) {
                        Row {
                            Column(modifier = mod_padd) {
                                Text("Талонов: ${it["CountFreeParticipantIE"]}", style = typography.body2)
                                Text("Резерв: ${it["CountFreeTicket"]}", style = typography.body2)
                                if ("${it["ErrorDescription"]}".length > 4) Text(
                                    "${it["ErrorDescription"]}",
                                    style = typography.body2
                                )
                            }
                        }
                    }
                }
            }
        }
}


@Composable
fun myLpuCardBox(model: myViewModel) {
    if (!model.lpuList.value.isNullOrEmpty()) {
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
                Column(modifier = mod_padd) {
                    Text("${it["Name"]}", style = typography.body1)
                    Box(modifier = mod_list()) {
                        Row {
                            Column(modifier = mod_padd) {
                                Text("МИС: ${it["Description"]}", style = typography.body2)
                                if ("${it["ErrorDescription"]}".length > 4) Text(
                                    "${it["ErrorDescription"]}",
                                    style = typography.body2
                                )
                            }
                            Column(modifier = mod_padd) {
                                Text("${it["Address"]}", style = typography.body2)
                                Text("${it["Email"]}", style = typography.body2)
                                Text("${it["Phone"]}", style = typography.body2)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun myUsrCardBox(model: myViewModel) {
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
                    Box(modifier = mod_list()) {
                        Row {
                            Column {
                                IconButton(onClick = {
                                    model.current_usr = it
                                    model.current_state.postValue("Изменить")
                                }) { Icon(Icons.Filled.Edit, tint = MaterialTheme.colors.secondary) }
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
fun myTalonGetCardBox(model: myViewModel) {
    Box(modifier = mod_card {}) {
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
fun myTalonDelCardBox(model: myViewModel) {
    Box(modifier = mod_card {}) {
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

