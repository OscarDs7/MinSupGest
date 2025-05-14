package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
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
    private lateinit var btnRegresar: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etPrecioProveedor = findViewById(R.id.etPrecioProveedor)
        etStock = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnRegresar = findViewById(R.id.btnRegresar1)

        val db = FirebaseFirestore.getInstance() //instancia a la BD
        val productosRef = db.collection("productos") //Acceso a la colección de los productos

        //Evento de guardar el producto
        btnGuardar.setOnClickListener {
            // Extraemos y convertimos los valores de los campos
            val nombre = etNombre.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val precioProveedor = etPrecioProveedor.text.toString().toDoubleOrNull()
            val stock = etStock.text.toString().toIntOrNull()

            when {
                // Validaciones
                nombre.isEmpty() || precio == null || precioProveedor == null || stock == null -> {
                    Toast.makeText(this, "Por favor llena todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }

                precio <= 0 || precioProveedor <= 0 -> {
                    Toast.makeText(this, "No se permite un precio de $0 o menor", Toast.LENGTH_SHORT).show()
                }

                stock < 0 -> {
                    Toast.makeText(this, "El stock no puede ser negativo", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // Verificamos si el producto ya existe
                    productosRef.whereEqualTo("nombre_prod", nombre).get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // Crear el mapa del producto
                                val producto = hashMapOf(
                                    "nombre_prod" to nombre,
                                    "precio_emp" to precio,
                                    "precio_prov" to precioProveedor,
                                    "stock" to stock
                                )

                                // Guardar en Firestore
                                productosRef.add(producto)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show()
                                        limpiarCampos()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Ya existe un producto con ese nombre", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al verificar duplicado: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } //else-insertar datos
            } //when
        } //btnGuardar


        btnRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //Regreso a la ventana anterior
        }

    }//onCreate

    private fun limpiarCampos(){
        etNombre.text.clear()
        etPrecio.text.clear()
        etPrecioProveedor.text.clear()
        etStock.text.clear()
        etNombre.requestFocus() //enfoque al primer campo
    }

}//class

