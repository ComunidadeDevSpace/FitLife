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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TelaPrincipalBotoesCalculo : AppCompatActivity() {

    private lateinit var database: AppDataBase

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
        database = (application as FitLifeApplication).getAppDataBase()

        dao = database.userDao()
        val nome = findViewById<TextView>(R.id.tv_nome_user)

        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User?


        lifecycleScope.launch(Dispatchers.IO) {
            user = dao.getUserByEmail(userData!!.email)
            withContext(Dispatchers.Main) {
                nome.text = user!!.name
            }
        }

//Calculo do IMC
        val btnIMC: Button = findViewById(R.id.btnIMC)
        btnIMC.setOnClickListener {
            val weight = user!!.weight.toFloat()
            val height = user!!.height.toFloat()
            val result = weight / (height * height)
            val intent = ResultadoIMC.start(this, result, user!!)
            startActivity(intent)

        }
        val btnCalories: Button = findViewById(R.id.btn_calories)
        btnCalories.setOnClickListener {
            val intent = CaloriesResult.start(this, user!!)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_tela_principal, menu)
        return true
    }

    // Opções do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_editar_perfil -> {
                val intent = Intent(this, SignUpActivity::class.java).apply {
                    putExtra("EXTRA_USER_DATA", user)
                }
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

    private fun womanCaloriesCalc(weight: Float, height: Float, age: Int): Float {
        val weightConvert = weight.toFloat()
        val heightConvert = height.toFloat()
        val ageConvert = age.toInt()
        val result: Float = 655.1f + (weight * 9.6f) + (height * 1.8f) - (age * 4.7f)

        return result
    }
}