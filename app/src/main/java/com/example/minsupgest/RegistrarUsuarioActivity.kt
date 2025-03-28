package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistrarUsuarioActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var telefono: EditText
    private lateinit var contrasena: EditText
    private lateinit var crear_usuario: Button
    private lateinit var regreso: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_usuario)

        //Vinculación a componentes
        nombre = findViewById(R.id.edtName)
        apellido = findViewById(R.id.edtSurname)
        telefono = findViewById(R.id.edtPhone)
        contrasena = findViewById(R.id.edtPassword)
        crear_usuario = findViewById(R.id.btnAdd)
        regreso = findViewById(R.id.btnReturn)

        //Eventos escucha
        crear_usuario.setOnClickListener {
            Toast.makeText(this@RegistrarUsuarioActivity, "Usuario Registrado", Toast.LENGTH_SHORT).show()
        }
        regreso.setOnClickListener {
            val intent = Intent(this@RegistrarUsuarioActivity, Selecionusuario::class.java)
            startActivity(intent)
        }
    }

}