package com.bernardooechsler.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println()

        // Recuperando as ID's do UI components
        val editTextAltura: EditText = findViewById(R.id.edit_text_altura)
        val editTextPeso: EditText = findViewById(R.id.edit_text_peso)
        val buttonCalcular: Button = findViewById(R.id.button_calcular)

        // Criando o click no botao
        buttonCalcular.setOnClickListener {

            // Pegando o input do usuario e transformando em String (toString())
            val alturaStr = editTextAltura.text.toString()
            val pesoStr = editTextPeso.text.toString()

            // Checando se os edit texts nao estao vazios, assim, podendo fazer o calculo do IMC
            if (alturaStr.isNotEmpty() && pesoStr.isNotEmpty()) {
                // Pegando as variaveis: Strings e transformando and Float
                val altura = alturaStr.toFloat()
                val peso = pesoStr.toFloat()
                // Formula do IMC
                val resultado = peso / (altura * altura)

                // Formatando o resultado para 2 casas decimais
                val resultadoFormatado = "%.2f".format(resultado).toFloat()

                val intent = Intent(this, ResultadoActivity::class.java)
                    .apply {
                        putExtra("Resultado", resultadoFormatado)
                    }
                startActivity(intent)

                // Toast eh um aviso que aparece na tela, que nesse caso, se o usuario nao preencher todos
                // os campos, ele recebera um aviso na tela pedindo para preencher.
            } else {
                Toast.makeText(this, "Preencher todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}