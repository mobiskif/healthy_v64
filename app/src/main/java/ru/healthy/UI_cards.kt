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
fun myUsrEditCardBox(model: MyViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        val estyle = TextStyle(fontSize = 18.sp)
        var fF by state { TextFieldValue("${model.currentUsr["F"]}") }
        var iI by state { TextFieldValue("${model.currentUsr["I"]}") }
        var oO by state { TextFieldValue("${model.currentUsr["O"]}") }
        var dD by state { TextFieldValue("${model.currentUsr["D"]}") }
        var rR by state { TextFieldValue("${model.currentUsr["R"]}") }

        Box(modifier = mod_card {}) {
            Column(modifier = mod_padd) {
                //Text("$F $I $O", style = typography.body1)
                Box(modifier = mod_list()) {
                    Row {
                        Column(modifier = mod_padd) {
                            Row {
                                Button(content = { Text("Сохранить") }, onClick = {
                                    val usr = model.currentUsr.toMutableMap()
                                    usr["F"] = fF.text
                                    usr["I"] = iI.text
                                    usr["O"] = oO.text
                                    usr["D"] = dD.text
                                    model.currentUsr = usr
                                    model.updateUserInList()
                                    //model.readLpuList()
                                    model.currentState.postValue("Выбрать пациента")
                                })
                                TextButton(content = { Text("Удалить") }, onClick = {
                                    model.deleteCurrentUser()
                                    model.currentState.postValue("Выбрать пациента")
                                })
                            }
                            Spacer(modifier = Modifier.preferredHeightIn(padd))
                            TextField(
                                //textStyle = estyle,
                                value = fF,
                                onValueChange = { fF = it },
                                label = { Text("Фамилия") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = iI,
                                onValueChange = { iI = it },
                                label = { Text("Имя") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = oO,
                                onValueChange = { oO = it },
                                label = { Text("Отчество") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            TextField(
                                //textStyle = estyle,
                                value = dD,
                                onValueChange = { dD = it },
                                label = { Text("Дата рождения") },
                                placeholder = { Text(text = "1986-04-26") }, modifier = Modifier.padding(0.dp, padd / 2)
                            )
                            Spacer(modifier = Modifier.preferredHeightIn(padd))
                            myDistrictSpinner(model, model.currentUsr, estyle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun my10UsrEditCardBox(model: MyViewModel) {
    ScrollableColumn(modifier = Modifier.padding(padd)) {
        val estyle = TextStyle(fontSize = 18.sp)
        var fF by state { TextFieldValue("${model.currentUsr["F"]}") }
        myDistrictSpinner(model, model.currentUsr, estyle)
        myLPUSpinner(model, model.currentUsr, estyle)
        TextField(
            textStyle = estyle,
            value = fF,
            onValueChange = { fF = it },
            label = { Text("Фамилия") }, modifier = Modifier.padding(0.dp, padd / 2)
        )
        TextButton(content = { Text("Найти") }, onClick = {
            val usr = model.currentUsr.toMutableMap()
            usr["F"] = fF.text
            model.currentUsr = usr
            model.read10UsrList()
            model.currentState.postValue("Выбрать пациента")
        })
    }
}

@Composable
fun myTalonCardBox(model: MyViewModel) {
    if (!model.talonList.value.isNullOrEmpty())
        LazyColumnFor(model.talonList.value!!) {
            //model.talonList.value?.forEach { it ->
            val onclick = {
                val usr = model.currentUsr.toMutableMap()
                usr["IdAppointment"] = it["IdAppointment"].toString()
                usr["VisitStart"] = it["VisitStart"].toString()
                usr["VisitEnd"] = it["VisitEnd"].toString()
                model.currentUsr = usr
                model.currentState.postValue("Взять талон")
            }
            Box(modifier = mod_card(onclick)) {
                myTalonFields(it)
            }
        }
}

@Composable
fun myDoctorCardBox(model: MyViewModel) {
    if (!model.doctorList.value.isNullOrEmpty())
        LazyColumnFor(model.doctorList.value!!) {
            //model.doctorList.value?.forEach { it ->
            val onclick = {
                val usr = model.currentUsr.toMutableMap()
                usr["IdDoc"] = it["IdDoc"].toString()
                usr["DoctorName"] = it["Name"].toString()
                model.currentUsr = usr
                model.readTalonList()
                model.currentState.postValue("Выбрать талон")
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
fun myHistCardBox(model: MyViewModel) {
    Text("Отложенные в ${model.currentUsr["L"]} талоны:", modifier = Modifier.padding(padd))
    if (!model.historyList.value.isNullOrEmpty())
        LazyColumnFor(model.historyList.value!!) {
            //model.historyList.value?.forEach { it ->
            //val index = (colors.size * Math.random()).toInt()
            val onclick = {
                val usr = model.currentUsr.toMutableMap()
                usr["IdAppointment"] = it["IdAppointment"].toString()
                usr["VisitStart"] = it["VisitStart"].toString()
                usr["VisitEnd"] = it["VisitEnd"].toString()
                usr["Name"] = it["Name"].toString()
                usr["NameSpesiality"] = it["NameSpesiality"].toString()
                model.currentUsr = usr
                model.currentState.postValue("Отменить талон")
            }
            Box(modifier = mod_card(onclick)) {
                myTalonFields(it)
            }
        }
}

@Composable
fun mySpecCardBox(model: MyViewModel) {
    //val item = model.lpuInfo.value
    //Text("${item?.get("Address")} ${item?.get("Phone")} ${item?.get("Email")}", modifier = Modifier.padding(padd) )
    if (!model.specList.value.isNullOrEmpty())
        LazyColumnFor(model.specList.value!!) {
            //model.specList.value?.forEach { it ->
            //val index = (colors.size * Math.random()).toInt()
            val onclick = {
                val usr = model.currentUsr.toMutableMap()
                usr["IdSpesiality"] = it["IdSpesiality"].toString()
                usr["NameSpesiality"] = it["NameSpesiality"].toString()
                model.currentUsr = usr
                model.readDocList()
                model.currentState.postValue("Выбрать врача")
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
fun myLpuCardBox(model: MyViewModel) {
    if (!model.lpuList.value.isNullOrEmpty()) {
        LazyColumnFor(model.lpuList.value!!) {
            val onclick = {
                val usr = model.currentUsr.toMutableMap()
                usr["iL"] = it["IdLPU"].toString()
                usr["L"] = it["Name"].toString()
                model.currentUsr = usr
                model.readPatID()
                model.currentState.postValue("Проверка пациента")
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
fun myUsrCardBox(model: MyViewModel) {
    if (!model.usrList.value.isNullOrEmpty())
        LazyColumnFor(model.usrList.value!!) {
            val onclick = {
                model.currentUsr = it
                model.readLpuList()
                model.currentState.postValue("Выбрать клинику")
            }
            Box(modifier = mod_card(onclick)) {
                Column(modifier = mod_padd) {
                    Text("${it["F"]} ${it["I"]} ${it["O"]}", style = typography.body1)

                    /*-- myFieldList() --*/
                    Box(modifier = mod_list()) {
                        Row {
                            Column {
                                IconButton(onClick = {
                                    model.currentUsr = it
                                    model.currentState.postValue("Изменить")
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
fun myTalonGetCardBox(model: MyViewModel) {
    Box(modifier = mod_card {}) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.currentUsr)
            Row {
                Button(content = { Text("Взять талон") }, onClick = {
                    model.getTalon()
                    model.currentState.postValue("Отложенные талоны")
                })
                TextButton(
                    content = { Text("Назад") },
                    onClick = { model.currentState.postValue("Выбрать талон") })
            }
        }
    }
}

@Composable
fun myTalonDelCardBox(model: MyViewModel) {
    Box(modifier = mod_card {}) {
        Column(modifier = Modifier.padding(padd)) {
            myTalonFields(model.currentUsr)
            Row {
                Button(content = { Text("Отменить талон") }, onClick = {
                    model.delTalon()
                    model.currentState.postValue("Отложенные талоны")
                })
                TextButton(
                    content = { Text("Назад") },
                    onClick = { model.currentState.postValue("Отложенные талоны") })
            }
        }
    }
}

