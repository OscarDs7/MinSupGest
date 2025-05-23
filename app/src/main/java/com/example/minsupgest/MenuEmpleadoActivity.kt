package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore


class MenuEmpleadoActivity : AppCompatActivity() {
    //Instancias
    private lateinit var inventario: ImageButton
    private lateinit var agregar_prod: Button
    private lateinit var cerrar_sesion: TextView
    private lateinit var estadisticas: ImageButton
    private lateinit var hacer_venta: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_empleado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Vinculación a los componentes mediante su id
        inventario = findViewById(R.id.imgbInventary2)
        estadisticas = findViewById(R.id.imgbStatistics2)
        agregar_prod = findViewById(R.id.btnAgregar)
        hacer_venta = findViewById(R.id.btnSale)
        cerrar_sesion = findViewById(R.id.txtRegreso1)

        /* Permiso para el envío de notificaciones a dispositivos android >= 13 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        verificarStockCritico(this) //llamada a la función
        //propiedad de subrayado
        cerrar_sesion.paintFlags = cerrar_sesion.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Eventos de navegación
        estadisticas.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, GraficosActivity::class.java)
            startActivity(intent)
        }
        inventario.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, InventarioActivity::class.java)
            startActivity(intent)
        }
        agregar_prod.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, AgregarProductoActivity::class.java)
            startActivity(intent)
        }
        hacer_venta.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, ventasActiviti::class.java)
            startActivity(intent)
        }
        cerrar_sesion.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, Iniciosessionus::class.java)
            startActivity(intent)
            Toast.makeText(this@MenuEmpleadoActivity,"Saliste de rol empleado...", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show() // Diagnóstico
                        NotificationUtils.mostrarNotificacion(context, "Stock Crítico", mensaje)
                    }
                }
            }
    }//verificarStockCritico
}