package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarUsuarioActivity : AppCompatActivity() {
    // Instancias de componentes gráficos
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var telefono: EditText
    private lateinit var contrasena: EditText
    private lateinit var crear_usuario: Button
    private lateinit var regreso: Button

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_usuario)

        // Vinculación de componentes
        nombre = findViewById(R.id.edtName)
        apellido = findViewById(R.id.edtSurname)
        telefono = findViewById(R.id.edtPhone)
        contrasena = findViewById(R.id.edtPassword)
        crear_usuario = findViewById(R.id.btnAdd)
        regreso = findViewById(R.id.btnReturn)

        // Evento del botón para registrar usuario en Firestore
        crear_usuario.setOnClickListener {
            val userName = nombre.text.toString().trim()
            val userSurname = apellido.text.toString().trim()
            val userPhone = telefono.text.toString().trim()
            val userPassword = contrasena.text.toString().trim()

            if (userName.isEmpty() || userSurname.isEmpty() || userPhone.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = hashMapOf(
                "nombre" to userName,
                "apellido" to userSurname,
                "telefono" to userPhone,
                "contrasena" to userPassword
            )

            //Se agrega un nuevo documento dentro de la colección (tabla) de empleados
            db.collection("empleados")  // Nombre de la colección en Firestore
                .add(usuario)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Usuario registrado con ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                    // Limpiar los campos después de registrar
                    nombre.text.clear()
                    apellido.text.clear()
                    telefono.text.clear()
                    contrasena.text.clear()
                    //Enfoque en el nombre
                    nombre.requestFocus()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Evento del botón regresar
        regreso.setOnClickListener {
            val intent = Intent(this@RegistrarUsuarioActivity, Selecionusuario::class.java)
            startActivity(intent)
        }
    }
}
