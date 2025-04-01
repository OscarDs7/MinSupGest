package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Iniciogerente : AppCompatActivity() {
    //Instancias a componentes
    private lateinit var acceder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciogerente)

        //Vinculación a componente
        acceder = findViewById(R.id.btnenter2)

        acceder.setOnClickListener {
            val intent = Intent(this@Iniciogerente, MenuAdminActivity::class.java)
            startActivity(intent)
        }

    }
}