package ru.healthy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception

class AppRepository {

    val previewList = MutableLiveData<List<Map<String, String>>>(
        mutableListOf(
            mapOf(
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
        )
    )

    //val previewUsrList = createUsrList()
    fun createUser(): Map<String, String> {
        var new_usr = previewList.value!![0]
        //_usrList.value?.plus(new_usr)
        return new_usr
    }

    suspend fun addUserToList(user: Map<String, String>) {
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _usrList.postValue(usrList.value?.plus(user))
            Log.d("jop", "------ add ------ ${usrList.value}")
            _wait.postValue(false)
        }
    }


    suspend fun updateUser(usr: Map<String, String>) {
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //исключаем из списка старого юсера с id текущего
            var lst = usrList.value?.filterNot { it["id"] == usr["id"] }
            //вместо него добавляем нового текущего и подменяем usrList
            _usrList.postValue(lst?.plus(usr))
            _wait.postValue(false)
        }
    }

    suspend fun deleteUser(usr: Map<String, String>) {
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            var lst1 =  usrList.value
            var usr1 = usr
            var lst2 = lst1?.filterNot { it ==usr1 }
            _usrList.postValue(lst2)

            //исключаем из списка старого юсера с id текущего
            //var lst = usrList.value!!.filterNot { it == usr }
            //var lst = usrList.value!!.filterNot { it["id"].equals(usr["id"]) }
            //_usrList.postValue(lst)
            _wait.postValue(false)
        }
    }

    private val _wait = MutableLiveData(false)
    val wait: LiveData<Boolean> = _wait

    private var _doctorList = MutableLiveData<List<Map<String, String>>>()
    val doctorList: LiveData<List<Map<String, String>>> = _doctorList
    suspend fun readDocList(curr_user: Map<String, String>) {
        val args = arrayOf(curr_user["IdSpesiality"].toString(), curr_user["iL"].toString())
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _doctorList.postValue(Hub().GetDoc("GetDoctorList", args))
            _wait.postValue(false)
        }
    }

    private var _talonList = MutableLiveData<List<Map<String, String>>>()
    val talonList: LiveData<List<Map<String, String>>> = _talonList
    suspend fun readTalonList(curr_user: Map<String, String>) {
        val args = arrayOf(
            curr_user["iL"].toString(),
            curr_user["IdDoc"].toString(),
            curr_user["IdPat"].toString()
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _talonList.postValue(Hub().GetApp("GetAvaibleAppointments", args))
            _wait.postValue(false)
        }
    }

    private var _historyList = MutableLiveData<List<Map<String, String>>>()
    val historyList: LiveData<List<Map<String, String>>> = _historyList
    suspend fun readHistList(curr_user: Map<String, String>) {
        val args = arrayOf(curr_user["iL"].toString(), curr_user["IdPat"].toString())
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _historyList.postValue(Hub().GetHist("GetPatientHistory", args))
            _wait.postValue(false)
        }
    }

    private var _districtList = MutableLiveData<List<Map<String, String>>>()
    val districtList: LiveData<List<Map<String, String>>> = _districtList
    suspend fun readDistrictList(curr_user: Map<String, String>) {
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _districtList.postValue(Hub().GetDistr("GetDistrictList"))
            _wait.postValue(false)
        }
    }

    private var _lpuList = MutableLiveData<List<Map<String, String>>>()
    val lpuList: LiveData<List<Map<String, String>>> = _lpuList
    suspend fun readLpuList(curr_user: Map<String, String>) {
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _lpuList.postValue(Hub().GetLpu("GetLPUList", curr_user["iR"].toString()))
            _wait.postValue(false)
        }
    }

    private var _specList = MutableLiveData<List<Map<String, String>>>()
    val specList: LiveData<List<Map<String, String>>> = _specList
    suspend fun readSpecList(curr_user: Map<String, String>) {
        val args = arrayOf(curr_user["iL"].toString(), curr_user["IdPat"].toString())
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _specList.postValue(Hub().GetSpec("GetSpesialityList", args))
            _wait.postValue(false)
        }
    }

    private var _patID = MutableLiveData<Map<String, String>>()
    val patID: LiveData<Map<String, String>> = _patID
    suspend fun readPatID(curr_user: Map<String, String>) {
        val args = arrayOf(
            curr_user["F"].toString(),
            curr_user["I"].toString(),
            curr_user["O"].toString(),
            curr_user["D"].toString(),
            curr_user["iL"].toString()
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //_patList.postValue( Hub().GetTop10("SearchTop10Patient", args) )
            var res = Hub().GetPat("CheckPatient", args)
            _patID.postValue(res)
            _wait.postValue(false)
        }
    }

    /*
    private var _lpuInfo = MutableLiveData<Map<String, String>>()
    val lpuInfo: LiveData<Map<String, String>> = _lpuInfo
    suspend fun readLpuInfo(idLPU: String) {
        val args = arrayOf(
            idLPU
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //_patList.postValue( Hub().GetTop10("SearchTop10Patient", args) )
            var res = Hub().GetLpuInfo("GetLPUInfo", args)
            _lpuInfo.postValue(res)

            var lst1 =  _lpuList.value
            var lpu = _lpuList.value?.filter { it["IdLPU"] == idLPU }
            var lst2 = lst1?.filterNot { it ==lpu }


            Log.d("jop", "***** $lpu")
            var address = mapOf("Address" to res["Address"])
            var email = mapOf("Email" to res["Email"])
            var phone = mapOf("Phone" to res["Phone"])

            var lpu2 = lpu?.get(0)?.plus(address)
            var lpu3 = lpu2?.plus(email)
            var lpu4 = lpu3?.plus(phone)
            Log.d("jop", "***** $lpu4")

            var lst3 = lst2?.plus(lpu4) as List<Map<String, String>>
            _lpuList2.postValue(lst3)

            _wait.postValue(false)
        }
    }
    */


    private var _talonState = MutableLiveData<Map<String, String>>()
    val talonState: LiveData<Map<String, String>> = _talonState
    suspend fun delTalon(curr_user: Map<String, String>) {
        val args = arrayOf(
            curr_user["iL"].toString(),
            curr_user["IdAppointment"].toString(),
            curr_user["IdPat"].toString()
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _talonState.postValue(Hub().RefApp("CreateClaimForRefusal", args))
            _wait.postValue(false)
        }
        readHistList(curr_user)
    }

    suspend fun getTalon(curr_user: Map<String, String>) {
        val args = arrayOf(
            curr_user["iL"].toString(),
            curr_user["IdAppointment"].toString(),
            curr_user["IdPat"].toString()
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            _talonState.postValue(Hub().SetApp("SetAppointment", args))
            _wait.postValue(false)
        }
        readHistList(curr_user)
    }

    private var _usrList = previewList//MutableLiveData<List<Map<String, String>>>()
    val usrList: LiveData<List<Map<String, String>>> = _usrList
    fun readUsrList(usrfile: File) {
        try {
            if (usrfile.length() > 0) {
                var fis = FileInputStream(usrfile)
                val ois = ObjectInputStream(fis)
                val usrlist = ois.readObject() as List<Map<String, String>>
                fis.close()
                _usrList.postValue(usrlist)
                Log.d("jop", "${usrfile} прочитано: $usrlist")
            } else _usrList.postValue(previewList.value)

        }
        catch (e: Exception) {
            _usrList.postValue(previewList.value)
        }
    }
    fun updateUsrList(usrfile: File) {
        var fos = FileOutputStream(usrfile)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(usrList.value)
        oos.close()
        fos.close()

        Log.d("jop", "${usrfile} записано: ${usrList.value}")
    }
    suspend fun read10UsrList(curr_user: Map<String, String>) {
        val args = arrayOf(
            curr_user["F"].toString(),
            curr_user["iL"].toString(),
            curr_user["iR"].toString(),
            curr_user["R"].toString(),
            curr_user["L"].toString()
        )
        withContext(Dispatchers.IO) {
            _wait.postValue(true)
            //_patList.postValue( Hub().GetTop10("SearchTop10Patient", args) )
            _usrList.postValue(Hub().GetTop10("SearchTop10Patient", args))
            _wait.postValue(false)
        }
    }

}

