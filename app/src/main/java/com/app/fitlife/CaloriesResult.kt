package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.app.fitlife.data.User

class CaloriesResult : AppCompatActivity() {

    companion object{
        fun start(context: Context, result: Double, user: User) : Intent {
            return Intent(context, CaloriesResult::class.java).apply {
                putExtra("EXTRA_CALORIES_RESULT", result)
                putExtra("EXTRA_RESULT", user)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calories_result)
        setSupportActionBar(findViewById(R.id.toolbar_tela_principal))

        val result = intent.getDoubleExtra("EXTRA_CALORIES_RESULT", 0.1)
        val userDataNome = intent?.getSerializableExtra("EXTRA_RESULT") as User
        val nome = findViewById<TextView>(R.id.tv_nome_user)
        nome.text = userDataNome.name

        val objetivo = findViewById<TextView>(R.id.tv_objetivo)
        objetivo.text = userDataNome.goal

        val tvResult = findViewById<TextView>(R.id.textview_result)
        tvResult.text = result.toString()

        // Habilitar botão de voltar no ToolBar
        supportActionBar?.setHomeButtonEnabled(true)
        // Mostrar botão de voltar no ToolBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // fechar a tela atual
        finish()
        return super.onOptionsItemSelected(item)

    }
}