package Adm_proyectos.approyecto.VISTA.DOCENTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosEstudiante
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.docente_lista_cursos.*
import kotlinx.android.synthetic.main.docente_lista_cursos.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DocenteListaCursos : Fragment() {

    private lateinit var comunicador: DatosDocente
    private lateinit var comunicador2: DatosEstudiante
    private lateinit var nombreP: String
    private lateinit var apellidoP: String
    private var estudiante = false
    private val controller = ControladorComponentesVista()
    private var correo: String = ""
    private var cedula: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        comunicador = activity as DatosDocente
        comunicador2 = activity as DatosEstudiante
        val view = inflater.inflate(R.layout.docente_lista_cursos, container, false)
        val datos = arguments?.getString("correoProfesor")
        val datosEst = arguments?.getStringArray("datosEst")
        if(arguments != null && datos == null && datosEst?.get(0) == null){
            val datos = arguments!!.getStringArray("datosPrimer")
            correo = datos?.get(0).toString()
            nombreP = datos?.get(1).toString()
            apellidoP = datos?.get(2).toString()
        } else{
            if (datos != null) {
                correo = datos.toString()
                cedula = correo
            }
            else{
                if (datosEst != null){
                    estudiante = true
                    correo = datosEst[0]
                    nombreP = datosEst[1]
                    apellidoP = datosEst[2]
                    cedula = datosEst[3]
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCol = listOf<TableRow>(view.columna1, view.columna2, view.columna3, view.columna4, view.columna5, view.columna6, view.columna7, view.columna8)
        val listaIds = listOf<TextView>(view.idDc1, view.idDc2, view.idDc3, view.idDc4,
            view.idDc5, view.idDc6, view.idDc7, view.idDc8)
        val listaNoms = listOf<TextView>(view.nombreDc1, view.nombreDc2, view.nombreDc3, view.nombreDc4,
            view.nombreDc5, view.nombreDc6, view.nombreDc7, view.nombreDc8)
        cursosEstudiante(cedula, listaIds, listaNoms, false)
        cursosProfesor(correo, listaIds, listaNoms, false)


        avanzarDocCursos.setOnClickListener{
            if (cedula != "")
                cursosProfesor(correo, listaIds, listaNoms, true)
            else
                cursosEstudiante(cedula, listaIds, listaNoms, true)
        }

        for (i in listaCol.indices){
            listaCol[i].setOnClickListener{
                if(listaIds[i].text.isNotEmpty())
                    enviarDatos(listaIds[i])
            }
        }

    }

    fun cursosProfesor(correo: String, listaIds: List<TextView>, listaNoms: List<TextView>, avanzar: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getCursosProfesor(correo)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val cursos = call.body()
                    val ids = ArrayList<String>()
                    val noms = ArrayList<String>()
                    if (cursos != null) {
                        for (curso in cursos) {
                            ids.add(curso.get("codigo").toString().replace("\"", "")+
                            "_"+curso.get("clase").toString().replace("\"", ""))
                            noms.add(curso.get("nombre").toString().replace("\"", ""))
                        }
                        llenarTabla(ids, noms, listaIds, listaNoms, avanzar)
                    }
                }
            }
        }
    }

    private fun cursosEstudiante(cedula: String, listaIds: List<TextView>, listaNoms: List<TextView>, avanzar: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getCursosEstudiante(cedula)

            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val cursos = call.body()
                    if (cursos != null) {
                        val ids = ArrayList<String>()
                        val noms = ArrayList<String>()
                        for (curso in cursos) {
                            ids.add(curso.get("codigo").toString().replace("\"", "")+
                                    "_"+curso.get("clase").toString().replace("\"", ""))
                            noms.add(curso.get("nombre").toString().replace("\"", ""))
                        }
                        llenarTabla(ids, noms, listaIds, listaNoms, avanzar)
                    }
                } else {
//                    controller.notificacion("Error! Conexion con el Adm_proyectos.approyecto.API Fallida", activity!!)
                }
            }
        }
    }

    private fun llenarTabla(listaIdsA: ArrayList<String>, listaNomsA: ArrayList<String>,
                            listaIds: List<TextView>, listaNoms: List<TextView>, avanzar:Boolean) {
        var indice = 0
        if (!avanzar){
            if (listaIdsA.size>=8) {
                for (id in listaIds) {
                    id.text = listaIdsA[indice]
                    listaNoms[indice].text = listaNomsA[indice]
                    indice++
                }
            }
            else {
                for (id in listaIdsA) {
                    listaIds[indice].text = id
                    listaNoms[indice].text = listaNomsA[indice]
                    indice++
                }
            }
        }
        else{
            if (listaIdsA.size>8){
                var nuevoInd:Int = obtenerIndiceActual(listaIdsA, listaIds)
                var ind = 0
                limpiarLista(listaIds, listaNoms)
                val restantes = (listaIdsA.size - (nuevoInd+1))

                if(restantes >=8){
                    for(id in listaIds){
                        id.text = listaIdsA[nuevoInd]
                        listaNoms[ind].text = listaNomsA[nuevoInd]
                        ind++
                        nuevoInd++
                    }
                }
                else{
                    nuevoInd+=1
                    for(i in 0 until restantes){
                        listaIds[ind].text = listaIdsA[nuevoInd]
                        listaNoms[ind].text = listaNomsA[nuevoInd]
                        ind++
                        nuevoInd++
                    }
                }

            }

        }
    }

    private fun obtenerIndiceActual(listaIdsA: ArrayList<String>, listaIds: List<TextView>): Int {
        var nuevoInd = 0
        for(id in listaIdsA){
            if(listaIds[7].text.equals(id)){
                nuevoInd = listaIdsA.indexOf(id)
                break
            }
        }
        return nuevoInd
    }

    private fun limpiarLista(listaIds: List<TextView>, listaNoms: List<TextView>) {
        var indice = 0
        for(elemento in listaIds){
            elemento.text = ""
            listaNoms[indice].text = ""
            indice ++
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
                            if (estudiante) {
                                comunicador.enviarDatosCurso(id, nombre, grado, horario, detalles, cedula, nombreP, apellidoP, correo)
                            }
                            else{
                                comunicador.enviarDatosCurso(id, nombre, grado, horario, detalles, correo, nombreP, apellidoP)
                            }
                        }
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

    private fun enviarDatos(id:TextView){
        val datos = id.text.split("_")
        val codigo = datos[0]
        val grado = datos[1]
        cursoInfo(codigo, grado)
    }
}