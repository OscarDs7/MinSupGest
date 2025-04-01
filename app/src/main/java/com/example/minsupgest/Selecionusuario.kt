package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Selecionusuario : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var gerente: Button
    private lateinit var empleado: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selecionusuario)

        //Vinculación a componentes gráficos
        gerente = findViewById(R.id.btnGerente)
        empleado = findViewById(R.id.btnEmpleado)

        //Eventos escucha para cambiar a nueva ventana
        gerente.setOnClickListener {
            val intent = Intent(this@Selecionusuario, Iniciogerente::class.java)
            startActivity(intent)
        }
        empleado.setOnClickListener {
            val intent = Intent(this@Selecionusuario, Iniciosessionus::class.java)
            startActivity(intent)
        }

    }
}