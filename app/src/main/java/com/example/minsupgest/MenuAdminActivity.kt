package com.example.minsupgest

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent

class MenuAdminActivity : AppCompatActivity() {
    private lateinit var inventario: ImageButton
    private lateinit var btnAgregarProducto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)
        inventario = findViewById(R.id.imgbInventary)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)

        //Eventos listener
        inventario.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, InventarioActivity::class.java)
            startActivity(intent)
        }
        btnAgregarProducto.setOnClickListener {
            val intent = Intent(this@MenuAdminActivity, AgregarProductoActivity::class.java)
            startActivity(intent)
        }
    }
}
