package Adm_proyectos.approyecto.VISTA.INTERFACES

import Adm_proyectos.approyecto.MODELO.Noticia
import androidx.fragment.app.Fragment

interface DatosEstudiante {
    fun enviarDatosNoticias(noticia:Noticia)
    fun enviarCedula(cedula: String, fragment: Fragment)
    fun tareaNoticia(esTarea: Boolean)
    fun enviarDatosDocente(ced: String, nombre:String, correo: String, calificacion: String, contra:String, fragment: Fragment, nombreE: String, apellido:String, cedula: String, id:String, grado:String)
    fun enviarCal(cedula: String)
}