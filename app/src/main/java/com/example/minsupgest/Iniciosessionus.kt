package com.example.minsupgest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Iniciosessionus : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var employee: EditText
    private lateinit var password: EditText
    private lateinit var enter: Button
    private lateinit var exit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciosessionus)

        //Vinculación a los componentes
        employee = findViewById(R.id.etxtusuario)
        password = findViewById(R.id.etxtcontra)
        enter = findViewById(R.id.btnenter)
        exit = findViewById(R.id.btnsalir)

        //Eventos en los botones
        enter.setOnClickListener {
            val user = employee.text.toString().trim()
            val passw = password.text.toString().trim()
            if (user.isEmpty() || passw.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }else if (validarCredenciales(user, passw)) {
                Toast.makeText(this, "Has ingresado como empleado!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Iniciosessionus, MenuEmpleadoActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
        exit.setOnClickListener {
            val intent = Intent(this@Iniciosessionus, Selecionusuario::class.java)
            startActivity(intent)
        }

    }

    // Méthod para la validación de las credenciales
    private fun validarCredenciales(user: String, passw: String): Boolean {
        return user == "user" && passw == "1234" // Aquí puedes implementar validación con una base de datos
    }
}