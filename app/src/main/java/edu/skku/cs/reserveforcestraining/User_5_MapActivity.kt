package edu.skku.cs.reserveforcestraining

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.util.*

class User_5_MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var button_back: ImageButton
    private lateinit var trainingCampListView: ListView
    private val trainingCampList = mutableListOf<TrainingCamp>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_5_map)
        button_back = findViewById(R.id.imageButton_back)
        trainingCampListView = findViewById(R.id.listview_maplist)

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 국방부_예비군 훈련장 정보 (제공기관 : 국방부)
        val url = "https://openapi.mnd.go.kr/${BuildConfig.MND_API_KEY}/xml/DS_AMRSV_TRNGCMP_RGHMAP/1/90/"
        fetchXMLData(url)

        // 뒤로가기 버튼
        button_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    // XML데이터 처리
    private fun fetchXMLData(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val xmlData = downloadXML(url)
            val addressList = getAddressListFromXML(xmlData)

            runOnUiThread {
                trainingCampList.clear()
                addressList?.let {
                    trainingCampList.addAll(it)
                    updateTrainingCampListView()

                    // 마커 표시
                    for (trainingCamp in trainingCampList) {
                        showMarkerOnMap(trainingCamp.address)
                    }
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

    private fun getAddressListFromXML(xmlData: String?): List<TrainingCamp>? {
        val addressList = mutableListOf<TrainingCamp>()

        val xmlDoc =
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(InputSource(StringReader(xmlData)))
        val nodeList = xmlDoc.getElementsByTagName("row")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val addressElement = element.getElementsByTagName("addr").item(0) as Element
                val address = addressElement.textContent
                val nameElement = element.getElementsByTagName("trngcmp").item(0) as Element
                val name = nameElement.textContent
                val linkElement = element.getElementsByTagName("rghmap").item(0) as Element
                val link = linkElement.textContent
                addressList.add(TrainingCamp(name, address, link))
            }
        }
        return addressList
    }

    // 지도에 마커 표시
    private fun showMarkerOnMap(address: String) {
        val geocoder = Geocoder(this, Locale.KOREA)
        val addressList = geocoder.getFromLocationName(address, 1)
        if (addressList?.isNotEmpty() == true) {
            val location = addressList[0]
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng).title(address))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
    }

    data class TrainingCamp(
        val name: String,
        val address: String,
        val imageUrl: String
    )

    private fun updateTrainingCampListView() {
        val adapter = TrainingCampAdapter(this, trainingCampList)
        trainingCampListView.adapter = adapter
    }

    private inner class TrainingCampAdapter(
        private val context: Context,
        private val trainingCampList: List<TrainingCamp>
    ) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val viewHolder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.user_5_map_list, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }

            val trainingCamp = trainingCampList[position]

            viewHolder.nameTextView.text = trainingCamp.name
            viewHolder.addressTextView.text = trainingCamp.address
            viewHolder.linkTextView.text = trainingCamp.imageUrl
            // 부대명 클릭 시 해당 주소로 지도 이동
            viewHolder.nameTextView.setOnClickListener {
                moveMapToLocation(trainingCamp.address)
            }
            // 부대 주소 클릭 시 해당 주소로 지도 이동
            viewHolder.addressTextView.setOnClickListener {
                moveMapToLocation(trainingCamp.address)
            }
            // 링크 클릭 시 약도로 크롬 이동
            viewHolder.linkTextView.setOnClickListener {
                openLink(trainingCamp.imageUrl)
            }
            return view
        }

        // 지도 이동
        private fun moveMapToLocation(address: String) {
            val geocoder = Geocoder(context, Locale.KOREA)
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList?.isNotEmpty() == true) {
                val location = addressList[0]
                val latLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            }
        }

        private fun openLink(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        override fun getItem(position: Int): Any {
            return trainingCampList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return trainingCampList.size
        }

        private inner class ViewHolder(view: View) {
            val nameTextView: TextView = view.findViewById(R.id.nameTextView)
            val addressTextView: TextView = view.findViewById(R.id.addressTextView)
            val linkTextView: TextView = view.findViewById(R.id.linkTextView)
        }
    }
}
