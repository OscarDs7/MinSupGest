package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Iniciogerente : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var gerente: EditText
    private lateinit var contrasena: EditText
    private lateinit var ingresar: Button
    private lateinit var salir: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciogerente)

        //Vinculación a los componentes
        gerente = findViewById(R.id.etxtusuario2)
        contrasena = findViewById(R.id.etxtcontra2)
        ingresar = findViewById(R.id.btnenter2)
        salir = findViewById(R.id.btnexit2)

        //Eventos
        ingresar.setOnClickListener {
            Toast.makeText(this@Iniciogerente, "Has ingresado como Gerente!", Toast.LENGTH_LONG).show()
        }
        salir.setOnClickListener {
            val intent = Intent(this@Iniciogerente, Selecionusuario::class.java)
            startActivity(intent)
        }


    }
}