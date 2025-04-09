package com.example.minsupgest

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class InventarioActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        tableLayout = findViewById(R.id.tblDatos)
        // Inicialización Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // Llamada a la función
        mostrarDatos()
    }//onCreate

    private fun mostrarDatos() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
               // agregarEncabezado()

                for (document in result) {
                    val nombre = document.getString("nombre_prod") ?: ""
                    val precio_empresa  = document.getDouble("precio_emp")?.toString() ?: "0.0"
                    val precio_proveedor = document.getDouble("precio_prov")?.toString() ?: "0.0"
                    val cantidad = document.getLong("stock")?.toString() ?: "0"

                    agregarFila(nombre, precio_empresa, precio_proveedor, cantidad)
                }
            }
            .addOnFailureListener { exception ->
                // Manejo de errores
                agregarFila("Error", exception.message ?: "Error desconocido", "", "")
            }
    }//mostrarDatos

    /*
    private fun agregarEncabezado() {
        val fila = TableRow(this)
        fila.setBackgroundColor(0xFFE0E0E0.toInt())
        fila.gravity = Gravity.CENTER

        val columnas = listOf("Nombre", "Precio", "Proveedor", "Stock")
        columnas.forEach {
            val tv = crearTextView(it, true)
            fila.addView(tv)
        }
        tableLayout.addView(fila)
    }//agregarEncabezado*/

    private fun agregarFila(nombre: String, precio_emp: String, precio_prov: String, cantidad: String) {
        val fila = TableRow(this)

        val valores = listOf(nombre, precio_emp, precio_prov, cantidad)
        valores.forEach {
            val tv = crearTextView(it)
            fila.addView(tv)
        }
        tableLayout.addView(fila)
    }//agregarFila

    private fun crearTextView(texto: String, isHeader: Boolean = false): TextView {
        return TextView(this).apply {
            text = texto
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
            }
        }
    }//crearTextView


}