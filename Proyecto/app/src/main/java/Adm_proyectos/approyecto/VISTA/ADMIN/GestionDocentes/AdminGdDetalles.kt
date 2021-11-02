package Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ESTUDIANTE.estudianteCalificarDocente
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gd_detalles.*
import kotlinx.android.synthetic.main.admin_gd_detalles.listCursos
import kotlinx.android.synthetic.main.admin_gd_detalles.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class AdminGdDetalles : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var correo: String
    private lateinit var calificacion: String
    private lateinit var ced: String
    private lateinit var contra: String
    private lateinit var comunicador:DatosAdmin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_gd_detalles, container, false)
        val array = arguments?.getStringArray("datosDocente")
        comunicador = activity as DatosAdmin
        ced = array?.get(0).toString()
        val nomD = array?.get(1)
        correo = array?.get(2).toString()
        calificacion = array?.get(3).toString()
        contra = array?.get(4).toString()
        view.cedulaD.text = ced
        view.nombreD.text = nomD
        view.correoD.text = correo
        view.ratingD.rating = calificacion.toFloat() ?: 0.0F

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val esEst = arguments?.getBoolean("datosDocente")

        view.verListaDocente.setOnClickListener{
//            controller.notificacion(correo, activity!!)
            cursosProfesor(correo)
        }

        view.modificarDocenteP.setOnClickListener(){
            val modificar = AdminGdModificar()
            comunicador.enviarDatosDocente(nombreD.text.toString(), cedulaD.text.toString(), correoD.text.toString(), calificacion, contra, modificar)
        }

        eliminarDocente.setOnClickListener{
            eliminarProfesor(ced)
        }

        if(esEst == true){
            eliminarDocente.visibility = View.INVISIBLE
            modificarDocenteP.visibility = View.INVISIBLE
            listCursos.visibility = View.INVISIBLE
            verListaDocente.visibility = View.INVISIBLE
            calificarProfesor.visibility = View.VISIBLE
        }
        calificarProfesor.setOnClickListener(){
            popUp2()
        }

    }

    fun cursosProfesor(correo: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getCursosProfesor(correo)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val cursos = call.body()
                    val cursosDatos = ArrayList<String>()
                    if (cursos != null) {
                        for (curso in cursos) {
                            cursosDatos.add(curso.get("nombre").toString().replace("\"", "")+
                                    "_"+curso.get("codigo").toString().replace("\"", ""))
                        }
                        comunicador.cursosPopUp(cursosDatos)
                    }
                } else {
                    print("Error! Conexion con el Adm_proyectos.approyecto.API Fallida")
                }
            }
        }
    }

    private fun eliminarProfesor(cedula: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.eliminarProfesor(cedula)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("eliminardocente")
                        if (resultado.asInt == 0) {
                            elimarExitoso()
                        }else{
                            controller.notificacion("Error al elminar el profesor, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("Error al elminar el profesor, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun elimarExitoso() {
        val lista = AdminGdListaDocentes()
        controller.cambiarFragment(lista, R.id.contenedor, activity!!)
        controller.notificacion("Docente eliminado con éxito!!", activity!!)
    }

    fun popUp2(){
        val dialogo = estudianteCalificarDocente()
        dialogo.show(activity!!.supportFragmentManager, "Prueba")
    }
}