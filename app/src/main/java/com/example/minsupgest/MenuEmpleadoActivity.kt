package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.firestore.FirebaseFirestore


class MenuEmpleadoActivity : AppCompatActivity() {
    //Instancias
    private lateinit var inventario: ImageButton
    private lateinit var agregar_prod: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_empleado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Vinculación
        inventario = findViewById(R.id.imgbInventary2)
        agregar_prod = findViewById(R.id.btnAgregar)

        //Eventos de navegación
        inventario.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, InventarioActivity::class.java)
            startActivity(intent)
        }
        agregar_prod.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, AgregarProductoActivity::class.java)
            startActivity(intent)
        }
    }
}