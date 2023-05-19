package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.app.fitlife.R.*
import com.app.fitlife.data.User

class ResultadoIMC : AppCompatActivity() {

    companion object{
        fun start(context: Context, user : User) : Intent {
            return Intent(context, ResultadoIMC::class.java).apply {
                putExtra("EXTRA_RESULT", user)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_resultado_imc)
        setSupportActionBar(findViewById(R.id.toolbar))

        val userDataNome = intent?.getSerializableExtra("EXTRA_RESULT") as User
        val nome = findViewById<TextView>(R.id.tv_nome_user)
        nome.text = userDataNome.name

        /*
        class ResultActivity : AppCompatActivity() {
            val dao = FakeData()
        }*/

        val tvResult = findViewById<TextView>(id.textview_resultado)
        val tvClassification = findViewById<TextView>(id.textview_classificacao)

        val result = intent.getFloatExtra("EXTRA_RESULT", 0.1f)

        tvResult.text = result.toString()

        val classificacao = if (result < 18.5f) {
            "Abaixo do Peso"
        } else if (result in 18.5f..24.9f) {
            "Normal"
        } else if (result in 25f..29.9f) {
            "Sobrepeso"
        } else if (result in 30f..34.9f) {
            "Obesidade Grau I"
        } else if (result in 30f..34.9f) {
            "Obesidade Grau I"
        } else if (result in 35f..39.9f) {
            "Obesidade Grau II"
        } else {
            "Obesidade Grau III"
        }

        tvClassification.text = classificacao

        // Habilitar botão de voltar no ToolBar
        supportActionBar?.setHomeButtonEnabled(true)
        // Mostrar botão de voltar no ToolBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}