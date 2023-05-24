package com.app.fitlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splashscreen)
        Handler().postDelayed({
            // Finaliza a exibição da tela de Splash
            finish()

            // Inicia a Activity de Login
            val intent = Intent(this, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }, 2000 )
    }
}
