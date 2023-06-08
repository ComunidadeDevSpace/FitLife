package com.app.fitlife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class detalhesIMC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_imc)


        val nomeTv = findViewById<TextView>(R.id.tv_nome_user_imc)
        val tvResultIMC = findViewById<TextView>(R.id.textview_resultado)

        val imcResultado = intent.getFloatExtra("EXTRAIMC_RESULT", 0.1f)
        val name = intent.getStringExtra("EXTRA_NAME")

        nomeTv.text = name
        tvResultIMC.text = imcResultado.toString()
    }
}