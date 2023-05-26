package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.app.fitlife.data.AppDataBase
import com.app.fitlife.data.User
import com.app.fitlife.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityLogin : AppCompatActivity() {

    private lateinit var database: AppDataBase
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)
        setSupportActionBar(findViewById(R.id.toolbar_tela_principal))
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)

        database = (application as FitLifeApplication).getAppDataBase()
        dao = database.userDao()
        loginButton.setOnClickListener {
            val loginEditText: EditText = findViewById(R.id.loginEditText)
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            isValidCredentials(login, password)

        }

        registerButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidCredentials(login: String, password: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            val user = dao.getUserByEmail(login)
            withContext(Dispatchers.Main) {
                if (user != null && user.password == password) {
                    // Valid credentials, proceed with login
                    Toast.makeText(applicationContext, "Bem vindo! Logado com sucesso.", Toast.LENGTH_SHORT)
                        .show()
                    // Proceed to the next activity or perform any other action
                    val intent = TelaPrincipalBotoesCalculo.start(applicationContext, user)
                    startActivity(intent)
                } else {
                    // Invalid credentials
                    Toast.makeText(
                        applicationContext,
                        "Credenciais invalidas por favor tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


    }
}
