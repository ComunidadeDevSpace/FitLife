package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import kotlinx.coroutines.launch

class TelaPrincipalBotoesCalculo : AppCompatActivity() {
    companion object {
        fun start(context: Context, user: User): Intent {
            return Intent(context, TelaPrincipalBotoesCalculo::class.java).apply {
                putExtra("EXTRA_RESULT", user)
            }
        }
    }

    private lateinit var dao: UserDao
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal_botoes_calculo)
        setSupportActionBar(findViewById(R.id.toolbar_tela_principal))


        //Recover data from the DataRoom
        val db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "database-fitlife"
        ).build()

        dao = db.userDao()

        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User?
        lifecycleScope.launch {
            user = dao.getUserByEmail(userData!!.email)
        }
        val nome = findViewById<TextView>(R.id.tv_nome_user)
        nome.text = user!!.name


        //Calculo do IMC

        val btnIMC: Button = findViewById(R.id.btnIMC)
        btnIMC.setOnClickListener {
            val weight = user!!.weight.toFloat()
            val height = user!!.height.toFloat()
            val result = weight / (height * height)
            val intent = Intent(this, ResultadoIMC::class.java).apply {
                putExtra("EXTRA_RESULT", result)

            }
            startActivity(intent)
        }


        val btnCalories: Button = findViewById(R.id.btn_calories)
        btnCalories.setOnClickListener {
            val intent = Intent(this, CaloriesResult::class.java).apply {
                putExtra("EXTRA_RESULT", userData)
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

    // Opções do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_editar_perfil -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_logout -> {
                val intent = Intent(this, MainActivityLogin::class.java)
                startActivity(intent)
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