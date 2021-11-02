package Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.admin_gd_modificar.*
import kotlinx.android.synthetic.main.admin_gd_modificar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminGdModificar : Fragment() {

    private lateinit var vista:View
    var cedula = ""
    private val controller = ControladorComponentesVista()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.admin_gd_modificar, container, false)
        llenarDatos()
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modificarDocente.setOnClickListener(){
            val apellidos: String
            if(segundoApellidoModificarDocente.text.isNotEmpty()){
                apellidos = primerApellidoModificarDocente.text.toString()+" "+segundoApellidoModificarDocente.text.toString()
            }
            else{
                apellidos = primerApellidoModificarDocente.text.toString()
            }
            updateProfesor(cedula, cedulaModificarDocente.text.toString().replace(" ", ""),
                nombreModificarDocente.text.toString().replace(" ", ""),
                correoModificarDocente.text.toString().replace(" ", ""),
                contraModifaGd.text.toString(),
                apellidos)


        }
    }

    private fun llenarDatos() {
        val datos = arguments?.getStringArray("datosDocente")
        cedula = datos?.get(1).toString()
        val nombreCom = datos?.get(0)?.split(" ")
        val correo = datos?.get(2)
        val nombre = nombreCom?.get(0)
        val ap1 = nombreCom?.get(1)
        val contra = datos?.get(4).toString()
        var ap2 = ""
        if(nombreCom?.size == 3 ) {
            ap2 = nombreCom[2]
        }
        vista.cedulaModificarDocente.setText(cedula)
        vista.nombreModificarDocente.setText(nombre)
        vista.primerApellidoModificarDocente.setText(ap1)
        vista.segundoApellidoModificarDocente.setText(ap2)
        vista.correoModificarDocente.setText(correo)
        vista.contraModifaGd.setText(contra)

    }

    private fun updateProfesor(cedulaVieja: String, cedula: String, nombre: String, correo: String, contra: String, apellido: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.updateProfesor(cedulaVieja, cedula, nombre, correo, contra, apellido)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("actualizardocente")
                        if (resultado.asInt == 0) {
                            controller.notificacion("Docente editado con éxito!!!", activity!!)
                            guardadoExitos()
                        }else{
                            controller.notificacion("No se pudo actualizar la información, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("No se pudo actualizar la información, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun guardadoExitos() {
        val lista = AdminGdListaDocentes()
        controller.cambiarFragment(lista, R.id.contenedor, activity!!)
    }



}