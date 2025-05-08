package com.example.minsupgest

import android.annotation.SuppressLint
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
    private lateinit var idproducto: EditText
    private lateinit var cantidad: EditText
    private lateinit var vender: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ventas_activiti)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idproducto = findViewById(R.id.edtidproducto)
        cantidad = findViewById(R.id.edtCantidad)
        vender = findViewById(R.id.btnventas)
        val db = FirebaseFirestore.getInstance()
        //val productosVRef = db.collection("ventas")
        //val productosRef = db.collection("productos")

        vender.setOnClickListener {
            val idBuscada = idproducto.text.toString()

            // Si el producto no está registrado
            db.collection("productos").document(idBuscada)
                .get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        val Idpro = documento.id
                        val stocks = documento.getLong("stock") ?: 0
                        val Nombre = documento.getString("nombre_prod")
                        val precioventa = documento.getDouble("precio_emp") ?: 0.0
                        val precioprovedor = documento.getDouble("precio_prov") ?: 0.0
                        val vendido = cantidad.text.toString().toInt()
                        val ganancia = precioventa - precioprovedor
                        val total = ganancia * vendido
                        var totalpro = stocks - vendido
                        if (totalpro < 0) {
                            totalpro = 0
                            Toast.makeText(this, "Se vendió exceso de producto", Toast.LENGTH_SHORT).show()
                        }

                        val nuevoDoc = hashMapOf(
                            "cantidad_ventas" to vendido,
                            "idproducto" to Idpro,
                            "nombre_prod" to Nombre,
                            "precio_emp" to precioventa,
                            "precio_prov" to precioprovedor,
                            "total_ganancia" to total
                        )

                        // Actualizar el stock en productos
                        db.collection("productos")
                            .document(Idpro)
                            .update("stock", totalpro)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Producto vendido", Toast.LENGTH_SHORT).show()
                                limpiarCampos() //llamada a la función de limpiar campos para evitar duplicados de ventas
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al vender", Toast.LENGTH_SHORT).show()
                                limpiarCampos() //llamada a la función de limpiar campos para evitar duplicados de ventas
                            }

                        // Consultar si ya existe el producto en la colección "ventas"
                        db.collection("ventas").whereEqualTo("idproducto", idBuscada)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    for (document in documents) {
                                        db.collection("ventas").document(document.id).get()
                                            .addOnSuccessListener { documentos ->
                                                // Obtener el valor de "cantidad_ventas" y sumarlo
                                                val ventasguardadas = documentos.getDouble("cantidad_ventas") ?: 0.0
                                                val ventatotals = ventasguardadas + vendido
                                                val ventastot = ganancia * ventatotals

                                                // Actualizar el documento con la nueva cantidad de ventas
                                                db.collection("ventas")
                                                    .document(document.id)
                                                    .update(
                                                        mapOf(
                                                            "cantidad_ventas" to ventatotals, // Usar ventatotals aquí
                                                            "total_ganancia" to ventastot
                                                        )
                                                    )
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this, "Documento actualizado exitosamente", Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Toast.makeText(this, "Error al actualizar documento", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                            .addOnFailureListener { exception ->
                                                Toast.makeText(this, "Error al obtener documento", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    Toast.makeText(this, "Datos encontrados correctamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Si no existe, agregar un nuevo documento
                                    db.collection("ventas")
                                        .add(nuevoDoc)
                                        .addOnSuccessListener { docRef ->
                                            Toast.makeText(this, "Dato registrado", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Dato no registrado", Toast.LENGTH_SHORT).show()
                                        }
                                    Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error en la conexión", Toast.LENGTH_SHORT).show()
                }
        } //evento-vender
    }//onCreate

    fun limpiarCampos(){
        idproducto.text.clear()
        cantidad.text.clear()
        idproducto.requestFocus()
    }
}
