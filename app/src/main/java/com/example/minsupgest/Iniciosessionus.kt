package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Iniciosessionus : AppCompatActivity() {
    //Instancias a componentes
    private lateinit var acceder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciosessionus)
        //Vinculación a componente
        acceder = findViewById(R.id.btnenter)

        acceder.setOnClickListener {
            val intent = Intent(this@Iniciosessionus, MenuEmpleadoActivity::class.java)
            startActivity(intent)
        }
    }
}