package ru.healthy

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import java.io.File

val padd = 8.dp
val tmod = Modifier.padding(0.dp, 0.dp, 0.dp, padd)
val tstyle = TextStyle(fontSize = 18.sp)
val colors = listOf(
        Color(0xFFffd7d7.toInt()),
        Color(0xFFffe9d6.toInt()),
        Color(0xFFfffbd0.toInt()),
        Color(0xFFe3ffd9.toInt()),
        Color(0xFFd0fff8.toInt())
)

private var infoString = ""
private var isVertical = true

class MainActivity : AppCompatActivity() {
    private val model: AppViewModel by viewModels()

    override fun onCreate(savedStateHandle: Bundle?) {
        super.onCreate(savedStateHandle)
        File(filesDir, "usrlist.csv").createNewFile()
        model.usrfile = File(filesDir, "usrlist.csv")

        model.wait.observe(this, waitObserver())
        model.usrList.observe(this, usrListObserver())
        model.current_state.observe(this, stateObserver())
        model.lpuList.observe(this, stateObserver())
        model.specList.observe(this, stateObserver())
        model.doctorList.observe(this, stateObserver())
        model.talonList.observe(this, stateObserver())
        model.historyList.observe(this, stateObserver())
        model.patID.observe(this, patientObserver())
        model.talonState.observe(this, talonStateObserver())
    }

    override fun onResume() {
        super.onResume()
        model.readUsrList()
    }

    override fun onPause() {
        super.onPause()
        model.saveUsrList()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || window.attributes.width > window.attributes.height) isVertical = false
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT || window.attributes.width < window.attributes.height) isVertical = true
        setContent { UI_(model) }
    }

    override fun onBackPressed() {
        var state = model.current_state.value
        when (state) {
            "Выбрать пациента" -> state = if (model.isAdmin) "Search top 10" else "Выбрать пациента"
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Информация" -> state = "Выбрать пациента"
            "Выбрать специальность" -> state = "Выбрать клинику"
            "Выбрать врача" -> state = "Выбрать специальность"
            "Отложенные талоны" -> state = "Выбрать специальность"
            "Выбрать талон" -> state = "Выбрать врача"
            "Взять талон" -> state = "Выбрать талон"
            "Отменить талон" -> state = "Выбрать специальность"
        }
        model.current_state.postValue(state)
    }

    private fun waitObserver() = Observer<Any> {
        Log.d("jop", "------ waitObserver: wait = ${model.wait.value}")
        setContent { UI_(model) }
    }

    private fun usrListObserver() = Observer<List<Map<String, String>>> {
        Log.d("jop", "------ usrListObserver: $it")
        setContent { UI_(model) }
    }

    private fun patientObserver() = Observer<Map<String, String>> {
        val usr = model.current_usr.toMutableMap()
        usr["IdPat"] = it["IdPat"].toString()
        if ("${it["ErrorDescription"]}".length > 4) {
            usr["lastError"] =
                    "${it["ErrorDescription"]}. Запись в ${model.current_usr["L"]} невозможна."
        }
        model.current_usr = usr
        model.readHistList()
        model.readSpecList()
        model.readLpuInfo()
        model.current_state.postValue("Выбрать специальность")
    }

    private fun talonStateObserver() = Observer<Map<String, String>> {
        if ("${it["ErrorDescription"]}".length > 4) {
            val usr = model.current_usr.toMutableMap()
            usr["lastError"] =
                    "${it["ErrorDescription"]}. Запись в ${model.current_usr["L"]} невозможна."
            model.current_usr = usr
        }
        Toast.makeText(this, "${model.talonState.value}", Toast.LENGTH_SHORT).show()
    }

    private fun stateObserver() = Observer<Any> {
        Log.d("jop", "------ stateObserver: state = ${model.current_state.value}")
        val usr = model.current_usr.toMutableMap()
        when (model.current_state.value) {
            "Search top 10" -> {
                infoString = ""
            }
            "Выбрать пациента" -> {
                model.current_usr = mapOf()
                infoString = ""
            }
            "Изменить" -> {
                infoString = ""
            }
            "Информация" -> {
                infoString = ""
            }
            "Выбрать клинику" -> {
                infoString = "${usr["F"]} ${usr["I"]} ${usr["D"]}"
                usr["lastError"] = ""
                model.current_usr = usr
            }
            "Проверка пациента" -> {
                infoString = "${usr["F"]} ${usr["I"]} ${usr["D"]}"
            }
            "Выбрать специальность" -> {
                infoString = "${usr["F"]} ${usr["I"]} ${usr["L"]} карточка №:${usr["IdPat"]}"
            }
            "Выбрать врача" -> {
                infoString = "${usr["F"]} ${usr["I"]} ${usr["L"]} ${usr["NameSpesiality"]}"
            }
            "Выйти" -> {
                finish()
            }
        }
        setContent { UI_(model) }

    }
}

