package Adm_proyectos.approyecto.MODELO

open class Usuario (nombre:String = "", apellidos:String = "") {

    var nombre: String
    var apellidos: String

    init {
        this.nombre = nombre
        this.apellidos = apellidos
    }


}