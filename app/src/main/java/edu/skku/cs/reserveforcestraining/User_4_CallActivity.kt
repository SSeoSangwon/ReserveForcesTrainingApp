package edu.skku.cs.reserveforcestraining

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class User_4_CallActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val dataList = mutableListOf<String>()
    private lateinit var button_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_4_call)

        listView = findViewById(R.id.listview_calllist)
        button_back = findViewById(R.id.imageButton_back)

        // 국방부_예비군훈련장 연락처 정보 (제공기관 : 국방부)
        val url = "https://openapi.mnd.go.kr/${BuildConfig.MND_API_KEY}/xml/DS_AMRSV_CNTADR_PRSTS/1/10980/"
        fetchXMLData(url)

        // 뒤로가기 버튼
        button_back.setOnClickListener {
            onBackPressed()
        }
    }

    // XML데이터 처리
    private fun fetchXMLData(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val xmlData = downloadXML(url)
            val contactList = getContactListFromXML(xmlData)

            runOnUiThread {
                dataList.clear()
                contactList?.let {
                    dataList.addAll(it)
                    updateListView()
                }
            }
        }
    }

    private fun downloadXML(urlString: String): String? {
        var inputStream: InputStream? = null
        var xmlData: String? = null

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                xmlData = stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return xmlData
    }

    private fun getContactListFromXML(xmlData: String?): List<String>? {
        val contactList = mutableListOf<String>()

        val xmlDoc =
            DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(InputSource(xmlData?.reader()))
        val nodeList = xmlDoc.getElementsByTagName("row")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val amrsvbnElement = element.getElementsByTagName("amrsvbn").item(0) as Element
                val amrsvbn = amrsvbnElement.textContent
                val telElement = element.getElementsByTagName("tel").item(0) as Element
                val tel = telElement.textContent

                val contactInfo = "$amrsvbn: $tel"
                contactList.add(contactInfo)
            }
        }

        return contactList
    }

    private fun updateListView() {
        val adapter = CallAdapter(this, dataList)
        listView.adapter = adapter
    }

    inner class CallAdapter(context: Context, private val callList: List<String>) :
        ArrayAdapter<String>(context, R.layout.user_4_call_list, callList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val viewHolder: ViewHolder

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.user_4_call_list, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as ViewHolder
            }

            val callInfo = callList[position]
            val callParts = callInfo.split(": ")
            val name = callParts[0]
            val phoneNumber = callParts[1]

            viewHolder.nameTextView.text = name
            viewHolder.callTextView.text = phoneNumber
            viewHolder.callTextView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                context.startActivity(intent)
            }

            return view!!
        }

        private inner class ViewHolder(view: View) {
            val nameTextView: TextView = view.findViewById(R.id.nameTextView)
            val callTextView: TextView = view.findViewById(R.id.callTextView)
        }
    }
}