@androidx.compose.runtime.Composable
fun myTopAppBar(model: AppViewModel) {
    TopAppBar(
            title = { Text("${model.current_state.value}", maxLines = 2) },
            navigationIcon = {
                IconButton(onClick = { model.current_state.postValue("Выбрать пациента") }) {
                    Icon(Icons.Filled.Person)
                }
            },
            actions = {
                IconButton(onClick = { model.current_state.postValue("Информация") }) {
                    Icon(Icons.Filled.Info)
                }
                IconButton(onClick = { model.current_state.postValue("Выйти") }) {
                    Icon(Icons.Filled.ExitToApp)
                }
                //IconButton(onClick = { model.isAdmin = !model.isAdmin }) { Icon(Icons.Filled.MoreVert) }
                //myTopDropDownMenu(model)
            }
    )
}

@androidx.compose.runtime.Composable
fun UI_(model: AppViewModel) {
    if (model.isAdmin) model.current_usr.forEach { infoString += "${it.key}=${it.value}  " }
    Scaffold(
            topBar = {
                myTopAppBar(model)
            },
            floatingActionButton = {
                if (model.current_state.value.equals("Выбрать пациента")) {
                    FloatingActionButton(
                            onClick = {
                                //model.current_usr = model.createUser()
                                //model.usrList.value!!.plus(model.current_usr)
                                val newuser = model.createUser()
                                model.addUser(newuser)
                                model.current_usr = newuser
                                model.current_state.postValue("Изменить")
                            }
                    ) { Icon(Icons.Filled.Add) }
                } else if (model.current_state.value.equals("Выбрать специальность")) {
                    FloatingActionButton(
                            onClick = {
                                model.current_state.postValue("Отложенные талоны")
                            }
                    ) { Icon(Icons.Filled.DateRange) }
                }
            },
            bodyContent = {
                Column {
                    Text(infoString, modifier = Modifier.padding(padd), style = tstyle)
                    if (!model.current_usr["lastError"].isNullOrEmpty()) {
                        Text("${model.current_usr["lastError"]}", modifier = Modifier.padding(padd), style = TextStyle(color = Color.Red))
                    }
                    if (isVertical) ScrollableColumn { UI_Content(model) }
                    else ScrollableRow { UI_Content(model) }
                }
            }
    )
}

@androidx.compose.runtime.Composable
fun UI_Content(model: AppViewModel) {

    if (model.wait.value!!) {
        Text(
                "Запрос в регистратуру ${model.current_usr["L"]}",
                modifier = Modifier.padding(padd)
        )
        LinearProgressIndicator(modifier = Modifier.padding(padd).fillMaxWidth())
    } else {
        when (model.current_state.value) {
            "Search top 10" -> my10UsrEditCardBox(model)
            "Выбрать пациента" -> myUsrCardBox(model)
            "Изменить" -> myUsrEditCardBox(model)
            "Выбрать клинику" -> myLpuCardBox(model)
            "Проверка пациента" -> mySpecCardBox(model)
            "Отложенные талоны" -> myHistCardBox(model)
            "Выбрать специальность" -> mySpecCardBox(model)
            "Выбрать врача" -> myDoctorCardBox(model)
            "Выбрать талон" -> myTalonCardBox(model)
            "Взять талон" -> myTalonGetCardBox(model)
            "Отменить талон" -> myTalonDelCardBox(model)
            "Информация" -> myHelp(model)
            "Выйти" -> Text("Выход")
            else -> Text("Under construction ...")
        }
    }
}
