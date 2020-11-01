package ru.healthy

import android.annotation.SuppressLint
import android.database.MatrixCursor
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection

class Hub {

    private fun readSOAP(body: String, action: String): XmlPullParser? {
        try {
            val url = URL("https://api.gorzdrav.spb.ru/Proxy/HubService.svc")
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate")
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8")
            conn.setRequestProperty("SOAPAction", "http://tempuri.org/IHubService/$action")
            conn.setRequestProperty("Content-Length", body.toByteArray().size.toString())
            conn.setRequestProperty("Host", "api.gorzdrav.spb.ru")
            conn.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)")
            //conn.doOutput = true
            conn.doInput = true

            //передача запроса
            val out = DataOutputStream(conn.outputStream)
            val outputStream = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
            outputStream.write(body)
            outputStream.flush()
            outputStream.close()
            Log.d("jop", "== Запрос == $action = " + body.length + " bytes, " + body)

            //чтение ответа
            conn.connect()
            val cis = conn.inputStream
            val isr = InputStreamReader(cis)
            val reader = BufferedReader(isr)
            val sb = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }
            isr.close()
            reader.close()
            Log.d("jop", "== Ответ == $action = " + sb.length + " bytes, " + sb)

            //препарсинг
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true// включаем поддержку namespace (по умолчанию выключена)
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(sb.toString()))
            return xpp
        } catch (e: Exception) {
            Log.e("jop", "Ошибка чтения SOAP $e")
            return null
        }

    }

    fun GetDistr(action: String): MutableList<Map<String, String>> {
        val ret = arrayListOf<String>()
        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:GetDistrictList>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "      </tem:GetDistrictList>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"
        val myParser = readSOAP(query, action)

        val from = arrayOf("_ID", "column1", "column2", "column3")
        var event: Int
        var text = ""
        val mc = MatrixCursor(from)
        val row = arrayOfNulls<Any>(from.size)
        //mc.addRow(row);
        val result: MutableList<Map<String, String>> = mutableListOf()
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "DistrictName" -> {
                                row[1] = text
                                row[2] = "район"
                                set["Name"] = text
                            }
                            "IdDistrict" -> {
                                row[0] = text
                                mc.addRow(row)
                                ret.add(row[1].toString())
                                set["IdDistrict"] = text
                                result.add(set)
                                set = mutableMapOf()
                                //Log.d("jop", text)
                            }
                            else -> {
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        return result
    }

    fun GetLpu(action: String, idDistrict: String): MutableList<Map<String, String>> {
        var ret = arrayListOf<String>()
        val districtID = "17"

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:GetLPUList>" +
                        "         <tem:IdDistrict>${idDistrict}</tem:IdDistrict>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "      </tem:GetLPUList>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        val from = arrayOf("_ID", "column1", "column2", "column3")
        var event: Int
        var text: String = ""
        val mc = MatrixCursor(from)
        val row = arrayOfNulls<Any>(from.size)
        //mc.addRow(row);
        val result: MutableList<Map<String, String>> = mutableListOf()
        var set: MutableMap<String, String> = mutableMapOf()
        var idLPU = "0"
        try {
            event = myParser!!.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "Description" -> set["Description"] = text
                            "District" -> set["District"] = text
                            "IdLPU" -> {
                                set["IdLPU"] = text
                                idLPU = text
                            }
                            "LPUFullName" -> set["LPUFullName"] = text
                            "LPUShortName" -> set["Name"] = text
                            "LPUType" -> {
                                set["LPUType"] = text

                                //var result2: MutableList<Map<String, String>> = mutableListOf()
                                val result2 = GetLpuInfo("GetLPUInfo", arrayOf(idLPU))
                                set["Address"] = result2["Address"].toString()
                                set["Email"] = result2["Email"].toString()
                                set["Phone"] = result2["Phone"].toString()
                                set["Oid"] = result2["Oid"].toString()

                                result.add(set)
                                set = mutableMapOf()
                            }

                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        return result
    }

    private fun GetLpuInfo(action: String, args: Array<String>): MutableMap<String, String> {
        var ret = arrayListOf<String>()
        val idLpu = args[0]

        val query =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                    "   <soapenv:Header/>" +
                    "   <soapenv:Body>" +
                    "      <tem:GetLPUInfo>" +
                    "         <tem:idLpu>${idLpu}</tem:idLpu>" +
                    "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                    "      </tem:GetLPUInfo>" +
                    "   </soapenv:Body>" +
                    "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        val from = arrayOf("_ID", "column1", "column2", "column3")
        var event: Int
        var text: String = ""
        val mc = MatrixCursor(from)
        val row = arrayOfNulls<Any>(from.size)
        //mc.addRow(row);
        val result: MutableList<Map<String, String>> = mutableListOf()
        val set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                //set = mutableMapOf()
                            }
                            /*
                  <a:Address>198095, Санкт-Петербург, ул. Косинова, д. 17</a:Address>
                  <a:Commentary i:nil="true"/>
                  <a:DistrictId>5</a:DistrictId>
                  <a:DistrictName>Кировский</a:DistrictName>
                  <a:DistrictOkato>40276</a:DistrictOkato>
                  <a:Email>p23@zdrav.spb.ru</a:Email>
                  <a:FullName>СПб ГБУЗ "Городская поликлиника №23"</a:FullName>
                  <a:Id>175</a:Id>
                  <a:Oid>1.2.643.5.1.13.13.12.2.78.8639.0.238289</a:Oid>
                  <a:Phone>(812) 786-44-29</a:Phone>
                  <a:PublicGuid>67E63AB5-1228-4D8F-8006-0CB90C17FC5E</a:PublicGuid>
                  <a:ShortName>ГП 23</a:ShortName>
                  <a:Type>Поликлиники взрослые</a:Type>
                  <a:WorkTime i:nil="true"/>
                             */
                            "Address" -> set["Address"] = text
                            "Email" -> set["Email"] = text
                            "Id" -> set["Id"] = text
                            "FullName" -> set["FullName"] = text
                            "ShortName" -> set["ShortName"] = text
                            "Phone" -> {
                                set["Phone"] = text
                                result.add(set)
                                //set = mutableMapOf()
                            }

                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return set
        }

        return set
    }

    fun GetSpec(action: String, args: Array<String>): MutableList<Map<String, String>> {
        val ret = arrayListOf<String>()
        val idPat = args[1]
        val idLPU = args[0]

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:GetSpesialityList>" +
                        "         <tem:idLpu>" + idLPU + "</tem:idLpu>" +
                        "         <tem:idPat>" + idPat + "</tem:idPat>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "      </tem:GetSpesialityList>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        val from = arrayOf("_ID", "column1", "column2", "column3")
        //var event: Int
        //var text: String? = null
        val mc = MatrixCursor(from)
        val row = arrayOfNulls<Any>(from.size)
        //mc.addRow(row);
        val result: MutableList<Map<String, String>> = mutableListOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()

        try {
            event = myParser!!.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "CountFreeParticipantIE" -> set["CountFreeParticipantIE"] = text
                            "CountFreeTicket" -> set["CountFreeTicket"] = text
                            "IdSpesiality" -> set["IdSpesiality"] = text
                            "NameSpesiality" -> {
                                row[1] = text
                                set["NameSpesiality"] = text
                                mc.addRow(row)
                                ret.add(row[1].toString())
                                result.add(set)
                                set = mutableMapOf()
                            }
                            else -> {
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        return result
    }

    fun GetDoc(action: String, args: Array<String>): MutableList<Map<String, String>> {

        //val idPat = args[2]
        val specID = args[0]
        val idLPU = args[1]

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:GetDoctorList>" +
                        "         <tem:idSpesiality>" + specID + "</tem:idSpesiality>" +
                        "         <tem:idLpu>" + idLPU + "</tem:idLpu>" +
                        //"         <tem:idPat>" + idPat + "</tem:idPat>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "      </tem:GetDoctorList>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        val result: MutableList<Map<String, String>> = mutableListOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "AriaNumber" -> set["AriaNumber"] = text
                            "CountFreeParticipantIE" -> set["CountFreeParticipantIE"] = text
                            "CountFreeTicket" -> set["CountFreeTicket"] = text
                            "IdDoc" -> set["IdDoc"] = text
                            "LastDate" -> set["LastDate"] = text
                            "Name" -> set["Name"] = text
                            "NearestDate" -> set["NearestDate"] = text
                            "Snils" -> {
                                set["Snils"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        //return ret.toMutableList()
        return result
    }

    fun GetHist(action: String, args: Array<String>): MutableList<Map<String, String>> {

        val idPat = args[1]
        val idLPU = args[0]

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:GetPatientHistory>" +
                        "         <tem:idLpu>" + idLPU + "</tem:idLpu>" +
                        "         <tem:idPat>" + idPat + "</tem:idPat>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "      </tem:GetPatientHistory>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        val result: MutableList<Map<String, String>> = mutableListOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "DateCreatedAppointment" -> set["DateCreatedAppointment"] = text
                            "AriaNumber" -> set["AriaNumber"] = text
                            "Name" -> set["Name"] = text
                            "IdAppointment" -> set["IdAppointment"] = text
                            "NameSpesiality" -> set["NameSpesiality"] = text
                            "UserName" -> set["UserName"] = text
                            "VisitStart" -> {
                                var loc = text.indexOf("T")
                                val date = text.substring(0, loc)
                                var time = text.substring(loc + 1)
                                loc = time.lastIndexOf(":")
                                time = time.substring(0, loc)
                                //row[2] = text.split("T")[0] + "\n"+ text.split("T")[1];
                                //row[2] = "$date $time"


                                set["VisitStart"] = time
                                set["VisitEnd"] = date
                                result.add(set)
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        //return ret.toMutableList()
        return result
    }

    fun GetPat(action: String, args: Array<String>): MutableMap<String, String> {

        val idPat = "502655"
        val specID = 78
        val idLPU = 174

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">" +
                        "   <soapenv:Header/>" +
                        "   <soapenv:Body>" +
                        "      <tem:CheckPatient>" +
                        "         <tem:pat>" +
                        "           <hub:Birthday>" + args[3] + "</hub:Birthday>" +
                        "            <hub:Name>" + args[1] + "</hub:Name>" +
                        "            <hub:SecondName>" + args[2] + "</hub:SecondName>" +
                        "            <hub:Surname>" + args[0] + "</hub:Surname>" +
                        "         </tem:pat>" +
                        "         <tem:idLpu>" + args[4] + "</tem:idLpu>" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                        "     </tem:CheckPatient>" +
                        "   </soapenv:Body>" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        var result: MutableMap<String, String> = mutableMapOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> set["ErrorDescription"] = text
                            "Success" -> set["Success"] = text
                            "IdPat" -> {
                                set["IdPat"] = text
                                result = set
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            set = mutableMapOf()
            set["IdPat"] = ""
            set["ErrorDescription"] = "Карточка пациента не найдена. Указанные ФИО, дата рождения и данные в поликлинике должны совпадать. Позвоните/сходите в регистратуру для сверки"
            return set
        }

        //return ret.toMutableList()
        return result
    }

    @SuppressLint("SimpleDateFormat")
    fun GetApp(action: String, args: Array<String>): MutableList<Map<String, String>> {

        val idPat = args[2]
        val idDoc = args[1]
        val idLPU = args[0]

        val s = SimpleDateFormat("yyyy-MM-dd").format(Date())

        val query = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <tem:GetAvaibleAppointments>" +
                "         <tem:idDoc>" + idDoc + "</tem:idDoc>" +
                "         <tem:idLpu>" + idLPU + "</tem:idLpu>" +
                "         <tem:idPat>" + idPat + "</tem:idPat>" +
                "         <tem:visitStart>$s</tem:visitStart>" +
                "         <tem:visitEnd>2025-12-31</tem:visitEnd>" +
                "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                "      </tem:GetAvaibleAppointments>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>"


        val myParser = readSOAP(query, action)

        val result: MutableList<Map<String, String>> = mutableListOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                result.add(set)
                                set = mutableMapOf()
                            }
                            "IdAppointment" -> set["IdAppointment"] = text
                            "VisitEnd" -> set["VisitEnd"] = text
                            "VisitStart" -> {
                                var loc = text.indexOf("T")
                                val date = text.substring(0, loc)
                                var time = text.substring(loc + 1)
                                loc = time.lastIndexOf(":")
                                time = time.substring(0, loc)
                                //row[2] = text.split("T")[0] + "\n"+ text.split("T")[1];
                                //row[2] = "$date $time"


                                set["VisitStart"] = time
                                set["VisitEnd"] = date
                                set["Name"] = ""
                                set["NameSpesiality"] = ""
                                result.add(set)
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        //return ret.toMutableList()
        return result
    }

    fun SetApp(action: String, args: Array<String>): MutableMap<String, String> {

        val idPat = args[2]
        val idAppoint = args[1]
        val idLPU = args[0]

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <tem:SetAppointment>\n" +
                        "         <tem:idAppointment>" + idAppoint + "</tem:idAppointment>\n" +
                        "         <tem:idLpu>" + idLPU + "</tem:idLpu>\n" +
                        "         <tem:idPat>" + idPat + "</tem:idPat>\n" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                        "      </tem:SetAppointment>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        var result: MutableMap<String, String> = mutableMapOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            Log.e("jops", query)
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                Log.d("jops", "ошибка=$text")
                            }
                            "Success" -> {
                                Log.d("jops", "успех=$text")
                                //set["Success"] = text
                                if (text == "true") set["Success"]="Талон отложен!"
                                else set["Success"] = "Отказано! "+set["ErrorDescription"]
                                result = set
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableMapOf()
        }

        //return ret.toMutableList()
        return result
    }

    fun RefApp(action: String, args: Array<String>): MutableMap<String, String> {

        val idPat = args[2]
        val idAppoint = args[1]
        val idLPU = args[0]

        val query =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <tem:CreateClaimForRefusal>\n" +
                        "         <tem:idLpu>" + idLPU + "</tem:idLpu>\n" +
                        "         <tem:idPat>" + idPat + "</tem:idPat>\n" +
                        "         <tem:idAppointment>" + idAppoint + "</tem:idAppointment>\n" +
                        "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>\n" +
                        "      </tem:CreateClaimForRefusal>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>"

        val myParser = readSOAP(query, action)

        var result: MutableMap<String, String> = mutableMapOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        try {
            Log.e("jops", query)
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            "ErrorDescription" -> {
                                set["ErrorDescription"] = text
                                Log.d("jops", "ошибка=$text")
                            }
                            "Success" -> {
                                Log.d("jops", "успех=$text")
                                //set["Success"] = text
                                if (text == "true") set["Success"]="Талон отменен!"
                                else set["Success"] = "Ошибка!\n"+set["ErrorDescription"]
                                result = set
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableMapOf()
        }

        //return ret.toMutableList()
        return result
    }

    fun GetTop10(action: String, args: Array<String>): MutableList<Map<String, String>> {

        val idPat = "502655"
        val specID = 78
        val LPUShortName = args[4]
        val Distr = args[3]
        val idDistr = args[2]
        val idLPU = args[1]
        val surn = args[0]

        val query =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:hub=\"http://schemas.datacontract.org/2004/07/HubService2\">" +
                    "   <soapenv:Header/>" +
                    "   <soapenv:Body>" +
                    "      <tem:SearchTop10Patient>" +
                    "         <tem:pat>" +
                    "            <hub:Surname>" + surn + "</hub:Surname>" +
                    "         </tem:pat>" +
                    "         <tem:idLpu>" + idLPU + "</tem:idLpu>" +
                    "         <tem:guid>6b2158a1-56e0-4c09-b70b-139b14ffee14</tem:guid>" +
                    "     </tem:SearchTop10Patient>" +
                    "   </soapenv:Body>" +
                    "</soapenv:Envelope>"
        val myParser = readSOAP(query, action)

        val from = arrayOf("_ID", "column1", "column2", "column3")
        val mc = MatrixCursor(from)
        val row = arrayOfNulls<Any>(from.size)
        //mc.addRow(row);
        val result: MutableList<Map<String, String>> = mutableListOf()
        var event: Int
        var text: String = ""
        var set: MutableMap<String, String> = mutableMapOf()
        var ret = arrayListOf<String>()

        try {
            event = myParser!!.eventType
            //set= mutableMapOf()
            while (event != XmlPullParser.END_DOCUMENT) {
                val name = myParser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                    }

                    XmlPullParser.TEXT -> text = myParser.text

                    XmlPullParser.END_TAG -> {
                        when (name) {
                            /*
                            "ErrorDescription" -> {
                                if (text.equals("null")) set["ErrorDescription"] = " "
                                else set["ErrorDescription"] = text
                            }
                            "Success" -> set["Success"] = text
                            */
                            "AriaNumber" -> set["AriaNumber"] = text
                            "Birthday" -> {
                                val loc = text.indexOf("T")
                                val date = text.substring(0, loc)
                                var time = text.substring(loc + 1)
                                set["D"] = date
                            }
                            "Document_N" -> set["Document_N"] = text
                            "IdPat" -> set["IdPat"] = text
                            "Name" -> set["I"] = text
                            "Polis_N" -> set["Polis_N"] = text
                            "SecondName" -> set["O"] = text
                            "Surname" -> {
                                set["F"] = text
                                set["iR"] = idDistr
                                set["R"] = Distr
                                set["iL"] = idLPU
                                set["L"] = LPUShortName
                                set["id"] = "${Math.random()}"
                                result.add(set)
                                set = mutableMapOf()
                            }
                        }
                        text = ""
                    }
                }
                event = myParser.next()
            }
        } catch (e: Exception) {
            Log.e("jop", "Ошибка парсинга SOAP $e")
            return mutableListOf()
        }

        //return ret.toMutableList()
        return result
    }


}