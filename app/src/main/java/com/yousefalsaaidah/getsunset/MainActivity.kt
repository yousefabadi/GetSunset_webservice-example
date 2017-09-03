package com.yousefalsaaidah.getsunset

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun GetSunset(view: View){
        var city = etCityName.text.toString()
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //before excute the task
        }

        override fun doInBackground(vararg p0: String?): String {
            try {
                val url = URL(p0[0])
                val urlConnet = url.openConnection() as HttpURLConnection
                urlConnet.connectTimeout = 7000
                var inString = ConvertStreamToString(urlConnet.inputStream)
                publishProgress(inString)
            }catch (ex:Exception){}
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                val json = JSONObject(values[0])
                var query = json.getJSONObject("query")
                var results = query.getJSONObject("results")
                var channel = results.getJSONObject("channel")
                var astronomy = channel.getJSONObject("astronomy")
                var sunset = astronomy.getString("sunset")

                tvSunsetTime.text = "Sunset time is: "+sunset

            }catch (ex:Exception){}


        }

        override fun onPostExecute(result: String?) {
            //after excute the task
        }

    }

    fun ConvertStreamToString(inputStream:InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String=""
        try{
            do{
                line = bufferReader.readLine()
                if(line!=null){
                    AllString+=line
                }
            }while(line!=null)
            inputStream.close()
        }catch (ex:Exception){}
        return AllString
    }
}
