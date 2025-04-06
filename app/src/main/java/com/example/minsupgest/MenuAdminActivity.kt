package com.example.minsupgest

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class MenuAdminActivity : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance()
    val ref = db.getReference("productos")
    private lateinit var guardar: ImageButton
    val nuevoProducto = Producto(
        nombre_producto = "peras",
        precio = 200.00,
        stock = 10
    )
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        guardar = findViewById(R.id.imgbStatistics)
        guardar.setOnClickListener{
            ref.push().setValue(nuevoProducto)
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            Toast.makeText(this, "funciono", Toast.LENGTH_SHORT).show()
        }

    }
    data class Producto(
        val nombre_producto: String = "",
        val precio: Double = 0.0,
        val stock: Int = 0
    )
}