package com.bernardooechsler.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Recuperando os ID's do xml
        val resultadoFinal = findViewById<TextView>(R.id.text_view_resultado)
        val classificaoFinal = findViewById<TextView>(R.id.text_view_classificacao)

        // Recuperando o resultado (Float) da MainActivity
        val resultado = intent.getFloatExtra("Resultado", 0.1f)

        // Pegando o texto e transformando em String
        resultadoFinal.text = resultado.toString()

        // Checando em qual categoria o usuario esta baseado no seu resultado do IMC
        var classificacao = ""
        if (resultado < 18.5f) {
            classificacao = "ABAIXO DO PESO"
        } else if (resultado in 18.5f..24.9f) {
            classificacao = "NORMAL"
        } else if (resultado in 25f..29.9f) {
            classificacao = "SOBREPESO"
        } else if (resultado in 30f..34.9f) {
            classificacao = "OBESIDADE"
        } else {
            classificacao = "OBESIDADE GRAVE"
        }

        // Setando a classificacao para o TextView
        classificaoFinal.text = classificacao

    }
}