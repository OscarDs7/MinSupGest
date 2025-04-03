package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Iniciosessionus : AppCompatActivity() {
    // Instancias a componentes gráficos
    private lateinit var employee: EditText
    private lateinit var password: EditText
    private lateinit var enter: Button
    private lateinit var exit: Button

    // Instancia de Firestore (conexión con Firestore)
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciosessionus)

        // Vinculación a los componentes gráficos del xml
        employee = findViewById(R.id.etxtusuario)
        password = findViewById(R.id.etxtcontra)
        enter = findViewById(R.id.btnenter)
        exit = findViewById(R.id.btnsalir)

        // Eventos en los botones
        enter.setOnClickListener {
            //Obtención de los datos en las cajas de texto
            val user = employee.text.toString().trim()
            val passw = password.text.toString().trim()
            //Validación de que no existan campos vacíos
            if (user.isEmpty() || passw.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                validarCredenciales(user, passw)
            }
        }

        exit.setOnClickListener {
            val intent = Intent(this@Iniciosessionus, Selecionusuario::class.java)
            startActivity(intent)
        }
    }

    // Méthod para validar credenciales en Firestore
    private fun validarCredenciales(user: String, passw: String) {
        db.collection("empleados")
            .whereEqualTo("nombre", user)  // Filtrar por nombre de usuario
            .whereEqualTo("contrasena", passw) // Filtrar por contraseña
            .get()
            .addOnSuccessListener { documents ->
                //Si hay documentos / registros dentro de la colección que valide y luego te redirija
                if (!documents.isEmpty) {
                    Toast.makeText(this, "Bienvenido ${user}!", Toast.LENGTH_SHORT).show()

                    // Redirigir al usuario si las credenciales son correctas
                    val intent = Intent(this@Iniciosessionus, RegistrarUsuarioActivity::class.java)
                    startActivity(intent)
                    finish() // Cerrar esta actividad
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al consultar Firestore: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
