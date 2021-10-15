package com.ankur.costats

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import util.ConnectionManager

class DisplayData : AppCompatActivity() {

    lateinit var txtTotalCases2: TextView
    lateinit var txtConfirmedCases2: TextView
    lateinit var txtDischargedCases2: TextView
    lateinit var txtDeathCases2: TextView
    lateinit var textViewStateName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        txtTotalCases2=findViewById(R.id.txtTotalCases2)
        txtConfirmedCases2=findViewById(R.id.txtConfirmedCases2)
        txtDischargedCases2=findViewById(R.id.txtDischargedCases2)
        txtDeathCases2=findViewById(R.id.txtDeathCases2)
        textViewStateName=findViewById(R.id.textViewStateName)

        var orderNumber=intent.getIntExtra("key",0)

        val queue= Volley.newRequestQueue(this@DisplayData)
        val url="https://api.rootnet.in/covid19-in/stats/latest"
        if(ConnectionManager().checkConnectivity(this@DisplayData))
        {
            val jsonObjectRequest=object : JsonObjectRequest(Method.GET,url,null, Response.Listener {

                try {
                    val data1=it.getJSONObject("data")
                    val regionalArray=data1.getJSONArray("regional")
                    val data=regionalArray.getJSONObject(orderNumber)

                    textViewStateName.text=data.getString("loc")
                    txtTotalCases2.text=data.getString("totalConfirmed")
                    txtConfirmedCases2.text=data.getString("confirmedCasesIndian")
                    txtDischargedCases2.text=data.getString("discharged")
                    txtDeathCases2.text=data.getString("deaths")
                }
                catch (e: JSONException)
                {
                    Toast.makeText(this@DisplayData, "Some Unexpected Error Occurred1!", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this@DisplayData, "Some Unexpected Error Occurred2!", Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/Json"
                    //headers["token"] = "5419298965184cfd99bfe3b1348f61af"

                    return headers
                }// sending tokens and stuffs
            }
            queue.add(jsonObjectRequest)

        }
        else
        {
            val dialog= AlertDialog.Builder(this@DisplayData)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not found")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
            }// if internet connection not found then dialog pops, positive button
            dialog.setNegativeButton("Cancel"){text,listener->

            }// negative button
            dialog.create()
            dialog.show()
        }

    }

}