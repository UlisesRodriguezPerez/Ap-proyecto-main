package Adm_proyectos.approyecto.VISTA.ADMIN.ADMIN

import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ADMIN.AsignarCursos.AdminAcListaCursos
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes.AdminGdListaDocentes
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionEstudiantes.AdminGeDetalles
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionEstudiantes.AdminGeListaEstudiantes
import Adm_proyectos.approyecto.VISTA.ADMIN.popUpCursos
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import Adm_proyectos.approyecto.VISTA.log_in
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main._admin_pricipal.*

class adminPricipal : AppCompatActivity(), DatosAdmin{ //Comunicador, Comunicador2

    val controller = ControladorComponentesVista()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._admin_pricipal)

        val listaCurso = AdminGcListaCursos()
        controller.cambiarFragment(listaCurso, R.id.contenedor, this)

        adminGC.textSize = 20F
        adminGC.setBackgroundResource(R.drawable.button_selected)

        adminGC.setOnClickListener{
            controller.cambiarFragment(listaCurso, R.id.contenedor, this)
            adminGC.setBackgroundResource(R.drawable.button_selected)
            adminGD.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGE.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminAC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGC.textSize = 20F
            adminGD.textSize = 16F
            adminGE.textSize = 16F
            adminAC.textSize = 16F
        }

        adminGD.setOnClickListener{
            val listaDocentes = AdminGdListaDocentes()
            controller.cambiarFragment(listaDocentes, R.id.contenedor, this)
            adminGC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGD.setBackgroundResource(R.drawable.button_selected)
            adminGE.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminAC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGC.textSize = 16F
            adminGD.textSize = 20F
            adminGE.textSize = 16F
            adminAC.textSize = 16F
        }

        adminGE.setOnClickListener{
            val listaEstudiantes = AdminGeListaEstudiantes()
            controller.cambiarFragment(listaEstudiantes, R.id.contenedor, this)
            adminGC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGD.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGE.setBackgroundResource(R.drawable.button_selected)
            adminAC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGC.textSize = 16F
            adminGD.textSize = 16F
            adminGE.textSize = 20F
            adminAC.textSize = 16F
        }

        adminAC.setOnClickListener{
            val cursos = AdminAcListaCursos()
            controller.cambiarFragment(cursos, R.id.contenedor, this)
            adminGC.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGD.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminGE.setBackgroundResource(R.drawable.button_menu_admin_style)
            adminAC.setBackgroundResource(R.drawable.button_selected)
            adminGC.textSize = 16F
            adminGD.textSize = 16F
            adminGE.textSize = 16F
            adminAC.textSize = 20F
        }

        signOut.setOnClickListener{
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show()
            Intent(this, log_in::class.java).also{
                startActivity(it)
            }
        }

        contenedor.setOnClickListener{view ->
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        val nombre = intent.getStringExtra("nombre")
        nombreUsuario.text = "$nombre"
    }

    override fun enviarDatosCurso(id: String, nombre:String, grado:String, horario:String, fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("datosCurso", id)
        val datos = arrayOf(id, nombre, grado, horario)
        bundle.putStringArray("datosCurso", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedor, fragment)
        transaccion.commit()
    }

    override fun enviarDatosCurso(id: String, nombre:String, fragment: Fragment) {
        val bundle = Bundle()
        val datos = arrayOf(id, nombre)
        bundle.putStringArray("datosCurso", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedor, fragment)
        transaccion.commit()
    }

    override fun enviarDatosDocente(ced: String, nombre: String, correo: String, calificacion: String, contra:String, fragment: Fragment) {
        val bundle = Bundle()
        val datos = arrayOf(ced, nombre, correo, calificacion, contra)
        bundle.putStringArray("datosDocente", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedor, fragment)
        transaccion.commit()
    }

    override fun enviarDatosEstudiante(ced: String, nombre: String, grado: String, correo:String, contra: String, fragment: Fragment) {
        val bundle = Bundle()

        val datos = arrayOf("1", ced, nombre, grado, correo, contra)
        bundle.putStringArray("datosEstudiante", datos)

        val transaccion = this.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle

        transaccion.replace(R.id.contenedor, fragment)
        transaccion.commit()
    }

    override fun cursosPopUp(cursos: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putStringArrayList("cursos_docente", cursos)
        val popUp = popUpCursos()
        popUp.arguments = bundle
        popUp.show(this.supportFragmentManager, "CursosPopUp")
    }

}

