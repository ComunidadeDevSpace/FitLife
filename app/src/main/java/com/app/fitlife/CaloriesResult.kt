package com.app.fitlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CaloriesResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calories_result)

        val tvResult : TextView = findViewById(R.id.tv_result)

        val result = intent.getDoubleExtra("EXTRA_RESULT", 0.1)
        tvResult.text = result.toString()

    }
}