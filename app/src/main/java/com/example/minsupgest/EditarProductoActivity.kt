package com.example.minsupgest

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var idDocumento: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        db = FirebaseFirestore.getInstance()

        val nombre = intent.getStringExtra("nombre") ?: ""
        val precioEmp = intent.getDoubleExtra("precio_emp", 0.0)
        val precioProv = intent.getDoubleExtra("precio_prov", 0.0)
        val stock = intent.getLongExtra("stock", 0)

        findViewById<EditText>(R.id.edNombre).setText(nombre)
        findViewById<EditText>(R.id.edPrecioEmp).setText(precioEmp.toString())
        findViewById<EditText>(R.id.edPrecioProv).setText(precioProv.toString())
        findViewById<EditText>(R.id.edStock).setText(stock.toString())

        db.collection("productos").whereEqualTo("nombre_prod", nombre)
            .get()
            .addOnSuccessListener { docs ->
                if (!docs.isEmpty) {
                    idDocumento = docs.first().id
                }
            }

        findViewById<Button>(R.id.btnGuardarCambios).setOnClickListener {
            val nuevoNombre = findViewById<EditText>(R.id.edNombre).text.toString()
            val nuevoPrecioEmp = findViewById<EditText>(R.id.edPrecioEmp).text.toString().toDoubleOrNull()
            val nuevoPrecioProv = findViewById<EditText>(R.id.edPrecioProv).text.toString().toDoubleOrNull()
            val nuevoStock = findViewById<EditText>(R.id.edStock).text.toString().toLongOrNull()

            if (nuevoPrecioEmp != null && nuevoPrecioProv != null && nuevoStock != null) {
                db.collection("productos").document(idDocumento).update(
                    mapOf(
                        "nombre_prod" to nuevoNombre,
                        "precio_emp" to nuevoPrecioEmp,
                        "precio_prov" to nuevoPrecioProv,
                        "stock" to nuevoStock
                    )
                ).addOnSuccessListener {
                    finish()
                }
            }
        }

        findViewById<Button>(R.id.btnEliminarProducto).setOnClickListener {
            db.collection("productos").document(idDocumento).delete().addOnSuccessListener {
                finish()
            }
        }
    }
}
