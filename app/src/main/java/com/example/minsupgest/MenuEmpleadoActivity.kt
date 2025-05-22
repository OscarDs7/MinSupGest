package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.firebase.firestore.FirebaseFirestore


class MenuEmpleadoActivity : AppCompatActivity() {
    //Instancias
    private lateinit var inventario: ImageButton
    private lateinit var agregar_prod: Button
    private lateinit var cerrar_sesion: TextView
    private lateinit var estadisticas: ImageButton

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
        cerrar_sesion = findViewById(R.id.txtRegreso1)
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
        cerrar_sesion.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, Iniciosessionus::class.java)
            startActivity(intent)
            Toast.makeText(this@MenuEmpleadoActivity,"Saliste de rol empleado...", Toast.LENGTH_SHORT).show()
        }
    }
}