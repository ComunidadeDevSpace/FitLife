package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import kotlinx.coroutines.launch

class TelaPrincipalBotoesCalculo : AppCompatActivity() {
    companion object{
        fun start(context: Context, user : User) : Intent {
            return Intent(context, TelaPrincipalBotoesCalculo::class.java).apply {
                putExtra("EXTRA_RESULT", user)
            }
        }
    }
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal_botoes_calculo)
        setSupportActionBar(findViewById(R.id.toolbar))

        //Habilitando a tela principal quando clica no botão IMC

        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User

        //Calculo do IMC

        val btnIMC : Button = findViewById(R.id.btnIMC)
        btnIMC.setOnClickListener{
            val weight = userData.weight
            val height = userData.height
            val result = weight.toFloat() / (height.toFloat() * height.toFloat())
            val intent = Intent(this, ResultadoIMC::class.java).apply {
                putExtra("EXTRA_RESULT", result)
                println(result)
            }
            startActivity(intent)
        }

//        println(userData)
//        lifecycleScope.launch{
//           val user = dao.getUserByEmail(userData.email)
//            println(user)
//        }


        // Habilitar botão de voltar no ToolBar
        supportActionBar?.setHomeButtonEnabled(true)
        // Mostrar botão de voltar no ToolBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Recover data from the DataRoom

//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDataBase::class.java, "database-fitlife"
//        ).build()
//
//        dao = db.userDao()
////        dao.getUserByEmail()


//        val fakeData = FakeData().getData()
//        val btnCalories: Button = findViewById(R.id.btn_calories)
//        btnCalories.setOnClickListener {
//            val intent = Intent(this, CaloriesResult::class.java).apply {
//                putExtra("EXTRA_RESULT", fakeData)
//            }
//            startActivity(intent)
//        }

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