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
import com.app.fitlife.data.User
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class TelaPrincipalBotoesCalculo : AppCompatActivity() {
    companion object{
        fun start(context: Context, user : User) : Intent {
            return Intent(context, TelaPrincipalBotoesCalculo::class.java).apply {
                putExtra("EXTRA_RESULT", user)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal_botoes_calculo)
        setSupportActionBar(findViewById(R.id.menu_editar_perfil))
        //Habilitando a tela principal quando clica no botão IMC
        val userData = intent?.getSerializableExtra("EXTRA_RESULT") as User?
        val btnCalories: Button = findViewById(R.id.btn_calories)
        val userButtonRecover = SignUpActivity()

        btnCalories.setOnClickListener {
//
        }

        if (userData?.gender == "Masculino") {
//                when (userData.goal) {
//                    "Manter" -> {
//                         intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesMenCalc(userData) + 500)
//                        }
//                        startActivity(intent)
//                    }
//                    "Ganhar" -> {
//                         intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesMenCalc(userData) + 1000)
//                        }
//                        startActivity(intent)
//                    }
//                    "Emagrecer" -> {
//                          intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesMenCalc(userData) - 500)
//                        }
//                        startActivity(intent)
//                    }
//                }
//
//            } else if(userData?.gender == "Feminino") {
//                when (userData.goal) {
//                    "Manter" -> {
//                         intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesWomanCalc(userData) + 500)
//                        }
//                        startActivity(intent)
//                    }
//                    "Ganhar" -> {
//                         intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesWomanCalc(userData) + 1000)
//                        }
//                        startActivity(intent)
//                    }
//                    "Emagrecer" -> {
//                         intent = Intent(this, CaloriesResult::class.java).apply {
//                            putExtra("EXTRA_RESULT", caloriesWomanCalc(userData) - 500)
//                        }
//                        startActivity(intent)
//                    }
//                }
//            }


            // Habilitar botão de voltar no ToolBar
            supportActionBar?.setHomeButtonEnabled(true)
            // Mostrar botão de voltar no ToolBar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
            R.id.menu_editar_perfil -> {
                //colocar aqui para abrir tela de cadastro
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

}
