package ru.healthy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class AppViewModel(ispreview: Boolean = false) : ViewModel() {
    lateinit var usrfile: File
    private val repository: AppRepository = AppRepository()
    val wait = repository.wait

    var isAdmin = false
    val current_state = MutableLiveData<String>("Выбрать пациента")

    var current_usr: Map<String, String> = mapOf()
    fun createUser(): Map<String, String> {
        //current_usr = repository.createUser()
        return mapOf(
            "R" to "Адмиралтейский",
            "iR" to "1",
            "L" to "",
            "iL" to "",
            "F" to "Новый",
            "I" to "Пациент",
            "O" to "",
            "D" to "1986-04-26",
            "id" to "${Math.random()}"
        )
        //return repository.createUser()
    }
    fun updateUserInList() {
        viewModelScope.launch {
            repository.updateUser(current_usr)
        }
    }
    fun deleteCurrentUser() {
        viewModelScope.launch {
            repository.deleteUser(current_usr)
        }
    }

    val usrList = if (ispreview) repository.previewList else repository.usrList
    fun addUser(user: Map<String, String>) {
        viewModelScope.launch {
            repository.addUserToList(user)
        }
    }
    fun readUsrList() {
        viewModelScope.launch {
            repository.readUsrList(usrfile)
        }
    }
    fun saveUsrList() {
        viewModelScope.launch {
            repository.updateUsrList(usrfile)
        }
    }
    fun read10UsrList() {
        viewModelScope.launch {
            repository.read10UsrList(current_usr)
        }
    }

    val districtList = if (ispreview) repository.previewList else repository.districtList
    fun readDistrictList() {
        viewModelScope.launch {
            repository.readDistrictList(current_usr)
        }
    }

    val lpuList = if (ispreview) repository.previewList else repository.lpuList
    fun readLpuList() {
        viewModelScope.launch {
            repository.readLpuList(current_usr)
        }
    }

    val patID = repository.patID
    fun readPatID() {
        viewModelScope.launch {
            repository.readPatID(current_usr)
        }
    }

    val historyList = if (ispreview) repository.previewList else repository.historyList
    fun readHistList() {
        viewModelScope.launch {
            repository.readHistList(current_usr)
        }
    }

    val specList = if (ispreview) repository.previewList else repository.specList
    fun readSpecList() {
        viewModelScope.launch {
            repository.readSpecList(current_usr)
        }
    }

    val lpuInfo = repository.lpuInfo
    fun readLpuInfo() {
        viewModelScope.launch {
            repository.readLpuInfo(current_usr)
        }
    }

    val doctorList = if (ispreview) repository.previewList else repository.doctorList
    fun readDocList() {
        viewModelScope.launch {
            repository.readDocList(current_usr)
        }
    }

    val talonList = if (ispreview) repository.previewList else repository.talonList
    fun readTalonList() {
        viewModelScope.launch {
            repository.readTalonList(current_usr)
        }
    }

    val talonState = repository.talonState
    fun getTalon() {
        viewModelScope.launch {
            repository.getTalon(current_usr)
        }
    }
    fun delTalon() {
        viewModelScope.launch {
            repository.delTalon(current_usr)
        }
    }

    init {
        if(!ispreview) readDistrictList()
    }

}
