package Adm_proyectos.approyecto.VISTA.ADMIN.ADMIN

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gc_modificar_curso.*
import kotlinx.android.synthetic.main.admin_gc_modificar_curso.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGcModificar : Fragment() {

    private lateinit var vista: View
    private var id: String = ""
    private var grado: String = ""
    private val controller = ControladorComponentesVista()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.admin_gc_modificar_curso, container, false)
        llenarDatos()
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArrayAdapter.createFromResource(activity!!, R.array.ListaGrados, R.layout.support_simple_spinner_dropdown_item).also {
            adapter -> adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            gradoModificarCurso.adapter = adapter
        }
        ArrayAdapter.createFromResource(activity!!, R.array.diasSemana, R.layout.support_simple_spinner_dropdown_item).also {
         adapter -> adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
         diaModificarCurso.adapter = adapter
        }
        guardarCursoGCM.setOnClickListener{
            updateCurso(id, grado,idModificarCurso.text.toString().replace(" ", ""),
                nombreModificarCurso.text.toString(), gradoModificarCurso.selectedItem.toString(),
                diaModificarCurso.selectedItem.toString(),
                horaInicioModificarCurso.text.toString().replace(" ", ""),
                horafinModificarCurso.text.toString().replace(" ", ""))
        }
    }

    private fun updateCurso(codigoViejo: String, gradoViejo: String, codigo: String, nombre: String, gradoId: String, diaSemana: String, horaInicio: String, horaFin : String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.updateCurso(codigoViejo, gradoViejo, codigo, nombre, gradoId, diaSemana, horaInicio, horaFin)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        val resultado = resultados[0].get("actualizarcurso")
                        if (resultado.asInt == 0) {
                            controller.notificacion("Curso modificado con éxito!!", activity!!)
                            actualizarFinalizado()
                        }else{
                            controller.notificacion("No se pudo actualizar el curso, inten de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("No se pudo actualizar alguna de las características del curso, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun actualizarFinalizado() {
        val listaCursos = AdminGcListaCursos()
        controller.cambiarFragment(listaCursos, R.id.contenedor, activity!!)
    }

    private fun llenarDatos(){
        val grados = ArrayList<String>()
        grados.add("prepa")
        grados.add("1")
        grados.add("2")
        grados.add("3")
        grados.add("4")
        grados.add("5")
        grados.add("6")
        grados.add("7")
        grados.add("8")
        grados.add("9")
        grados.add("10")
        grados.add("11")

        val arrayDatos = arguments?.getStringArray("datosCurso")
        id = arrayDatos?.get(0).toString().replace(" ", "")
        val nombre = arrayDatos?.get(1)
        grado = arrayDatos?.get(2).toString()
        val horario = arrayDatos?.get(3)?.split(" ")
        vista.idModificarCurso.setText(id)
        vista.idCursoModificar.text = id
        vista.nombreModificarCurso.setText(nombre)

        vista.horaInicioModificarCurso.setText(horario?.get(2))
        vista.horafinModificarCurso.setText(horario?.get(4))
    }


}