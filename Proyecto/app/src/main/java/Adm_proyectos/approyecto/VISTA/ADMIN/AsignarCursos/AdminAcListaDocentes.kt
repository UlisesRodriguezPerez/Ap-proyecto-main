package Adm_proyectos.approyecto.VISTA.ADMIN.AsignarCursos

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import android.graphics.Color
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_ac_lista_docentes.*
import kotlinx.android.synthetic.main.admin_ac_lista_docentes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminAcListaDocentes : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var columnas: List<TableRow>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_ac_lista_docentes, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCeds = listOf<TextView>(view.cedulaAcD1, view.cedulaAcD2, view.cedulaAcD3, view.cedulaAcD4,
            view.cedulaAcD5, view.cedulaAcD6, view.cedulaAcD7, view.cedulaAcD8)
        val lisCeds = ArrayList<TextView>()
        lisCeds.addAll(listaCeds)
        val listaNoms = listOf<TextView>(view.nombreAcD1, view.nombreAcD2, view.nombreAcD3, view.nombreAcD4,
            view.nombreAcD5, view.nombreAcD6, view.nombreAcD7, view.nombreAcD8)
        val lisNoms = ArrayList<TextView>()
        lisNoms.addAll(listaNoms)

        var cedulaSelec: TextView = cedulaAcD1
        columnas = listOf<TableRow>(view.colum1, view.colum2, view.colum3, view.colum4, view.colum5, view.colum6, view.colum7, view.colum8)

        obtenerProfesores(lisNoms, lisCeds, false)
        avanzarDoc.setOnClickListener{
            obtenerProfesores(lisNoms, lisCeds, true)
        }

        colum1.setOnClickListener(){
            colum1.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum1)
            cedulaSelec = cedulaAcD1
        }

        colum2.setOnClickListener(){
            colum2.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum2)
            cedulaSelec = cedulaAcD2
        }

        colum3.setOnClickListener(){
            colum3.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum3)
            cedulaSelec = cedulaAcD3
        }

        colum4.setOnClickListener(){
            colum4.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum4)
            cedulaSelec = cedulaAcD4
        }

        colum5.setOnClickListener(){
            colum5.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum5)
            cedulaSelec = cedulaAcD5
        }

        colum6.setOnClickListener(){
            colum6.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum6)
            cedulaSelec = cedulaAcD6
        }

        colum7.setOnClickListener(){
            colum7.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum7)
            cedulaSelec = cedulaAcD7
        }

        colum8.setOnClickListener(){
            colum8.setBackgroundColor(Color.parseColor("#5A9CE8"))
            columnaSeleccionada(columnas, colum8)
            cedulaSelec = cedulaAcD8
        }

        agregarNuevoProfesor.setOnClickListener(){
            val cedula = cedulaSelec.text.toString()
            val datos = arguments?.getStringArray("datosCurso")
            val id = datos?.get(0).toString()
            val grado = datos?.get(1).toString()
            asignarProfesor(cedula, id, grado)
        }
    }

    fun columnaSeleccionada(columnas:List<TableRow>, columnaActual: TableRow?){
        for (colum in columnas) {
            if (colum != columnaActual)
                colum.setBackgroundResource(R.drawable.seleccionada)
        }
    }

    private fun obtenerProfesores(listaNoms: ArrayList<TextView>, listaCeds: ArrayList<TextView>, avanzar:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getProfesores()
            val listaNomsA = ArrayList<String>()
            val listaCedsA = ArrayList<String>()
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val profesores = call.body()
                    if (profesores != null) {
                        for (profe in profesores) {
                            listaNomsA.add(profe.get("nombre").toString().replace("\"", ""))
                            listaCedsA.add(profe.get("cedula").toString().replace("\"", ""))
                        }
                        llenarLista(listaNomsA, listaCedsA, listaNoms, listaCeds, avanzar)
                    }
                } else {
                    controller.notificacion("No se pudo conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun llenarLista(listaIdsA: ArrayList<String>, listaNomsA: ArrayList<String>, listaIds: ArrayList<TextView>,
                            listaNoms: ArrayList<TextView>, avanzar: Boolean) {
        var indice = 0
        if (!avanzar){
            if(listaIdsA.size>=8) {
                for (id in listaIds) {
                    id.text = listaIdsA[indice]
                    listaNoms[indice].text = listaNomsA[indice]
                    indice++
                }
            }
            else{
                for(id in listaIdsA){
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

    private fun limpiarLista(listaIds: ArrayList<TextView>, listaNoms: ArrayList<TextView>) {
        var indice = 0
        for(elemento in listaIds){
            elemento.text = ""
            listaNoms[indice].text = ""
            indice ++
        }
    }

    private fun obtenerIndiceActual(listaIdsA: ArrayList<String>, listaIds: ArrayList<TextView>): Int {
        var nuevoInd = 0
        for(id in listaIdsA){
            if(listaIds[7].text.equals(id)){
                nuevoInd = listaIdsA.indexOf(id)
                break
            }
        }
        return nuevoInd
    }

    private fun asignarProfesor(cedula: String, codigo: String, grado: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.asignarProfesor(cedula, codigo, grado)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("asignarprofe")
                        if (resultado.asInt == 0) {
                            asignacionExitosa()
                        }else{
                            controller.notificacion("Error al asignar el profesor, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("Error al asignar el profesor, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun asignacionExitosa() {
        controller.notificacion("Profesor asignado con éxito!!", activity!!)
        val listaCursos = AdminAcListaCursos()
        controller.cambiarFragment(listaCursos, R.id.contenedor, activity!!)
        columnaSeleccionada(columnas, null)
    }
}