package com.app.fitlife

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.app.fitlife.SignUpActivity.Companion.REGISTER_REQUEST_CODE
import com.app.fitlife.SignUpActivity.Companion.SOURCE_MAIN
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

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


        val userData = intent.getSerializableExtra("EXTRA_RESULT") as User

        lifecycleScope.launch(Dispatchers.IO) {
            user = dao.getUserByEmail(userData.email)
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

        //Calculo Calorias
        val btnCalories: Button = findViewById(R.id.btn_calories)
        btnCalories.setOnClickListener {
            println(user?.gender)
            if (user?.gender == "Masculino") {
                when (user?.goal) {
                    "Ganhar" -> {
                        val result = caloriesMenCalc(userData) + 1000
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }

                    "Manter" -> {
                        val result = caloriesMenCalc(userData) + 500
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }

                    "Emagrecer" -> {
                        val result = caloriesMenCalc(userData) - 500
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }
                }
            } else if (user?.gender == "Feminino") {
                when (user?.goal) {
                    "Ganhar" -> {
                        val result = caloriesWomanCalc(userData) + 1000
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }

                    "Manter" -> {
                        val result = caloriesWomanCalc(userData) + 500
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }

                    "Emagrecer" -> {
                        val result = caloriesWomanCalc(userData) - 500
                        val intent = CaloriesResult.start(this, result, user!!)
                        startActivity(intent)
                    }
                }
            }
        }
        val btnWaterIngestion : Button = findViewById(R.id.waterIngestion)
        btnWaterIngestion.setOnClickListener{
            val result = user!!.weight.toDouble() * 0.35
            val intent = WaterIngestion.start(this, result, user!!)
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
                    putExtra("source", SOURCE_MAIN)
                }
                startActivityForResult(intent, REGISTER_REQUEST_CODE)
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


    private fun ageCalculate(user: User): Int {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val birthDate = LocalDate.parse(user.birth, formatter)
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years
    }

    private fun caloriesMenCalc(user: User): Double {
        val weight = user.weight.toDouble() * 13.8
        val height = user.height.toDouble() * 5
        val age = ageCalculate(user) * 6.8
        return 66.5 + weight + height - age
    }

    private fun caloriesWomanCalc(user: User): Double {
        val weight = user.weight.toDouble() * 9.6
        val height = user.height.toDouble() * 5
        val age = ageCalculate(user) * 4.7
        return 655.1 + weight + height - age
    }

    private fun waterIngestionCalc (user: User): Double {
        val weight : Double = user.weight.toDouble() * 0.35
        return weight * 0.35
    }
}
