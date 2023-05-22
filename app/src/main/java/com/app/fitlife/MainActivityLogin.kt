package com.app.fitlife

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.app.fitlife.data.User

class MainActivityLogin : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            val loginEditText: EditText = findViewById(R.id.loginEditText)
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValidCredentials(login, password)) {
                val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
                welcomeTextView.text = "Bem-vindo, $login!"

                val intent = Intent(this,TelaPrincipalBotoesCalculo::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidCredentials(login: String, password: String): Boolean {
        // Implemente sua lógica de validação das credenciais aqui
        // Por exemplo, você pode verificar se o login e a senha correspondem a um registro existente no banco de dados
        // Neste exemplo, vamos considerar válido se o login for "admin" e a senha for "123456"
        return login == "admin" && password == "123456"
    }
}