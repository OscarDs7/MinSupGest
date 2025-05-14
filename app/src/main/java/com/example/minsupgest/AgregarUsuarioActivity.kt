package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AgregarUsuarioActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var contrasena: EditText
    private lateinit var correo: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnSalir: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_usuario)

        //Vinculación a los componentes
        nombre = findViewById(R.id.edtNombre)
        apellido = findViewById(R.id.edtApellido)
        contrasena = findViewById(R.id.edtContrasena)
        correo = findViewById(R.id.edtCorreo)
        btnAgregar = findViewById(R.id.btnAgregarUser)
        btnSalir = findViewById(R.id.btnExit)

        val db = FirebaseFirestore.getInstance() //instancia a la BD
        val empleadosRef = db.collection("empleados") //Acceso a la colección de los productos

        btnAgregar.setOnClickListener {
            val nombreEmpleado = nombre.text.toString().trim()
            val apellidoEmpleado = apellido.text.toString().trim()
            val contrasenaEmpleado = contrasena.text.toString().trim()
            val correoEmpleado = correo.text.toString().trim()

            val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$") //formato de correo válido

            when {
                nombreEmpleado.isEmpty() || apellidoEmpleado.isEmpty() || contrasenaEmpleado.isEmpty() || correoEmpleado.isEmpty() -> {
                    Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show()
                }

                contrasenaEmpleado.length < 4 -> {
                    Toast.makeText(this, "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show()
                }

                !correoEmpleado.matches(emailRegex) -> {
                    Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Verificar si el correo ya existe
                    empleadosRef.whereEqualTo("correo", correoEmpleado).get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // Si no existe, lo agregamos
                                val empleado = hashMapOf(
                                    "nombre" to nombreEmpleado,
                                    "apellido" to apellidoEmpleado,
                                    "contrasena" to contrasenaEmpleado,
                                    "correo" to correoEmpleado
                                )

                                empleadosRef.add(empleado)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Empleado guardado exitosamente", Toast.LENGTH_SHORT).show()
                                        limpiarCampos()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Ya existe un empleado con ese correo", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al verificar duplicado: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } //else-insertar
            } //when
        } //btnAgregar

        btnSalir.setOnClickListener {
            val intent = Intent(this@AgregarUsuarioActivity, ListadoUsuariosActivity::class.java)
            startActivity(intent)
        }

    }//onCreate

    //Evento limpiarcampos()
    private fun limpiarCampos(){
        nombre.text.clear()
        apellido.text.clear()
        contrasena.text.clear()
        correo.text.clear()
        nombre.requestFocus() //enfoque al primer campo
    }
}