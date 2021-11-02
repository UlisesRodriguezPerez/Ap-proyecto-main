package Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosAdmin
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gd_lista_docentes.*
import kotlinx.android.synthetic.main.admin_gd_lista_docentes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGdListaDocentes : Fragment() {

    private val controller = ControladorComponentesVista()
    private val crearDocente = AdminGdCrearDocente()
    private lateinit var comunicador: DatosAdmin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.admin_gd_lista_docentes, container, false)
        comunicador = activity as DatosAdmin
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCeds = listOf<TextView>(view.cedula1, view.cedula2, view.cedula3, view.cedula4,
            view.cedula5, view.cedula6, view.cedula7, view.cedula8)
        val lisCeds = ArrayList<TextView>()
        lisCeds.addAll(listaCeds)
        val listaNoms = listOf<TextView>(view.nombre1, view.nombre2, view.nombre3, view.nombre4,
            view.nombre5, view.nombre6, view.nombre7, view.nombre8)
        val lisNoms = ArrayList<TextView>()
        lisNoms.addAll(listaNoms)

        obtenerProfesores(lisNoms, lisCeds, false)
        view.agregarNuevoProfesor.setOnClickListener() {
            controller.cambiarFragment(crearDocente, R.id.contenedor, activity!!)
        }
        view.colum1.setOnClickListener(){
            enviarDatos(cedula1)
        }

        view.colum2.setOnClickListener(){
            enviarDatos(cedula2)
        }

        view.colum3.setOnClickListener(){
            enviarDatos(cedula3)
        }

        view.colum4.setOnClickListener(){
            enviarDatos(cedula4)
        }

        view.colum5.setOnClickListener(){
            enviarDatos(cedula5)
        }

        view.colum6.setOnClickListener(){
            enviarDatos(cedula6)
        }

        view.colum7.setOnClickListener(){
            enviarDatos(cedula7)
        }

        view.colum8.setOnClickListener(){
            enviarDatos(cedula8)
        }

        view.adelante.setOnClickListener{
            obtenerProfesores(lisNoms, lisCeds, true)
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


    private fun enviarDatos(cedula:TextView){
        infoProfesor(cedula.text.toString())
    }

    private fun infoProfesor(cedula: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getInfoProfesor(cedula)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val profes = call.body()
                    if (profes != null) {
                        for (profe in profes) {
                            val ced = profe.get("cedula").toString().replace("\"", "")
                            val nombre = profe.get("nombre").toString().replace("\"", "")
                            val apellidos = profe.get("apellido").toString().replace("\"", "")
                            val correo = profe.get("correo").toString().replace("\"", "")
                            val calificacionPromedio = profe.get("calificacion").toString().replace("\"", "")
                            val contra = profe.get("contrasenna").toString().replace("\"", "")
                            val detalles = AdminGdDetalles()
                            comunicador.enviarDatosDocente(ced, "$nombre $apellidos", correo, calificacionPromedio, contra, detalles)
                        }
                    }
                } else {
                    print("Error! Conexion con el Adm_proyectos.approyecto.API Fallida")
                }
            }
        }
    }


}