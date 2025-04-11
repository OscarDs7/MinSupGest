package com.example.minsupgest

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AgregarProductoActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etPrecioProveedor: EditText
    private lateinit var etStock: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etPrecioProveedor = findViewById(R.id.etPrecioProveedor)
        etStock = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val precioProveedor = etPrecioProveedor.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            if (nombre.isEmpty() || precio == null || precioProveedor == null || stock == null) {
                Toast.makeText(this, "Por favor llena todos los campos correctamente", Toast.LENGTH_SHORT).show()
            } else {
                val producto = hashMapOf(
                    "nombre_prod" to nombre,
                    "precio_emp" to precio,
                    "precio_prov" to precioProveedor,
                    "stock" to stock
                )
                // Se agrega el producto
                productosRef.add(producto)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }
                    //Marca un error en caso de que no se guarde
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }//else

        }//btnGuardar

    }//onCreate

    private fun limpiarCampos(){
        etNombre.text.clear()
        etPrecio.text.clear()
        etPrecioProveedor.text.clear()
        etStock.text.clear()
        etNombre.requestFocus() //enfoque al primer campo
    }

}//class

