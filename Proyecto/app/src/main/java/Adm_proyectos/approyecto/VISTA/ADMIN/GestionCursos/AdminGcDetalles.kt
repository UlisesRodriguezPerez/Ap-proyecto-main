package Adm_proyectos.approyecto.VISTA.ADMIN.ADMIN

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gc_detalles.*
import kotlinx.android.synthetic.main.admin_gc_detalles.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGcDetalles : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var comunicador: DatosAdmin
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_gc_detalles, container, false)
        comunicador = activity as DatosAdmin
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val array = arguments?.getStringArray("datosCurso")
        val idCursoS = " " + array?.get(0)
        val nombreCurso = array?.get(1).toString()
        val grado = array?.get(2).toString()
        val horario = array?.get(3).toString()
        view.idCurso.text = idCursoS
        view.nombreCurso.text = nombreCurso
        view.gradoCurso.text = grado
        view.horarioCurso.text = horario

        view.modificarCurso.setOnClickListener() {
            val modificar = AdminGcModificar()
            comunicador.enviarDatosCurso(idCursoS, nombreCurso, grado, horario, modificar)
        }

        eliminarCurso.setOnClickListener{
            eliminarCurso(idCursoS.replace(" ", ""), grado)
        }
    }

    private fun eliminarCurso(codigo: String, grado: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.eliminarCurso(codigo, grado)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("eliminarcurso")
                        if (resultado.asInt == 0) {
                            eliminadoExitoso()
                        }else{
                            controller.notificacion("Error al elminar el curso, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("Error al elminar el curso, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }
    private fun eliminadoExitoso(){
        controller.notificacion("El curso fue eliminado con éxito", activity!!)
        val listaCursos = AdminGcListaCursos()
        controller.cambiarFragment(listaCursos, R.id.contenedor, activity!!)
    }

}