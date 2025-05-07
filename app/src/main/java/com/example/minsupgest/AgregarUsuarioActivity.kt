package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class AgregarUsuarioActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var contrasena: EditText
    private lateinit var telefono: EditText
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
        telefono = findViewById(R.id.edtTelefono)
        btnAgregar = findViewById(R.id.btnAgregarUser)
        btnSalir = findViewById(R.id.btnSalir)

        val db = FirebaseFirestore.getInstance() //instancia a la BD
        val empleadosRef = db.collection("empleados") //Acceso a la colección de los productos

        btnAgregar.setOnClickListener {
            //Extraemos los datos de las cajas de texto
            val nombre = nombre.text.toString().trim()
            val apellido = apellido.text.toString().trim()
            val contrasena = contrasena.text.toString().trim()
            val telefono = telefono.text.toString().toIntOrNull()

            //Validamos que no se encuentre un campo vacío
            if(nombre.isEmpty() || apellido.isEmpty() || contrasena.isEmpty() || telefono == null){
                Toast.makeText(this, "Favor de llenar los campos", Toast.LENGTH_SHORT).show()
            }
            else{
                // Enviamos cada dato en el campo correspondiente dentro de la tabla
                val empleado = hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "contrasena" to contrasena,
                    "telefono" to telefono
                )
                // Se agrega el producto
                empleadosRef.add(empleado)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Empleado guardado exitosamente", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }
                    //Marca un error en caso de que no se guarde
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }//else
        }//btn-agregar

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
        telefono.text.clear()
        nombre.requestFocus() //enfoque al primer campo
    }
}