package Adm_proyectos.approyecto.VISTA.ADMIN.GestionEstudiantes

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_ge_modificar.*
import kotlinx.android.synthetic.main.admin_ge_modificar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGeModificar : Fragment() {

    private val controller = ControladorComponentesVista()
    private lateinit var vista: View
    private lateinit var nombre: String
    private lateinit var apellidos: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.admin_ge_modificar, container, false)
        llenarDatos()
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(
            context!!,
            R.array.ListaGrados,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            gradosGeM.adapter = adapter
        }

        guardarCambiosEstudiante.setOnClickListener{
            var apes:String = primerApModificarEst.text.toString()
            if(segundoApModificarEst.text.isNotEmpty()){
                apes = primerApModificarEst.text.toString() + " " + segundoApModificarEst.text.toString()
            }
            updateEstudiante(nombre, apellidos, cedulaModificarEstudiante.text.toString(), nombreModificarEstudiante.text.toString(), correoEstMod.text.toString(),
                contraEstMod.text.toString(), apes, gradosGeM.selectedItem.toString())
        }

    }

    fun updateEstudiante(nombreViejo: String, apellidoViejo:String, cedula: String, nombre: String, correo: String, contra: String, apellido: String, grado: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.updateEstudiante(nombreViejo, apellidoViejo, cedula, nombre, correo, contra, apellido, grado)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("actualizaralumno")
                        if (resultado.asInt == 0) {
                            controller.notificacion("Se actulizó el estudiante con éxito!!", activity!!)
                            guardadoExitos()
                        }else{
                            controller.notificacion("No se pudo actualizar el estudiante, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("No se pudo actualizar el estudiante, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun guardadoExitos() {
        cedulaModificarEstudiante.text.clear()
        nombreModificarEstudiante.text.clear()
        primerApModificarEst.text.clear()
        segundoApModificarEst.text.clear()
        correoEstMod.text.clear()
        contraEstMod.text.clear()
        val lista = AdminGeListaEstudiantes()
        controller.cambiarFragment(lista, R.id.contenedor, activity!!)
    }

    private fun llenarDatos() {
        val datos = arguments?.getStringArray("datosEstudiante")
        val cedula = datos?.get(1).toString()
        val nombreCom = datos?.get(2)?.split(" ")
        val correo = datos?.get(4)
        val contrasenna = datos?.get(5)
        nombre = nombreCom?.get(0).toString()

        val ap1 = nombreCom?.get(1).toString()
        val ap2: String
        apellidos = ap1
        if(nombreCom?.size == 3 ) {
            ap2 = nombreCom[2]
            apellidos = "$ap1 $ap2"
            vista.segundoApModificarEst.setText(ap2)
        }
        vista.cedulaModificarEstudiante.setText(cedula)
        vista.nombreModificarEstudiante.setText(nombre)
        vista.primerApModificarEst.setText(ap1)
        vista.correoEstMod.setText(correo)
        vista.contraEstMod.setText(contrasenna)
    }

}