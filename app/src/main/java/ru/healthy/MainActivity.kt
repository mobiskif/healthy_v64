package ru.healthy

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.Observer
import androidx.ui.tooling.preview.Preview
import java.io.File

class MainActivity : AppCompatActivity() {
    private val model: myViewModel by viewModels()
    private var isVertical = true

    override fun onCreate(savedStateHandle: Bundle?) {
        super.onCreate(savedStateHandle)
        //model = ViewModelProvider(this).get(myViewModel(this).javaClass)
        File(filesDir, "usrlist.csv").createNewFile()
        model.usrfile = File(filesDir, "usrlist.csv")

        model.wait.observe(this, waitObserver())
        model.current_state.observe(this, stateObserver())
        model.patID.observe(this, patientObserver())
        model.specList.observe(this, stateObserver())
        model.doctorList.observe(this, stateObserver())
        model.talonList.observe(this, stateObserver())
        model.historyList.observe(this, stateObserver())
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

    override fun onBackPressed() {
        var state = model.current_state.value
        when (state) {
            "Выбрать пациента" -> state = if (model.isAdmin) "Search top 10" else "Выбрать пациента"
            "Выбрать клинику" -> state = "Выбрать пациента"
            "Изменить" -> state = "Выбрать пациента"
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("jop",newConfig.toString())
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || window.attributes.width > window.attributes.height) isVertical =
            false
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT || window.attributes.width < window.attributes.height) isVertical =
            true
        setContent { UI_(model) }
    }

    private fun waitObserver() = Observer<Boolean> {
        Log.d("jop", "wait=$it")
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
        //model.readLpuInfo()
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
        val history = model.historyList.value?.size
        when (model.current_state.value) {
            "Search top 10" -> {
                model.infoString = ""
            }
            "Выбрать пациента" -> {
                model.current_usr = mapOf()
                model.infoString = "Санкт-Петербург"
            }
            "Изменить" -> {
                model.infoString = "Санкт-Петербург"
            }
            "Информация" -> {
                model.infoString = "Как это работает"
            }
            "Выбрать клинику" -> {
                model.infoString = "${usr["F"]} ${usr["I"]} ${usr["D"]}"
                usr["lastError"] = ""
                model.current_usr = usr
            }
            "Проверка пациента" -> {
                model.infoString = "${usr["F"]} ${usr["I"]} ${usr["D"]}"
            }
            "Выбрать специальность" -> {
                model.infoString = "${usr["L"]}" + "\nКарточка №:${usr["IdPat"]} $history"
            }
            "Выбрать врача" -> {
                model.infoString = "${usr["L"]} \n${usr["NameSpesiality"]}"
            }
            "Выйти" -> {
                finish()
            }
        }
        setContent { UI_(model) }

    }
}

@Composable
fun showCurrentState(model: myViewModel) {
    when (model.current_state.value) {
        "Search top 10" -> my10UsrEditCardBox(model)
        "Изменить" -> myUsrEditCardBox(model)
        "Выбрать пациента" -> myUsrCardBox(model)
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
        else -> Text("Функция не реализована")
    }
}

@Composable
fun UI_(model: myViewModel) {
    myTheme {
        Scaffold(
            topBar = { myBar(model) },
            floatingActionButton = { myFab(model) }
        ) {
            if (model.wait.value!!) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize().then(mod_padd)) {
                        Text("Запрос в регистратуру ${model.current_usr["L"]}", style = typography.body1)
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            else {
                Column {
                    showCurrentInfo(model)
                    showCurrentState(model)
                }
            }
        }
    }
}

@Preview(showBackground = true, showDecoration = true)
@Composable
fun LightPreview() {
    UI_(myViewModel(true))
}
