package Adm_proyectos.approyecto.VISTA.DOCENTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.docente_detalles_estudiante.*
import kotlinx.android.synthetic.main.docente_detalles_estudiante.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class DocenteDetallesEstudiante : Fragment() {

    private lateinit var idCurso: String
    private lateinit var grado: String
    private lateinit var correo: String
    private lateinit var correoEst: String
    private lateinit var nombreP: String
    private lateinit var apellido: String
    private lateinit var cedula: String
    private lateinit var comunicador: DatosDocente
    private val controller = ControladorComponentesVista()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.docente_detalles_estudiante, container, false)
        comunicador = activity as DatosDocente
        val datos = arguments?.getStringArray("datosEstudiante")
        val nombre = datos?.get(0).toString()
        cedula = datos?.get(1).toString()
        grado = datos?.get(2).toString()
        idCurso = datos?.get(3).toString()
        correo = datos?.get(4).toString()
        correoEst = datos?.get(5).toString()
        nombreP = datos?.get(6).toString()
        apellido = datos?.get(7).toString()
        llenarDatos(nombre, cedula, grado, idCurso, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        atrasDocEstDet.setOnClickListener {
            val estudiantes = docentesListaEstudiantes()
            comunicador.enviarDatosCurso(idCurso, grado, correo, estudiantes, nombreP, apellido)
        }
        verListaCursos.setOnClickListener{
            cursosEstudiante(cedula)
        }
    }

    private fun llenarDatos(nombre: String, cedula: String, grado: String, idCurso: String, view: View) {
        view.cedulaE.text = cedula
        view.nombreE.text = nombre
        view.gradoE.text = grado
        view.idCursoDocentes.text = idCurso
    }

    fun cursosEstudiante(correo: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getCursosEstudiante(correo)

            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val cursos = call.body()
                    val cursosDatos = ArrayList<String>()
                    if (cursos != null) {
                        for (curso in cursos) {
                            cursosDatos.add(curso.get("nombre").toString().replace("\"", "")+
                                    "_"+curso.get("codigo").toString().replace("\"", ""))
                        }
                    }
                    comunicador.cursosPopUp(cursosDatos)
                } else {
                    controller.notificacion("Error! Conexion con el Adm_proyectos.approyecto.API Fallida", activity!!)
                }
            }
        }
    }


}