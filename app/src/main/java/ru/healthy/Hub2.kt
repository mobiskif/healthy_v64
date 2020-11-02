package ru.healthy

import android.util.Log
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE


//не использую, т.к. очень долгий запрос
class Hub2 {
    private val SOAPURL = "https://api.gorzdrav.spb.ru/Service/HubService.svc?wsdl"
    private val SOAPNAMESPACE = "http://tempuri.org/"
    var set: MutableMap<String, String> = mutableMapOf()
    private var result: MutableList<Map<String, String>> = mutableListOf()

    private fun process(po: Any, pi: PropertyInfo) {
        if (po is SoapObject) {
            Log.d("jop", "SoapObject= ${pi.name}")
            if(set.isNotEmpty()) result.add(set)
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


    fun getDistr(action: String): MutableList<Map<String, String>> {
        result= mutableListOf()

        //val SOAP_METHOD_NAME = "GetDistrictList"
        val SOAPACTION = "http://tempuri.org/IHubService/$action"
        val request = SoapObject(SOAPNAMESPACE, action)
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

        val androidHttpTransport = HttpTransportSE(SOAPURL)

        try {
            androidHttpTransport.debug = false
            androidHttpTransport.call(SOAPACTION, envelope)
            //Log.d("jop", "request: " + androidHttpTransport.requestDump)
            //Log.d("jop", "response: " + androidHttpTransport.responseDump)

            val soapObject = envelope.response as SoapObject
            for (i in 0 until soapObject.propertyCount) {
                process(soapObject.getProperty(i), soapObject.getPropertyInfo(i))
            }
            Log.d("jop", result.toString())

        } catch (e: Exception) {
            Log.e("jop", "requestError: " + androidHttpTransport.requestDump)
            Log.e("jop", "responseEror: " + androidHttpTransport.responseDump)
            Log.e("jop", e.toString())
        }
        return result
    }
}
