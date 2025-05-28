package com.example.minsupgest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PerfilEmpleadoActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var idEmpleado: String

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnSalir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_empleado)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCorreo = findViewById(R.id.etCorreo)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnSalir = findViewById(R.id.btnLeave)

        db = FirebaseFirestore.getInstance()

        idEmpleado = intent.getStringExtra("ID_EMPLEADO") ?: return

        cargarDatos() //cargamos los datos del empleado

        btnEditar.setOnClickListener {
            val actualizacion = mapOf(
                "nombre" to etNombre.text.toString(),
                "apellido" to etApellido.text.toString(),
                "correo" to etCorreo.text.toString(),
            )
            db.collection("empleados").document(idEmpleado).update(actualizacion)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }//btnEditar

        btnEliminar.setOnClickListener {
            db.collection("empleados").document(idEmpleado).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PerfilEmpleadoActivity, MenuAdminActivity::class.java)
                    startActivity(intent)
                    cargarDatos() //hacemos la consulta de los registros
                    //finish() // Cerrar actividad
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
        }//btnEliminar

        btnSalir.setOnClickListener {
            val intent = Intent(this@PerfilEmpleadoActivity, MenuAdminActivity::class.java)
            startActivity(intent)
        }//btnSalir
    }

    private fun cargarDatos() {
        db.collection("empleados").document(idEmpleado).get()
            .addOnSuccessListener { documento ->
                if (documento.exists()) {
                    etNombre.setText(documento.getString("nombre"))
                    etApellido.setText(documento.getString("apellido"))
                    etCorreo.setText(documento.getString("correo"))
                }
            }
    }//cargarDatos
}
