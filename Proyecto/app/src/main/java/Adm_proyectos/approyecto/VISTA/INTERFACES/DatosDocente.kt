package Adm_proyectos.approyecto.VISTA.INTERFACES

import Adm_proyectos.approyecto.MODELO.Noticia
import androidx.fragment.app.Fragment

interface DatosDocente{
    fun enviarDatosEstudiante(ced: String, nombre: String, grado: String, curso:String, correoProfe:String, correo:String, nombreP: String, apellido: String)
    fun enviarDatosCurso (id: String, grado:String, correo: String, fragment: Fragment)
    fun enviarDatosCurso (id: String, grado:String, correo: String, fragment: Fragment, nombre: String, apellido: String)
    fun enviarDatosCurso (id: String, nombre: String, grado:String, horario:String, fragment: Fragment, correo: String, nombreP: String, apellido:String)
    fun enviarDatosCurso (id: String, nombre: String, grado:String, horario:String, fragment: Fragment, cedula: String, nombreP: String, apellido:String, correo: String)
    fun enviarDatosCurso (id: String, grado:String,  fragment: Fragment, correo: String, nombreP: String, apellido:String)
    fun enviarDatosCurso (id: String, nombre: String, grado:String, horario:String, fragment: Fragment, correo: String)
    fun enviarCorreo(correo:String, fragment: Fragment)
    fun cursosPopUp(cursos:ArrayList<String>)
    fun cosas(string: String)
}