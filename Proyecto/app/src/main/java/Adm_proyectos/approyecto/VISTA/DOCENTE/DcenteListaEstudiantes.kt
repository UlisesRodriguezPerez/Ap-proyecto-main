package Adm_proyectos.approyecto.VISTA.DOCENTE

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.docentes_lista_estudiantes.*
import kotlinx.android.synthetic.main.docentes_lista_estudiantes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class docentesListaEstudiantes : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var idCurso: String
    private lateinit var grado: String
    private lateinit var correo: String
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var comunicador: DatosDocente
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.docentes_lista_estudiantes, container, false)
        comunicador = activity as DatosDocente
        val datos = arguments?.getStringArray("datosCursoPequeno2")
        idCurso = datos?.get(0).toString()
        grado = datos?.get(1).toString()
        correo = datos?.get(2).toString()
        nombre = datos?.get(3).toString()
        apellido = datos?.get(4).toString()
        view.idCursoDocentes.text = idCurso

        val listagradoDcs = listOf<TextView>(view.gradoDcE1, view.gradoDcE2, view.gradoDcE3, view.gradoDcE4,
            view.gradoDcE5, view.gradoDcE6, view.gradoDcE7, view.gradoDcE8)
        val listaNoms = listOf<TextView>(view.nomDcE1, view.nomDcE2, view.nomDcE3, view.nomDcE4,
            view.nomDcE5, view.nomDcE6, view.nomDcE7, view.nomDcE8)

        estudiantesPorCurso(idCurso, grado, listagradoDcs, listaNoms, false)

        view.avanzarEstCurso.setOnClickListener {
            estudiantesPorCurso(idCurso, grado, listagradoDcs, listaNoms, true)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        atrasDocEst.setOnClickListener {
            cursoInfo(idCurso, grado)
        }

        view.colum1.setOnClickListener{
            enviarDatos(gradoDcE1)
        }

        view.colum2.setOnClickListener{
            enviarDatos(gradoDcE2)
        }

        view.colum3.setOnClickListener{
            enviarDatos(gradoDcE3)
        }

        view.colum4.setOnClickListener{
            enviarDatos(gradoDcE4)
        }

        view.colum5.setOnClickListener {
            enviarDatos(gradoDcE5)
        }

        view.colum6.setOnClickListener{
            enviarDatos(gradoDcE6)
        }

        view.colum7.setOnClickListener{
            enviarDatos(gradoDcE7)
        }

        view.colum8.setOnClickListener{
            enviarDatos(gradoDcE8)
        }

    }

    private fun estudiantesPorCurso(codigo: String, clase: String, cedsI: List<TextView>,
                                    nomsI: List<TextView>, avanzar: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.estudiantesPorCurso(codigo, clase)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val estudiantes = call.body()
                    val ceds = ArrayList<String>()
                    val noms = ArrayList<String>()
                    if (estudiantes != null) {
                        for (estudiante in estudiantes) {
                            val nombre = estudiante.get("nombre").toString().replace("\"", "")
                            val apellido = estudiante.get("apellido").toString().replace("\"", "")
                            val cedula = estudiante.get("cedula").toString().replace("\"", "")
                            noms.add("$nombre $apellido")
                            ceds.add(cedula)
                        }
                        llenarTabla(noms, ceds, cedsI, nomsI, avanzar)
                    }
                } else {
                    print("Error! Conexion con el Adm_proyectos.approyecto.API Fallida")
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

    private fun enviarDatos(nombre: TextView){
        val datos = nombre.text.split(" ")
        val nombre = datos[0]
        var apellido = datos[1]
        if(datos.size == 3)
            apellido = datos[1]+" "+datos[2]

        infoEstudiante(nombre, apellido)
    }

    fun infoEstudiante(nombreE: String, apellidoE: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getInfoEstudiante(nombreE, apellidoE)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val estudiantes = call.body()
                    if (estudiantes != null) {
                        for (estudiante in estudiantes) {
                            val nomb = estudiante.get("nombre").toString().replace("\"", "") + " " +
                                    estudiante.get("apellido").toString().replace("\"", "")
                            val ced = estudiante.get("cedula").toString().replace("\"", "")
                            val correoEst = estudiante.get("correo").toString().replace("\"", "")
                            comunicador.enviarDatosEstudiante(nomb, ced, grado, idCurso, correo, correoEst, nombre, apellido)
                        }

                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

}