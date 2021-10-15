package com.ankur.costats

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class DownloadCertificate : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_certificate)

        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE

        val secondsDelayed = 1
        Handler().postDelayed(Runnable {

                val intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+919013151515" +
                        "&text=Download Certificate"))
                progressBar.visibility=View.GONE
                startActivity(intent)

        }, (secondsDelayed * 3000).toLong())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)
        {
            R.id.StatewiseStatus -> {
                val intent = Intent(this@DownloadCertificate, StateWise::class.java)
                startActivity(intent)
                finish()
                return true
            }
            R.id.CasesAcrossIndia -> {
                val intent = Intent(this@DownloadCertificate, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
            val intent= Intent(this@DownloadCertificate, MainActivity::class.java)
            startActivity(intent)
            finish()
    }


    /*private fun isWhatsappInstalled(): Boolean {
        val packageManager = packageManager
        val whatsappInstalled: Boolean
        whatsappInstalled = try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return whatsappInstalled
    }*/

}