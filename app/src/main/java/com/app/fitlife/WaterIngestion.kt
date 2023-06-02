package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.app.fitlife.data.User

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
        val tvResult = findViewById<TextView>(R.id.textview_result)
        tvResult.text = result.toString()
        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User?
        val nome: TextView = findViewById(R.id.tv_name)
        nome.text = userData?.name
    }
}