package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MenuEmpleados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        /*
        // Referencia para acceder a otra Activity
        val btnEstadisticas: Button = findViewById(R.id.btnEstadisticas)
        val btnInventario: Button = findViewById(R.id.btnInventario)
        val btnRecomendaciones: Button = findViewById(R.id.btnRecomendaciones)
        val btnEmpleados: Button = findViewById(R.id.btnEmpleados)

        imgEstadisticas.setOnClickListener {
            val intent = Intent(this, EstadisticasActivity::class.java)
            startActivity(intent)
        }

        imgInventario.setOnClickListener {
            val intent = Intent(this, InventarioActivity::class.java)
            startActivity(intent)
        }

        imgRecomendaciones.setOnClickListener {
            val intent = Intent(this, RecomendacionesActivity::class.java)
            startActivity(intent)
        }*/
    }
}
