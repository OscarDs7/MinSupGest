package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ventasActiviti : AppCompatActivity() {
    private lateinit var idproducto: EditText
    private lateinit var cantidad: EditText
    private lateinit var vender: Button
    private lateinit var regresar: Button
    private lateinit var qrscan: Button
    private var qrContent: String = ""

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
        regresar = findViewById(R.id.btnRegresar3)
        qrscan = findViewById(R.id.btnqr)

        val db = FirebaseFirestore.getInstance()
        //val productosVRef = db.collection("ventas")
        //val productosRef = db.collection("productos")
         qrscan.setOnClickListener {
                 val options = ScanOptions()
                 options.setPrompt("Escanea un código QR")
                 options.setBeepEnabled(true)
                 options.setOrientationLocked(true)
                 options.setBarcodeImageEnabled(true)
                 barcodeLauncher.launch(options)
         }//escaneo qr
        vender.setOnClickListener {
            val idBuscada = idproducto.text.toString().trim()
            val cantidadVendida = cantidad.text.toString().toIntOrNull()

            when {
                idBuscada.isEmpty() || cantidadVendida == null -> {
                    Toast.makeText(this, "Llena todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }

                cantidadVendida <= 0 -> {
                    Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    db.collection("productos").document(idBuscada)
                        .get()
                        .addOnSuccessListener { documento ->
                            if (documento.exists()) {
                                val stocks = documento.getLong("stock")?.toInt() ?: 0
                                val nombre = documento.getString("nombre_prod") ?: "Desconocido"
                                val precioVenta = documento.getDouble("precio_emp") ?: 0.0
                                val precioProveedor = documento.getDouble("precio_prov") ?: 0.0
                                val gananciaUnidad = precioVenta - precioProveedor

                                if (stocks <= 0) {
                                    Toast.makeText(this, "Producto sin cantidad disponible", Toast.LENGTH_SHORT).show()
                                    return@addOnSuccessListener
                                }

                                if (cantidadVendida > stocks) {
                                    Toast.makeText(this, "No hay suficiente cantidad para esa venta", Toast.LENGTH_SHORT).show()
                                    return@addOnSuccessListener
                                }

                                val nuevoStock = stocks - cantidadVendida
                                val totalGanancia = gananciaUnidad * cantidadVendida

                                val venta = hashMapOf(
                                    "cantidad_ventas" to cantidadVendida,
                                    "idproducto" to idBuscada,
                                    "nombre_prod" to nombre,
                                    "precio_emp" to precioVenta,
                                    "precio_prov" to precioProveedor,
                                    "total_ganancia" to totalGanancia
                                )

                                // Actualizar stock
                                db.collection("productos").document(idBuscada)
                                    .update("stock", nuevoStock)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Stock actualizado", Toast.LENGTH_SHORT).show()
                                    }

                                // Verificar si ya hay venta registrada
                                db.collection("ventas").whereEqualTo("idproducto", idBuscada)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        if (!documents.isEmpty) {
                                            val docRef = documents.first().reference
                                            val ventasAnteriores = documents.first().getDouble("cantidad_ventas") ?: 0.0
                                            val totalActualizado = ventasAnteriores + cantidadVendida
                                            val gananciaTotal = gananciaUnidad * totalActualizado

                                            docRef.update(
                                                mapOf(
                                                    "cantidad_ventas" to totalActualizado,
                                                    "total_ganancia" to gananciaTotal
                                                )
                                            ).addOnSuccessListener {
                                                Toast.makeText(this, "Venta actualizada correctamente", Toast.LENGTH_SHORT).show()
                                                limpiarCampos()
                                            }
                                        } else {
                                            db.collection("ventas").add(venta)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this, "Venta registrada correctamente", Toast.LENGTH_SHORT).show()
                                                    limpiarCampos()
                                                }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Error al consultar ventas", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error de conexión con Firestore", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }


        regresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //Regreso a la ventana anterior
        }
    }//onCreate

    fun limpiarCampos(){
        idproducto.text.clear()
        cantidad.text.clear()
        idproducto.requestFocus()
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            qrContent = result.contents
            idproducto.setText(qrContent)
            Toast.makeText(this, "Contenido QR: $qrContent", Toast.LENGTH_LONG).show()
            // Aquí puedes usar qrContent como necesites
        } else {
            Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
        }
    }
}
