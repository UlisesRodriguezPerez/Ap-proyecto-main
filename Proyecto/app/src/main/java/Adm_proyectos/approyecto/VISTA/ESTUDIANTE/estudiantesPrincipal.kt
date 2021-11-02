package Adm_proyectos.approyecto.VISTA.ESTUDIANTE

import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.MODELO.Noticia
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ADMIN.popUpCursos
import Adm_proyectos.approyecto.VISTA.DOCENTE.DocenteDetallesCurso
import Adm_proyectos.approyecto.VISTA.DOCENTE.DocenteListaCursos
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosEstudiante
import Adm_proyectos.approyecto.VISTA.log_in
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main._admin_pricipal.*
import kotlinx.android.synthetic.main._admin_pricipal.nombreUsuario
import kotlinx.android.synthetic.main._docente_principal.*
import kotlinx.android.synthetic.main._estudiantes_principal.*
import kotlinx.android.synthetic.main.estudiante_noticias.*


class estudiantesPrincipal : AppCompatActivity(), DatosDocente, DatosEstudiante {

    private val controller = ControladorComponentesVista()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._estudiantes_principal)

        val nombreUsu = intent.getStringExtra("nombre")
        nombreUsuario.text = "$nombreUsu"
        val correo = intent.getStringExtra("correo")
        val cedula = intent.getStringExtra("cedula").toString()
        val apellido = intent.getStringExtra("apellido").toString().replace("\"", "")

        irPrimeraPantalla(correo.toString(), nombreUsu.toString(), apellido, cedula)

        val nombre = intent.getStringExtra("nombre")
        nombreUsuario.text = "$nombre"

        contenedorEstudiante.setOnClickListener{view ->
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        salirSesionEst.setOnClickListener {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show()
            Intent(this, log_in::class.java).also{
                startActivity(it)
            }
        }
    }

    private fun irPrimeraPantalla(correo: String, nombre: String, apellido: String, cedula:String){
        val Lista = DocenteListaCursos()
        val bundle = Bundle()
        val datos = arrayOf(correo, nombre, apellido, cedula)
        bundle.putStringArray("datosEst", datos)
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        Lista.arguments = bundle
        transaction.add(R.id.contenedorEstudiante, Lista, null)
        transaction.commit()
    }

    override fun enviarDatosDocente(ced: String, nombre: String, correo: String, calificacion: String, contra:String,
                                    fragment: Fragment, nombreP: String, apellido: String, cedula: String, id: String, grado: String) {
        val bundle = Bundle()
        val datos = arrayOf(ced, nombre, correo, calificacion, contra, nombreP, apellido, cedula, id, grado)
        bundle.putStringArray("datosDocente", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, fragment)
        transaccion.commit()
    }

    override fun enviarCal(cedula: String) {
        val bundle = Bundle()
        val cal = estudianteCalificarDocente()
        bundle.putString("ced", cedula)
        cal.arguments = bundle
        cal.show(this.supportFragmentManager, "cal")
    }

    override fun enviarDatosEstudiante(
        ced: String,
        nombre: String,
        grado: String,
        curso: String,
        correoProfe: String,
        correo: String,
        nombreP: String,
        apellido: String
    ) {
        TODO("Not yet implemented")
    }

    override fun enviarDatosCurso(id: String, grado: String, correo: String, fragment: Fragment) {
        val bundle = Bundle()
        val datos = arrayOf(id,grado,correo)
        bundle.putStringArray("datosCursoPequeno", datos)
        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(
        id: String,
        grado: String,
        correo: String,
        fragment: Fragment,
        nombre: String,
        apellido: String
    ) {
        val bundle = Bundle()
        val datos = arrayOf(id, grado, correo, nombre, apellido)
        bundle.putStringArray("datosCursoPequeno2", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(
        id: String,
        nombre: String,
        grado: String,
        horario: String,
        fragment: Fragment,
        correo: String,
        nombreP: String,
        apellido: String
    ) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre, grado, horario, correo, nombreP, apellido, "2")
        bundle.putStringArray("datosCursoGrande", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesCurso()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, detalles)
        transaccion.commit()
    }

    override fun enviarDatosCurso(
        id: String,
        nombre: String,
        grado: String,
        horario: String,
        fragment: Fragment,
        cedula: String,
        nombreP: String,
        apellido: String,
        correo: String
    ) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre, grado, horario, cedula, nombreP, apellido, correo, "2")
        bundle.putStringArray("datosCursoGrande", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesCurso()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, detalles)
        transaccion.commit()
    }

    override fun enviarDatosCurso(
        id: String,
        grado: String,
        fragment: Fragment,
        correo: String,
        nombreP: String,
        apellido: String
    ) {
        val bundle = Bundle()
        val datos = arrayOf(id, grado, correo, nombreP, apellido)
        bundle.putStringArray("datosCursoMed", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(
        id: String,
        nombre: String,
        grado: String,
        horario: String,
        fragment: Fragment,
        correo: String
    ) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre, grado, horario, correo, "2")
        bundle.putStringArray("datosCurso", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesCurso()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorEstudiante, detalles)
        transaccion.commit()
    }


    override fun enviarCorreo(correo: String, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("correoProfesor", correo)
        fragment.arguments = bundle
    }

    override fun cursosPopUp(cursos: ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun cosas(string: String) {
        val bundle = Bundle()
        bundle.putString("cosa", string)
        val not = estudianteNoticias()
        not.arguments = bundle

    }

    override fun enviarDatosNoticias(noticia: Noticia) {
        val bundle = Bundle()
        val datos = arrayOf(noticia.titulo, noticia.contenido, noticia.fecha)
        bundle.putStringArray("noticiaEst", datos)
        val popUp = popUpNoticia()
        popUp.arguments = bundle
        popUp.show(this.supportFragmentManager, "NoticiaPopUp")
    }

    override fun enviarCedula(cedula: String, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("cedulaEst", cedula)
        fragment.arguments = bundle
    }

    override fun tareaNoticia(esTarea: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean("esTarea", esTarea)
        val noticia = estudianteNoticias()
        noticia.arguments = bundle
    }

}