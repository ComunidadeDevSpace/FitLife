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

        // Habilitar botão de voltar no ToolBar
        supportActionBar?.setHomeButtonEnabled(true)
        // Mostrar botão de voltar no ToolBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //Receiving the button id's from activity_main_button_screen_calc.xml
        val fakeData = FakeData().getData()

        val btnCalories: Button = findViewById(R.id.btn_calories)
        btnCalories.setOnClickListener {
            val intent = Intent(this, CaloriesResult::class.java).apply {
                putExtra("EXTRA_RESULT", fakeData)
            }
            startActivity(intent)
        }

    }

    // Criar um Menu mas ainda nada acontece se clicar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_tela_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_perfil -> {
                //colocar aqui para abrir tela de cadastro
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
//    private fun womanCaloriesCalc(weight: Float, height: Float, age: Int) : Float {
//        val weightConvert = weight.toFloat()
//        val heightConvert = height.toFloat()
//        val ageConvert = age.toInt()
//        val result : Float = 655.1f + (weight * 9.6f) + (height * 1.8f) - (age * 4.7f)
//
//        return result
//    }
}