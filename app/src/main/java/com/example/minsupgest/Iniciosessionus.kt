package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Iniciosessionus : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var employee: EditText
    private lateinit var password: EditText
    private lateinit var enter: Button
    private lateinit var exit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciosessionus)

        //Vinculación a los componentes
        employee = findViewById(R.id.etxtusuario)
        password = findViewById(R.id.etxtcontra)
        enter = findViewById(R.id.btnenter)
        exit = findViewById(R.id.btnsalir)

        //Eventos en los botones
        enter.setOnClickListener {
            Toast.makeText(this@Iniciosessionus, "Has ingresado como Empleado!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@Iniciosessionus, MenuEmpleadoActivity::class.java)
            startActivity(intent)
        }
        exit.setOnClickListener {
            val intent = Intent(this@Iniciosessionus, Selecionusuario::class.java)
            startActivity(intent)
        }

    }
}