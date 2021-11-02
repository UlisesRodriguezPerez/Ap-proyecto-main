package Adm_proyectos.approyecto.VISTA.ESTUDIANTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes.AdminGdListaDocentes
import Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes.AdminGdModificar
import Adm_proyectos.approyecto.VISTA.DOCENTE.DocenteDetallesCurso
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosEstudiante
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gc_detalles.*
import kotlinx.android.synthetic.main.admin_gd_detalles.*
import kotlinx.android.synthetic.main.admin_gd_detalles.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class estudianteDetallesDocente:Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var correo: String
    private lateinit var calificacion: String
    private lateinit var ced: String
    private lateinit var contra: String
    private lateinit var nombreE: String
    private lateinit var apellidoE: String
    private lateinit var cedula: String
    private lateinit var idCurso: String
    private lateinit var grado: String
    private lateinit var comunicador: DatosDocente
    private lateinit var comunicador2: DatosEstudiante
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_gd_detalles, container, false)
        val array = arguments?.getStringArray("datosDocente")
        comunicador = activity as DatosDocente
        comunicador2 = activity as DatosEstudiante
        ced = array?.get(0).toString()
        val nomD = array?.get(1)
        correo = array?.get(2).toString()
        calificacion = array?.get(3).toString()
        contra = array?.get(4).toString()
        nombreE = array?.get(5).toString()
        apellidoE = array?.get(6).toString()
        cedula = array?.get(7).toString()
        idCurso = array?.get(8).toString()
        grado = array?.get(9).toString()
        view.cedulaD.text = ced
        view.nombreD.text = nomD
        view.correoD.text = correo
        view.ratingD.rating = calificacion.toFloat() ?: 0.0F

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eliminarDocente.visibility = View.INVISIBLE
        modificarDocenteP.visibility = View.INVISIBLE
        listCursos.visibility = View.INVISIBLE
        verListaDocente.visibility = View.INVISIBLE
        calificarProfesor.visibility = View.VISIBLE

        calificarProfesor.setOnClickListener{
            comunicador2.enviarCal(ced)
        }

        volverEst.setOnClickListener {
            cursoInfo(idCurso, grado)
        }

    }

    private fun cursoInfo(codigoCurso: String, gradoCurso: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getCursoInfo(codigoCurso, gradoCurso)

            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val cursos = call.body()
                    if (cursos != null) {
                        for (curso in cursos) {
                            val id = curso.get("codigo").toString().replace("\"", "")
                            val nombre = curso.get("nombre").toString().replace("\"", "")
                            val grado = curso.get("clase").toString().replace("\"", "")
                            val horario = curso.get("diaSemana").toString().replace("\"", "")+
                                    " de " + curso.get("horaInicio").toString().replace("\"", "") + " a " +
                                    curso.get("horaFin").toString().replace("\"", "")
                            val detalles = DocenteDetallesCurso()
                            comunicador.enviarDatosCurso(id, nombre, grado, horario, detalles, cedula, nombreE, apellidoE)
                        }
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

//    fun popUp2(){
//        val dialogo = estudianteCalificarDocente()
//        dialogo.show(activity!!.supportFragmentManager, "Prueba")
//    }

}