package Adm_proyectos.approyecto.VISTA.DOCENTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.docetes_asignar_tarea.*
import kotlinx.android.synthetic.main.docetes_asignar_tarea.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DocentesAsignarTarea : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var id: String
    private lateinit var grado: String
    private lateinit var correo: String
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var comunicador: DatosDocente
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.docetes_asignar_tarea, container, false)
        comunicador = activity as DatosDocente
        val datos = arguments?.getStringArray("datosCursoPequeno2")
        id = datos?.get(0).toString()
        grado = datos?.get(1).toString()
        correo = datos?.get(2).toString()
        nombre = datos?.get(3).toString()
        apellido = datos?.get(4).toString()
        view.idCursoDocentes.text = id
        return view
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.asignarTarea.setOnClickListener(){
            if (idTarea.text.isNotEmpty() && tituloTarea.text.isNotEmpty() && contenidoTarea.text.isNotEmpty()){
                val sdf = SimpleDateFormat("yyyy/M/dd")
                val currentDate = sdf.format(Date())
                insertarTarea(id, grado, idTarea.text.toString(), tituloTarea.text.toString(), contenidoTarea.text.toString(), currentDate)
            }

        }
    }

    private fun insertarTarea(codigo: String, clase: String, codigotarea:String, titulo: String, contenido: String, fecha: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.insertarTarea(codigo, clase, codigotarea, titulo, contenido, fecha)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("insertartarea")
                        if (resultado.asInt == 0) {
                            tareaEnviada()
                        }else{
                            controller.notificacion("Error al enviar la noticia", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("Error al enviar la noticia", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

    private fun tareaEnviada() {
        controller.notificacion("La tarea fue enviada con éxito!!", activity!!)
        cursoInfo(id, grado)

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
                            val nombreC = curso.get("nombre").toString().replace("\"", "")
                            val grado = curso.get("clase").toString().replace("\"", "")
                            val horario = curso.get("diaSemana").toString().replace("\"", "")+
                                    " de " + curso.get("horaInicio").toString().replace("\"", "") + " a " +
                                    curso.get("horaFin").toString().replace("\"", "")
                            val detalles = DocenteDetallesCurso()
                            comunicador.enviarDatosCurso(id, nombreC, grado, horario, detalles, correo, nombre, apellido)

                        }
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

}