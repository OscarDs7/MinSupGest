package com.example.minsupgest

data class Empleado(
    val id: String = "", // importante: almacenar el ID del documento de Firestore
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val telefono: String = ""
)
