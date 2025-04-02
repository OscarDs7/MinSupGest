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
    // Instancias a componentes gráficos
    private lateinit var gerente: EditText
    private lateinit var contrasena: EditText
    private lateinit var ingresar: Button
    private lateinit var salir: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciogerente)

        // Vinculación a los componentes
        gerente = findViewById(R.id.etxtusuario2)
        contrasena = findViewById(R.id.etxtcontra2)
        ingresar = findViewById(R.id.btnenter2)
        salir = findViewById(R.id.btnsalir)

        // Evento de botón ingresar
        ingresar.setOnClickListener {
            val user = gerente.text.toString().trim()
            val passwd = contrasena.text.toString().trim()

            if (user.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else if (validarCredenciales(user, passwd)) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Iniciogerente, MenuAdminActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        // Evento de botón salir
        salir.setOnClickListener {
            val intent = Intent(this@Iniciogerente, Selecionusuario::class.java)
            startActivity(intent)
        }
    }

    // Método para validar credenciales (debes cambiarlo por una validación real)
    private fun validarCredenciales(user: String, passwd: String): Boolean {
        return user == "admin" && passwd == "1234" // Aquí puedes implementar validación con una base de datos
    }
}
