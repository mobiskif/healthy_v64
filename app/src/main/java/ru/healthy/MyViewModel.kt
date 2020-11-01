package ru.healthy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class MyViewModel(ispreview: Boolean = false) : ViewModel() {
    var infoString: String = ""

    //class myViewModel(ispreview: Boolean = false) : ApplicationViewModel(application_context) {
    lateinit var usrfile: File
    private val repository: MyRepository = MyRepository()
    val wait = repository.wait

    var isAdmin = false
    val currentState = MutableLiveData("Выбрать пациента")

    var currentUsr: Map<String, String> = mapOf()
    fun createUser(): Map<String, String> {
        //current_usr = repository.createUser()
        return mapOf(
            "R" to "Выборгский",
            "iR" to "2",
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
            repository.updateUser(currentUsr)
        }
    }
    fun deleteCurrentUser() {
        viewModelScope.launch {
            repository.deleteUser(currentUsr)
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
            repository.read10UsrList(currentUsr)
        }
    }

    val districtList = if (ispreview) repository.previewList else repository.districtList
    private fun readDistrictList() {
        viewModelScope.launch {
            repository.readDistrictList(currentUsr)
        }
    }

    val lpuList = if (ispreview) repository.previewList else repository.lpuList
    fun readLpuList() {
        viewModelScope.launch {
            repository.readLpuList(currentUsr)
        }
    }

    val patID = repository.patID
    fun readPatID() {
        viewModelScope.launch {
            repository.readPatID(currentUsr)
        }
    }

    val historyList = if (ispreview) repository.previewList else repository.historyList
    fun readHistList() {
        viewModelScope.launch {
            repository.readHistList(currentUsr)
        }
    }

    val specList = if (ispreview) repository.previewList else repository.specList
    fun readSpecList() {
        viewModelScope.launch {
            repository.readSpecList(currentUsr)
        }
    }

    /*
    val lpuInfo = repository.lpuInfo
    fun readLpuInfo(idLPU: String) {
        viewModelScope.launch {
            repository.readLpuInfo(idLPU)
        }
    }
    */

    val doctorList = if (ispreview) repository.previewList else repository.doctorList
    fun readDocList() {
        viewModelScope.launch {
            repository.readDocList(currentUsr)
        }
    }

    val talonList = if (ispreview) repository.previewList else repository.talonList
    fun readTalonList() {
        viewModelScope.launch {
            repository.readTalonList(currentUsr)
        }
    }

    val talonState = repository.talonState
    fun getTalon() {
        viewModelScope.launch {
            repository.getTalon(currentUsr)
        }
    }
    fun delTalon() {
        viewModelScope.launch {
            repository.delTalon(currentUsr)
        }
    }

    init {
        if(!ispreview) readDistrictList()
    }

}
