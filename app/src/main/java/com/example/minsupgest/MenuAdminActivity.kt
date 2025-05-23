package com.example.minsupgest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Paint
import android.widget.TextView
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class MenuAdminActivity : AppCompatActivity() {
   // val statistics = findViewById<ImageButton>(R.id.imgbStatistics)
    private lateinit var inventary: ImageButton
    private lateinit var btnAgregarProducto: Button
    private lateinit var btnvender:Button
    private lateinit var ibtnEmpleados: ImageButton
    private lateinit var txtRegreso: TextView
    private lateinit var ibtnEstadisticas: ImageButton

    //val recommendations = findViewById<ImageButton>(R.id.imgbComments)
    //val employees = findViewById<ImageButton>(R.id.imgbEmployes)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)
        inventary = findViewById(R.id.imgbInventary)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
        btnvender = findViewById(R.id.btnVenta)
        ibtnEmpleados = findViewById(R.id.imgbEmployes)
        ibtnEstadisticas = findViewById(R.id.imgbStatistics)
        txtRegreso = findViewById(R.id.txtRegreso)
        verificarStockCritico(this)
        //propiedad de subrayado
        txtRegreso.paintFlags = txtRegreso.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Eventos listener
        inventary.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, InventarioActivity::class.java)
            startActivity(intent)
        }
        btnAgregarProducto.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, AgregarProductoActivity::class.java)
            startActivity(intent)
        }
        btnvender.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity,ventasActiviti::class.java)
            startActivity(intent)
        }
        ibtnEmpleados.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, ListadoUsuariosActivity::class.java)
            startActivity(intent)
        }
        txtRegreso.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, Iniciogerente::class.java)
            startActivity(intent)
            Toast.makeText(this@MenuAdminActivity, "Saliendo de rol Gerente...", Toast.LENGTH_SHORT).show()
        }
        ibtnEstadisticas.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, GraficosActivity::class.java)
            startActivity(intent)
        }
    }

    fun verificarStockCritico(context: Context) {
        val db = FirebaseFirestore.getInstance()
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("nombre_prod") ?: continue
                    val stock = document.getLong("stock") ?: continue

                    if (stock <= 5) {
                        val mensaje = "El producto \"$nombre\" tiene solo $stock unidades disponibles."
                        NotificationUtils.mostrarNotificacion(context, "Stock Crítico", mensaje)
                    }
                }
            }
    }
}
