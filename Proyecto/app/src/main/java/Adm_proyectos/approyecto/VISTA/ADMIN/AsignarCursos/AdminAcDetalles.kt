package Adm_proyectos.approyecto.VISTA.ADMIN.AsignarCursos

import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_ac_detalles.view.*

class AdminAcDetalles : Fragment() {


    private val controller = ControladorComponentesVista()
    private lateinit var comunicador: DatosAdmin

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_ac_detalles, container, false)
        comunicador = activity as DatosAdmin
        val array = arguments?.getStringArray("datosCurso")
        val id = array?.get(0)
        val nomAc = array?.get(1)
        val grado = array?.get(2)
        val horario = array?.get(3)
        view.idCursoAc.text = " $id"
        view.nombreCursoAc.text = nomAc
        view.gradoCursoAc.text = grado
        view.horarioCursoAc.text = horario
        controller.codigoCurso = view.idCursoAc.text.toString()
        controller.gradoCurso = view.gradoCursoAc.text.toString()

        view.asignarProfesor.setOnClickListener{
            val listaDoc = AdminAcListaDocentes()
            comunicador.enviarDatosCurso(view.idCursoAc.text.toString().replace(" ", ""),
                view.gradoCursoAc.text.toString(), listaDoc)
        }

        view.agregarEstudiantes.setOnClickListener{
            val listaEstudiantes = AdminAcListaEstudiantes()
            comunicador.enviarDatosCurso(view.idCursoAc.text.toString().replace(" ", ""),
                view.gradoCursoAc.text.toString(), listaEstudiantes)
        }

        return view
    }

}