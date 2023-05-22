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

    companion object {
        fun start(context: Context, user: User): Intent {
            return Intent(context, ResultadoIMC::class.java).apply {
                putExtra("EXTRA_RESULT", user)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_resultado_imc)
        setSupportActionBar(findViewById(R.id.toolbar_tela_principal))

        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User
        val nome = findViewById<TextView>(R.id.tv_nome_user)
        nome.text = userData.name

        /*
        class ResultActivity : AppCompatActivity() {
            val dao = FakeData()
        }*/
        val tvClassification = findViewById<TextView>(id.textview_classificacao)
        val tvResult = findViewById<TextView>(id.textview_resultado)


        val result = intent.getFloatExtra("EXTRA_RESULT", 0.1f)
        println(result)


        tvResult.text = result.toString()

        val classificacao = if (result < 18.5f) {
            "Abaixo do Peso"
        } else if (result in 18.5f..24.99f) {
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

        val classifcacaoDesc: TextView = findViewById(id.tv_classificacao_desc)
        classficacaoDescricao(classifcacaoDesc)

    }

    // função para chamar o texto sobre o resultado da classficação do IMC
    fun classficacaoDescricao(tvClassification: TextView): String {


        val classifcacaoDesc = if (tvClassification.text == "Abaixo do Peso") {
            "Procure um médico. Algumas pessoas têm um baixo peso por características do seu organismo e tudo bem. Outras podem estar enfrentando problemas, como a desnutrição. É preciso saber qual é o caso."
        } else if (tvClassification.text == "Normal") {
            "Que bom que você está com o peso normal! E o melhor jeito de continuar assim é mantendo um estilo de vida ativo e uma alimentação equilibrada."
        } else if (tvClassification.text == "Sobrepeso") {
            "Ele é, na verdade, uma pré-obesidade e muitas pessoas nessa faixa já apresentam doenças associadas, como diabetes e hipertensão. Importante rever hábitos e buscar ajuda antes de, por uma série de fatores, entrar na faixa da obesidade pra valer."
        } else if (tvClassification.text == "Obesidade Grau I") {
            "Sinal de alerta! Chegou na hora de se cuidar, mesmo que seus exames sejam normais. Vamos dar início a mudanças hoje! Cuide de sua alimentação. Você precisa iniciar um acompanhamento com nutricionista e/ou endocrinologista."
        } else if (tvClassification.text == "Obesidade Grau II") {
            "Mesmo que seus exames aparentem estar normais, é hora de se cuidar, iniciando mudanças no estilo de vida com o acompanhamento próximo de profissionais de saúde."
        } else {
            "Aqui o sinal é vermelho, com forte probabilidade de já existirem doenças muito graves associadas. O tratamento deve ser ainda mais urgente."
        }

        return classifcacaoDesc
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}