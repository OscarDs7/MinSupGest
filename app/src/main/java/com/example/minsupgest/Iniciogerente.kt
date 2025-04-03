package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Iniciogerente : AppCompatActivity() {
    private lateinit var gerente: EditText
    private lateinit var contrasena: EditText
    private lateinit var ingresar: Button
    private lateinit var salir: Button
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciogerente)
        //Vinculación a los componentes
        gerente = findViewById(R.id.etxtusuario2)
        contrasena = findViewById(R.id.etxtcontra2)
        ingresar = findViewById(R.id.btnenter2)
        salir = findViewById(R.id.btnsalir)
        //Vinculación a la instancia de autenticación de Firebase
        auth = FirebaseAuth.getInstance()

        //Eventos de los botones
        ingresar.setOnClickListener {
            val user = gerente.text.toString().trim()
            val passwd = contrasena.text.toString().trim()

            if (user.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ingresar.isEnabled = false // Deshabilitar botón mientras se valida
            validarCredenciales(user, passwd) //Hacemos validación

        } //fin-ingresar

        //Evento de regreso al menú de selección de usuario
        salir.setOnClickListener {
            val intent = Intent(this@Iniciogerente, Selecionusuario::class.java)
            startActivity(intent)
        }
    }//fin-onCreate

    private fun validarCredenciales(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                ingresar.isEnabled = true // Volver a habilitar el botón

                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                    // Limpiar los campos de texto
                    gerente.text.clear()
                    contrasena.text.clear()

                    val intent = Intent(this@Iniciogerente, MenuAdminActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = when (task.exception?.message) {
                        "There is no user record corresponding to this identifier." -> "El usuario no existe."
                        "The password is invalid or the user does not have a password." -> "Contraseña incorrecta."
                        else -> "Error: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()

                    // Limpiar solo el campo de contraseña (pero mantener el usuario para que no lo reescriba)
                    contrasena.text.clear()
                }
            }
    }

}
