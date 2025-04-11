package com.example.minsupgest

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MenuAdminActivity : AppCompatActivity() {
   // val statistics = findViewById<ImageButton>(R.id.imgbStatistics)
    private lateinit var inventary: ImageButton
    private lateinit var btnAgregarProducto: Button
    private lateinit var btnvender:Button
    //val recommendations = findViewById<ImageButton>(R.id.imgbComments)
    //val employees = findViewById<ImageButton>(R.id.imgbEmployes)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_admin)
        inventary = findViewById(R.id.imgbInventary)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
        btnvender = findViewById(R.id.btnVenta)

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
    }
}
