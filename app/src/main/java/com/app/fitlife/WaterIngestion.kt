package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.app.fitlife.R
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import org.w3c.dom.Text

class WaterIngestion : AppCompatActivity() {

    companion object {

        fun start(context: Context, user: User, result:Double): Intent {
            return Intent(context, WaterIngestion::class.java).apply {
                putExtra("EXTRA_RESULT", user)
                putExtra("EXTRA_WATER_INGESTION_RESULT", result)
            }
        }
    }

//    val btnWaterIngestion : Button = findViewById(R.id.waterIngestion)

//    private lateinit var dao: UserDao
//    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_ingestion)
        val result = intent.getDoubleExtra("EXTRA_WATER_INGESTION_RESULT", 0.1)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        tvResult.text = result.toString()
    }
}