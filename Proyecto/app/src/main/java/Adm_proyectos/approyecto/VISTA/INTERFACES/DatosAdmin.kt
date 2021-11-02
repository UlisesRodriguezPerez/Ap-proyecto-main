package Adm_proyectos.approyecto.VISTA.INTERFACES

import androidx.fragment.app.Fragment

interface DatosAdmin {
    fun enviarDatosCurso (id: String, nombre: String, grado:String, horario:String, fragment: Fragment)
    fun enviarDatosCurso (id: String, nombre: String, fragment: Fragment)
    fun enviarDatosDocente(ced: String, nombre:String, correo: String, calificacion: String, contra:String, fragment: Fragment)
    fun enviarDatosEstudiante(ced: String, nombre: String, grado:String, correo: String, contra: String, fragment: Fragment)
    fun cursosPopUp(cursos:ArrayList<String>)
}