package com.example.minsupgest

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
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

    // Inserción de toda una fila completa de información
    private fun agregarFila(nombre: String, precio_emp: String, precio_prov: String, cantidad: String) {
        val fila = TableRow(this)

        // Convertimos cantidad a Int (manejo de errores incluido)
        val cantidadInt = cantidad.toIntOrNull() ?: 0

        // Si el stock es crítico, cambia color de fondo
        if (cantidadInt <= 5) {
            fila.setBackgroundColor(Color.parseColor("#f29494")) // rojo claro
        }

        // Añadimos los campos como celdas
        val valores = listOf(nombre, precio_emp, precio_prov, cantidad)
        valores.forEach {
            val tv = crearTextView(it)
            fila.addView(tv)
        }

        // Añadir fila a la tabla
        tableLayout.addView(fila)

        // Evento clic en fila
        fila.setOnClickListener {
            mostrarDetalle(nombre, precio_emp, cantidad, "")
        }
    }//agregarFila

    //Creación de las celdas o campos de la tabla
    private fun crearTextView(texto: String,  isHeader: Boolean = false): TextView {
        val textView = TextView(this)
        textView.apply {
            text = texto
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            setPadding(16, 8, 16, 8)
            gravity = Gravity.CENTER
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f) // Responsive de la celda
            if (isHeader) {
                setTypeface(null, Typeface.BOLD)
            }
        }
        return textView
    }//crearTextView



    @SuppressLint("SetTextI18n")

    /*Función para mostrar los detalles de un producto dentro del cardview*/
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