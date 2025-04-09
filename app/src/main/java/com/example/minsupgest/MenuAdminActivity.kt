package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuAdminActivity : AppCompatActivity() {
private  lateinit var  inventary: ImageButton
private lateinit var  btnAgregarProducto:Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    inventary = findViewById(R.id.imgbInventary)
    btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
    inventary.setOnClickListener{
        val  intent = Intent(this@MenuAdminActivity,InventarioActivity::class.java)
        startActivity(intent)
    }
   btnAgregarProducto.setOnClickListener {
       val  intent = Intent(this@MenuAdminActivity,AgregarProductoActivity::class.java)
       startActivity(intent)
   }

    }

}