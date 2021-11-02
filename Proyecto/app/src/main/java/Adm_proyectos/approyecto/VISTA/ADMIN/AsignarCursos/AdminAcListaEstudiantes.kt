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
import kotlinx.android.synthetic.main.admin_ac_lista_estudiantes.*
import kotlinx.android.synthetic.main.admin_ac_lista_estudiantes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminAcListaEstudiantes : Fragment() {

    val controller = ControladorComponentesVista()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_ac_lista_estudiantes, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickeada = ArrayList<Int>()
        val click = listOf(0, 0, 0, 0, 0, 0, 0, 0)
        clickeada.addAll(click)

        val listaGrados = listOf<TextView>(view.gradoAcE1, view.gradoAcE2, view.gradoAcE3, view.gradoAcE4,
            view.gradoAcE5, view.gradoAcE6, view.gradoAcE7, view.gradoAcE8)
        val listaNombres = listOf<TextView>(view.nomAcE1, view.nomAcE2, view.nomAcE3, view.nomAcE4,
            view.nomAcE5, view.nomAcE6, view.nomAcE7, view.nomAcE8)

        obtenerListaEstudiantes(listaNombres, listaGrados, false)

        flechaAde.setOnClickListener{
            obtenerListaEstudiantes(listaNombres, listaGrados, true)
        }

        colum1.setOnClickListener(){
            seleccionar(clickeada, 0, colum1)
        }

        colum2.setOnClickListener(){
            seleccionar(clickeada, 1, colum2)
        }

        colum3.setOnClickListener(){
            seleccionar(clickeada, 2, colum3)
        }

        colum4.setOnClickListener(){
            seleccionar(clickeada, 3, colum4)
        }

        colum5.setOnClickListener(){
            seleccionar(clickeada, 4, colum5)
        }

        colum6.setOnClickListener(){
            seleccionar(clickeada, 5, colum6)
        }

        colum7.setOnClickListener(){
            seleccionar(clickeada, 6, colum7)
        }

        colum8.setOnClickListener(){
            seleccionar(clickeada, 7, colum8)
        }

        agregarEstudiantesAc.setOnClickListener{
            var i = 0
            val datos = arguments?.getStringArray("datosCurso")
            val codigo = datos?.get(0).toString()
            val grado  = datos?.get(1).toString()
            for (selec in clickeada){
                if (selec == 1){
                    val nombreCom = listaGrados[i].text.split(" ")
                    val nombre = nombreCom[0]
                    var apellido = nombreCom[1]
                    if (nombreCom.size == 3){
                        apellido = nombreCom[1] + " " + nombreCom[2]
                    }
                    asignarEstudiante(nombre, apellido, codigo, grado)
                    i +=1
                }
                else
                    i+=1
            }

        }
    }

    fun seleccionar(clickeada: ArrayList<Int>, posicion: Int, columnaActual: TableRow){
        if (clickeada[posicion] == 1) {
            columnaActual.setBackgroundResource(R.drawable.seleccionada)
            clickeada[posicion] = 0
        }
        else {
            columnaActual.setBackgroundColor(Color.parseColor("#5A9CE8"))
            clickeada[posicion] = 1
        }
    }

    fun asignarEstudiante(nombre: String, apellido: String, codigo: String, grado: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.asignarEstudiante(nombre, apellido, codigo, grado)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados?.get(0)?.get("asignaralumno")
                        if (resultado.asInt == 0) {
                            controller.notificacion("Estudiante agregado con éxito", activity!!)
                        }else{
                            controller.notificacion("Error al asignar el estudiante", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("Error al asignar el estudiante", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

    private fun obtenerListaEstudiantes(listaIds: List<TextView>, listaNoms: List<TextView>, avanzar: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getEstudiantes()

            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val estudiantes = call.body()
                    val listaClaseA = ArrayList<String>()
                    val listaNomsA = ArrayList<String>()
                    if (estudiantes != null) {
                        for (estudiante in estudiantes) {
                            listaNomsA.add(estudiante.get("nombre").toString().replace("\"", "") + " " +
                                    estudiante.get("apellido").toString().replace("\"", ""))
                            listaClaseA.add(estudiante.get("clase").toString().replace("\"", ""))
                        }
                        llenarTabla(listaClaseA, listaNomsA, listaIds, listaNoms, avanzar)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
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

}