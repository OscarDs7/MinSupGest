package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class ListadoUsuariosActivity : AppCompatActivity() {
    //Instancias
    private lateinit var empleados: RecyclerView
    private lateinit var agregar: Button
    private lateinit var db: FirebaseFirestore //instancia a la clase Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_usuarios)

        //Vinculación a componentes
        empleados = findViewById(R.id.rvEmpleados)
        agregar = findViewById(R.id.btnAddUser)

        // Inicialización Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // Llamada a la función


        empleados.layoutManager = LinearLayoutManager(this)

        //Evento Agregar Usuario Nuevo
        agregar.setOnClickListener {
            val intent = Intent(this@ListadoUsuariosActivity, AgregarUsuarioActivity::class.java)
            startActivity(intent)
        }
        mostrarEmpleados()
    }

    private fun mostrarEmpleados() {
        db.collection("empleados")
            .get()
            .addOnSuccessListener { result ->
                val listaEmpleados = mutableListOf<Empleado>()
                for (document in result) {
                    val id = document.id
                    val nombre = document.getString("nombre") ?: ""
                    val apellido = document.getString("apellido") ?: ""
                    val correo = document.getString("correo") ?: ""

                    listaEmpleados.add(Empleado(id, nombre, apellido, correo))
                }

                empleados.adapter = EmpleadoAdapter(listaEmpleados) { empleado ->
                    val intent = Intent(this, PerfilEmpleadoActivity::class.java)
                    intent.putExtra("ID_EMPLEADO", empleado.id)
                    startActivity(intent)
                }
            }
    }


}