package ru.healthy

import android.os.AsyncTask
import android.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

open class DataLoader : AsyncTask<Void, Void, String>() {
    var SOAP_URL = "https://api.gorzdrav.spb.ru/Service/HubService.svc?wsdl"
    var SOAP_NAMESPACE = "http://tempuri.org/"
    //var SOAP_METHOD_NAME = "GetDistrictList"
    //var SOAP_METHOD_NAME = "GetLPUList"
    //var SOAP_METHOD_NAME = "GetSpesialityList"
    var SOAP_METHOD_NAME = "GetDoctorList"
    var SOAP_ACTION = "http://tempuri.org/IHubService/$SOAP_METHOD_NAME"

    var result: MutableList<Map<String, String>> = mutableListOf()
    var set: MutableMap<String, String> = mutableMapOf()
    var level=0

    fun process(po: Any, pi: PropertyInfo) {
        if (po is SoapObject) {
            Log.d("jop", "SoapObject= ${pi.name}")
            if(set.size>0) result.add(set)
            set = mutableMapOf()
            for (i in 0 until po.propertyCount) {
                process(po.getProperty(i), po.getPropertyInfo(i))
            }
        }
        else {
            Log.d("jop", "\t $pi")
            set[pi.name] = "${pi.value}"
        }
    }

    override fun doInBackground(vararg params: Void): String {
        var strresult = ""
        val request = SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME)
        request.addProperty("IdDistrict", 4)
        //request.addProperty("idLpu", 27)
        request.addProperty("idLpu", 175)
        request.addProperty("idSpesiality", 78)
        //request.addProperty("idPat", 0)
        request.addProperty("guid", "6b2158a1-56e0-4c09-b70b-139b14ffee14")
        //request.addProperty("idHistory", 0)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER10)
        envelope.bodyOut = request
        envelope.implicitTypes = true
        envelope.setOutputSoapObject(request)
        envelope.dotNet = true

        //envelope.bodyOut = request
        //MarshalBase64().register(envelope)
        //envelope.isAddAdornments = false
        //envelope.headerOut = arrayOfNulls(1)

        val androidHttpTransport = HttpTransportSE(SOAP_URL)

        try {
            androidHttpTransport.debug = false
            androidHttpTransport.call(SOAP_ACTION, envelope)
            //Log.d("jop", "request: " + androidHttpTransport.requestDump)
            //Log.d("jop", "response: " + androidHttpTransport.responseDump)

            val soapObject = envelope.response as SoapObject
            for (i in 0 until soapObject.propertyCount) {
                level=0
                process(soapObject.getProperty(i), soapObject.getPropertyInfo(i))
            }
            Log.d("jop", result.toString())

        } catch (e: Exception) {
            Log.e("jop", "requestError: " + androidHttpTransport.requestDump)
            Log.e("jop", "responseEror: " + androidHttpTransport.responseDump)
            Log.e("jop", e.toString())
        }
        a(3)
        return strresult;
    }
}

val a = {i:Int -> i+1 }

class Dataloader2(): DataLoader() {

}