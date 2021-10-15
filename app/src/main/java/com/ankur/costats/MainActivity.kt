package com.ankur.costats

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import util.ConnectionManager

class MainActivity : AppCompatActivity() {

    lateinit var txtTotalCases:TextView
    lateinit var txtConfirmedCases:TextView
    lateinit var txtDischargedCases:TextView
    lateinit var txtDeathCases:TextView
    lateinit var txtUpdate:TextView
    lateinit var progressBarMain:ProgressBar
    lateinit var progressLayoutMain:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtTotalCases=findViewById(R.id.txtTotalCases)
        txtConfirmedCases=findViewById(R.id.txtConfirmedCases)
        txtDischargedCases=findViewById(R.id.txtDischargedCases)
        txtDeathCases=findViewById(R.id.txtDeathCases)
        txtUpdate=findViewById(R.id.txtUpdate)
        progressBarMain=findViewById(R.id.progressBarMain)
        progressLayoutMain=findViewById(R.id.progressLayoutMain)

        progressLayoutMain.visibility=View.VISIBLE

        val queue=Volley.newRequestQueue(this@MainActivity)
        val url="https://api.rootnet.in/covid19-in/stats/latest"
        if(ConnectionManager().checkConnectivity(this@MainActivity))
        {
            val jsonObjectRequest=object :JsonObjectRequest(Method.GET,url,null,Response.Listener {

                progressLayoutMain.visibility=View.GONE
                try {
                    val data1=it.getJSONObject("data")
                    val data=data1.getJSONObject("summary")
                    txtTotalCases.text=data.getString("total")
                    txtConfirmedCases.text=data.getString("confirmedCasesIndian")
                    txtDischargedCases.text=data.getString("discharged")
                    txtDeathCases.text=data.getString("deaths")
                    txtUpdate.text=it.getString("lastOriginUpdate").substring(0,10)
                }
                catch (e:JSONException)
                {
                    Toast.makeText(this@MainActivity, "Some Unexpected Error Occurred1!", Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this@MainActivity, "Some Unexpected Error Occurred2!", Toast.LENGTH_SHORT).show()
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
            val dialog= AlertDialog.Builder(this@MainActivity)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)
        {
            R.id.StatewiseStatus->
            {
                val intent=Intent(this@MainActivity,StateWise::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.VaccineCertificate->
            {
                val intent=Intent(this@MainActivity,DownloadCertificate::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}