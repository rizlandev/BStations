package com.rizlan_dev.bstations

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.rizlan_dev.bstations.adapter.CustomListAdapter
import com.rizlan_dev.bstations.controller.ApiGetPostHelper
import com.rizlan_dev.bstations.model.Stations
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity :  AppCompatActivity() {

    lateinit var Lv_stations: ListView
    lateinit var pro_bar: ProgressBar
    private var stations_list = ArrayList<Stations>()


    // Checking Internet is available or not
    private val isNetworkConnected: Boolean
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null


    companion object {
        var GetPostJSONApi = "http://www.poznan.pl/mim/plan/map_service.html?mtype=pub_transport&co=stacje_rowerowe"
    }

    // Show BackButton on Actionbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = "BIKE STATIONS"
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }

        // findViewById and set view id
        pro_bar = findViewById(R.id.progress_bar)
        Lv_stations = findViewById(R.id.Lv_stations)

        if (isNetworkConnected) {
            // Call AsyncTask for getting developer list from server (JSON Api)
            getDataFromApi().execute()
        } else {
            Toast.makeText(applicationContext, "No Internet Connection Yet!", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class getDataFromApi : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            // Show Progressbar for batter UI
            pro_bar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg voids: Void): String {

            // Here is post Json api example
            val sendParams = HashMap<String, String>()
            // Send parameters and value on JSON api
            // sendParams["Name"] = "JigarPatel"

            // Only Get JSON api send HashMap null see below comment example
            // return ApiGetPostHelper.SendParams(GetPostJSONApi, null);
            // Send the HttpPostRequest by HttpURLConnection and receive a Results in return string
            return ApiGetPostHelper.SendParams(GetPostJSONApi, sendParams)
        }

        override fun onPostExecute(results: String?) {
            // Hide Progressbar
            pro_bar.visibility = View.GONE

            if (results != null) {
                // See Response in Logcat for understand JSON Results and make DeveloperList
                Log.i("onPostExecute: ", results)
            }

            try {
                // Create JSONObject from string response if your response start from Array [ then create JSONArray
                val rootJsonObject = JSONObject(results)
//                {
//                    "geometry": {
//                    "coordinates": [
//                    16.911623,
//                    52.402831
//                    ],
//                    "type": "Point"
//                },
//                    "id": "15570",
//                    "type": "Feature",
//                    "properties": {
//                    "free_racks": "14",
//                    "bikes": "4",
//                    "label": "Poznań Główny",
//                    "bike_racks": "18",
//                    "updated": "2022-09-03 23:40"
//                }
//                }

                val stationArray = rootJsonObject.getString("features")

                val mJsonArray = JSONArray(stationArray)
                for (i in 0 until mJsonArray.length()) {
                    // Get single JSON object node
                    val sObject = mJsonArray.get(i).toString()
                    val mItemObject = JSONObject(sObject)

                    val geometry = JSONObject(mItemObject.getString("geometry"))
                    val coordinatesArray = geometry.getString("coordinates")
                    val codeJsonArray = JSONArray(coordinatesArray)
                    var lat: String="52"
                    var lon: String="16"

                    if(codeJsonArray.toString().length>0){

                        lat = codeJsonArray.get(0).toString()
                        lon = codeJsonArray.get(1).toString()
                    }

                    // Get String value from json object
                    val id = mItemObject.getString("id")

                    val prop = JSONObject(mItemObject.getString("properties"))
                    val lable =prop.getString("label")
                    val avb_bike=prop.getString("bikes")

                    val avb_place=prop.getString("bike_racks")

                    val x = Stations(lat, lon, id, lable,avb_bike, avb_place)
                    stations_list.add(x)
                }

                // This is simple Adapter (android widget) for ListView

                val adapter = CustomListAdapter(this@MainActivity, stations_list)

                Lv_stations.setAdapter(adapter)

                Lv_stations.setOnItemClickListener { parent, view, position, id ->
                    val element = adapter.getItem(position)

                   // Toast.makeText(applicationContext, "Selected item is " + element.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)



                    startActivity(intent)



                }




            } catch (e: JSONException) {
                Toast.makeText(applicationContext, "Something wrong. Try Again!"+ e.toString(), Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }
    }


}
