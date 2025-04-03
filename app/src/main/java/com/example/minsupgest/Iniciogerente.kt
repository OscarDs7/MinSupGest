package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class Iniciogerente : AppCompatActivity() {
    // Instancias a componentes gráficos
    private lateinit var gerente: EditText
    private lateinit var contrasena: EditText
    private lateinit var ingresar: Button
    private lateinit var salir: Button
    private lateinit var auth: FirebaseAuth  // Instancia de Firebase Auth

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

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Evento del botón ingresar
        ingresar.setOnClickListener {
            val user = gerente.text.toString().trim()
            val passwd = contrasena.text.toString().trim()

            if (user.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                validarCredenciales(user, passwd)
                Toast.makeText(this, "Ha ingresado como gerente!", Toast.LENGTH_SHORT).show()
            }
        }

        // Evento del botón salir
        salir.setOnClickListener {
            val intent = Intent(this@Iniciogerente, Selecionusuario::class.java)
            startActivity(intent)
        }
    }

    // Función para la autenticación en Firebase
    private fun validarCredenciales(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Iniciogerente, MenuAdminActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra esta actividad para que no se pueda regresar con "atrás"
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
