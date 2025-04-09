package com.example.minsupgest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class ventasActiviti : AppCompatActivity() {
    private lateinit var idproducto:EditText
    private lateinit var cantidad:EditText
    private lateinit var vender:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ventas_activiti)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = FirebaseFirestore.getInstance()
        val productosVRef = db.collection("ventas")
        val productosRef = db.collection("productos")
        val idBuscada = idproducto.text.toString()
        db.collection("usuarios").document(idBuscada)
            .get()
            .addOnSuccessListener { documento ->
                if (documento.exists()) {
                    val nombre = documento.getString("idproducto")
                    Toast.makeText(this, "Dato encontrado: "+nombre, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Dato no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Fallo en la conexion ", Toast.LENGTH_SHORT).show()
            }
    }

}