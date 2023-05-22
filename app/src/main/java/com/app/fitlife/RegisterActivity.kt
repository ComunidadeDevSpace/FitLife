package com.app.fitlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val registerButton: Button = findViewById(R.id.registerButton)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)

        registerButton.setOnClickListener {
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString()

            if (isValidRegistration(password, email)) {
                // Lógica para registrar o usuário no sistema
                // Por exemplo, você pode salvar os dados em um banco de dados ou fazer uma requisição de API para criar o usuário
                Toast.makeText(this, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a atividade de registro e volta para a tela anterior
                // Avançar para a próxima tela
                val intent = Intent(this, MainActivity_Login::class.java)
                startActivity(intent)

                finish() // Fecha a atividade de registro e volta para a tela anterior
            } else {
                Toast.makeText(this, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isValidRegistration(password: String, email: String): Boolean {
        // Lógica para validar os campos de registro
        // Verifique se os campos estão preenchidos adequadamente
        // Neste exemplo, apenas verificamos se ambos os campos não estão vazios
        return password.isNotEmpty() && email.isNotEmpty()
    }

}
