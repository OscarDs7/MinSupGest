package com.example.minsupgest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuEmpleadoActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var inventario: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_empleado)

        //Vinculación a los componentes
        inventario = findViewById(R.id.imgbInventary2)

        //Eventos de redireccionamiento
        inventario.setOnClickListener {
            val intent = Intent(this@MenuEmpleadoActivity, InventarioActivity::class.java)
            startActivity(intent)
        }

    }//onCreate

}