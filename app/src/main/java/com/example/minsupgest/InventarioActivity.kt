package com.example.minsupgest

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class InventarioActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var tableLayout: TableLayout
    private lateinit var btnRegresar: Button
    private lateinit var db: FirebaseFirestore //instancia a la clase Firebase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        tableLayout = findViewById(R.id.tblDatos)
        btnRegresar = findViewById(R.id.btnReturned)
        // Inicialización Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        // Llamada a la función
        mostrarDatos()

        // Evento de regreso
        btnRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //Regreso a la ventana anterior
        }

    }//onCreate

    // Función para mostrar los datos dentro de la tabla
    private fun mostrarDatos() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
               // agregarEncabezado()
                //Recorremos todos los documentos y extraemos sus campos
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

        fila.setOnClickListener {
            mostrarDetalle(nombre, precio_emp, cantidad, "") // puedes pasar una URL si tuvieras una imagen de Firebase, por ahora usa la imagen local
        }
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


    private fun mostrarDetalle(nombre: String, precio: String, stock: String, imagenUrl: String) {
        val contenedor = findViewById<LinearLayout>(R.id.rvProducto)
        contenedor.removeAllViews()

        val cardView = layoutInflater.inflate(R.layout.card_view_prod, null) as CardView

        val imgDetalle = cardView.findViewById<ImageView>(R.id.imgProducto)
        val Nombre = cardView.findViewById<TextView>(R.id.txtProd)
        val Precio = cardView.findViewById<TextView>(R.id.txtPrize)
        val Stock = cardView.findViewById<TextView>(R.id.txtStock)

        Nombre.text = "$nombre"
        Precio.text = "Precio Unitario:\n$$precio"
        Stock.text = "Cantidad:\n$stock pzas"

        // Imagen local por ahora
        imgDetalle.setImageResource(R.drawable.product)

        contenedor.addView(cardView)
    }




}