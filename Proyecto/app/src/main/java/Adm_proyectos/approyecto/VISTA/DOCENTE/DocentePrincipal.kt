package Adm_proyectos.approyecto.VISTA.DOCENTE

import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.MODELO.Noticia
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionEstudiantes.AdminGeDetalles
import Adm_proyectos.approyecto.VISTA.ADMIN.popUpCursos
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
import kotlinx.android.synthetic.main._docente_principal.*
import kotlinx.android.synthetic.main._docente_principal.nombreUsuario
import kotlinx.android.synthetic.main.admin_ge_crear.*

class DocentePrincipal : AppCompatActivity(), DatosDocente, DatosEstudiante {

    private val controller = ControladorComponentesVista()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._docente_principal)

        val nombreUsu = intent.getStringExtra("nombre")
        nombreUsuario.text = "$nombreUsu"
        val correo = intent.getStringExtra("correo")
        val apellido = intent.getStringExtra("apellido").toString().replace("\"", "")

        irPrimeraPantalla(correo.toString(), nombreUsu.toString(), apellido)

        salir.setOnClickListener{
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show()
            Intent(this, log_in::class.java).also{
                startActivity(it)
            }
        }
        contenedorDocente.setOnClickListener{view ->
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    private fun irPrimeraPantalla(correo: String, nombre: String, apellido: String){
        val Lista = DocenteListaCursos()
        val bundle = Bundle()
        val datos = arrayOf(correo, nombre, apellido)
        bundle.putStringArray("datosPrimer", datos)
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        Lista.arguments = bundle
        transaction.add(R.id.contenedorDocente, Lista, null)
        transaction.commit()
    }

    override fun enviarDatosCurso(id: String, grado: String, correo: String, fragment: Fragment) {
        val bundle = Bundle()
        val datos = arrayOf(id,grado,correo)
        bundle.putStringArray("datosCursoPequeno", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(id: String, grado: String, correo: String, fragment: Fragment, nombre: String, apellido: String) {
        val bundle = Bundle()
        val datos = arrayOf(id, grado, correo, nombre, apellido)
        bundle.putStringArray("datosCursoPequeno2", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, fragment)
        transaccion.commit()
    }


    override fun enviarDatosCurso(id: String, nombre: String, grado: String, horario: String, fragment: Fragment, correo: String, nombreP: String, apellido: String) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre, grado, horario, correo, nombreP, apellido, "1")
        bundle.putStringArray("datosCursoGrande", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesCurso()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, detalles)
        transaccion.commit()
    }

    override fun enviarDatosCurso(id: String, grado: String, fragment: Fragment, correo: String, nombreP: String, apellido: String) {
        val bundle = Bundle()
        val datos = arrayOf(id, grado, correo, nombreP, apellido)
        bundle.putStringArray("datosCursoMed", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(id: String, nombre: String, grado: String, horario: String, fragment: Fragment, correo: String) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre, grado, horario, correo, "1")
        bundle.putStringArray("datosCurso", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesCurso()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, detalles)
        transaccion.commit()
    }

    override fun enviarCorreo(correo: String, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("correoProfesor", correo)
        fragment.arguments = bundle
    }

    override fun enviarDatosEstudiante(ced: String, nombre: String, grado: String, cursoId:String, correoProfe:String, correoEst:String, nombreP: String, apellido: String) {
        val bundle = Bundle()
        val datos = arrayOf(ced, nombre, grado, cursoId, correoProfe, correoEst, nombreP, apellido, "1")
        bundle.putStringArray("datosEstudiante", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        val detalles = DocenteDetallesEstudiante()
        detalles.arguments = bundle

        transaccion.replace(R.id.contenedorDocente, detalles)
        transaccion.commit()
    }

    override fun cursosPopUp(cursos: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putStringArrayList("cursos_docente", cursos)
        val popUp = popUpCursos()
        popUp.arguments = bundle
        popUp.show(this.supportFragmentManager, "CursosPopUp")
    }

    //*******************************************************************************
    override fun cosas(string: String) {
        TODO("Not yet implemented")
    }

    override fun enviarDatosNoticias(noticia: Noticia) {
        TODO("Not yet implemented")
    }

    override fun enviarCedula(cedula: String, fragment: Fragment) {
        TODO("Not yet implemented")
    }

    override fun tareaNoticia(esTarea: Boolean) {
        TODO("Not yet implemented")
    }

    override fun enviarDatosDocente(
        ced: String,
        nombre: String,
        correo: String,
        calificacion: String,
        contra: String,
        fragment: Fragment,
        nombreE: String,
        apellido: String,
        cedula: String,
        id: String,
        grado: String
    ) {
        TODO("Not yet implemented")
    }

    override fun enviarCal(cedula: String) {
        TODO("Not yet implemented")
    }


    override fun enviarDatosCurso(id: String, nombre: String, grado: String, horario: String, fragment: Fragment, cedula: String, nombreP: String, apellido: String, correo: String) {
        TODO("Not yet implemented")
    }
}