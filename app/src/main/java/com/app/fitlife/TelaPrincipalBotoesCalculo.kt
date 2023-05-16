package com.app.fitlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button

class TelaPrincipalBotoesCalculo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal_botoes_calculo)
        setSupportActionBar(findViewById(R.id.toolbar))

        //Habilitando a tela principal quando clica no botão IMC
        val fakedata = FakeData().getData()
        val btnIMC : Button = findViewById(R.id.btnIMC)
        btnIMC.setOnClickListener{
            val intent = Intent(this, ResultadoIMC::class.java).apply {
                putExtra("EXTRA_RESULT", fakedata)
            }
            startActivity(intent)
        }

        // Habilitar botão de voltar no ToolBar
        supportActionBar?.setHomeButtonEnabled(true)
        // Mostrar botão de voltar no ToolBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Criar um Menu mas ainda nada acontece se clicar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_tela_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.editar_perfil -> {
                //colocar aqui para abrir tela de cadastro
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}