package Adm_proyectos.approyecto.VISTA.ESTUDIANTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.pop_up_estudiante_calificar_docente.*
import kotlinx.android.synthetic.main.pop_up_estudiante_calificar_docente.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class estudianteCalificarDocente : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pop_up_estudiante_calificar_docente, container, false)
        val ced = arguments?.getString("ced")
        view.cal.setOnClickListener{
            actualizarNotaProfesor(ced.toString(), ratingBar.rating.toString());
        }
        return view
    }

    private fun actualizarNotaProfesor(cedula: String, nuevaNota: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.actualizarNotaProfesor(cedula, nuevaNota)
            if (call.isSuccessful) {
                val notaProfesor = call.body()
                if (notaProfesor != null) {
                    val nota = notaProfesor?.get(0)
                }
            } else {
                print("Error! Conexion con el Adm_proyectos.approyecto.API Fallida")
            }
        }
    }
}