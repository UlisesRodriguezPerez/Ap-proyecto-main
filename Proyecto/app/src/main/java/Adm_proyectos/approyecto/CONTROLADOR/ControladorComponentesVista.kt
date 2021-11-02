package Adm_proyectos.approyecto.CONTROLADOR


import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class ControladorComponentesVista {
    var codigoCurso: String = ""
    var gradoCurso: String = ""
    var correo = ""
    fun cambiarFragment(fragment: Fragment, id:Int, activity: FragmentActivity){
        val transacion = (activity).supportFragmentManager.beginTransaction()
        transacion.replace(id, fragment)
        transacion.commit()
    }

    fun notificacion(notificacion: String, activity: FragmentActivity){
        Toast.makeText(activity, notificacion, Toast.LENGTH_LONG).show()
    }

}