package Adm_proyectos.approyecto.VISTA.ADMIN.GestionDocentes

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.admin_gd_crear_docente.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AdminGdCrearDocente : Fragment() {

    val controller = ControladorComponentesVista()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.admin_gd_crear_docente, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agregarProfesorNuevoCrear.setOnClickListener(){
            agregar()
        }

    }

    private fun agregar() {
        if (cedulaCrearDocente.text.isNotEmpty() && nombreCrearDocente.text.isNotEmpty() &&
            primerApellidoCrearDocente.text.isNotEmpty()&& correoCrearDocente.text.isNotEmpty() && contrasennaCrearDocente.text.isNotEmpty()) {

                insertarDocente(
                    cedulaCrearDocente.text.toString().replace(" ", ""),
                    nombreCrearDocente.text.toString().replace(" ", ""),
                    primerApellidoCrearDocente.text.toString().replace(" ", ""),
                    segundoApellidoCrearDocente.text.toString().replace(" ", ""),
                    correoCrearDocente.text.toString().replace(" ", "").lowercase(),
                    contrasennaCrearDocente.text.toString()
                )

        } else {
            controller.notificacion("Existen campos sin llenar", activity!!)
        }
    }

    fun insertarDocente(cedula: String, nombre: String, apellido1: String, apellido2: String, correo: String, contra: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<ArrayList<JsonObject>> = if(apellido2.isNotEmpty()) {
                RetroInstance.api.insertarProfesor(
                    cedula, nombre, correo, contra,
                    "$apellido1 $apellido2"
                )
            } else{
                RetroInstance.api.insertarProfesor(
                    cedula, nombre, correo, contra,
                    apellido1
                )
            }

            activity!!.runOnUiThread {
                print(call)
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        print(resultados)
                        val resultado = resultados[0].get("insertardocente")
                        if (resultado.asInt == 0) {
                            controller.notificacion("Docente insertado con éxito!!", activity!!)
                            insertadoExitoso(true)
                        }else{
                            controller.notificacion("No se pudo insertar el docente, intente de nuevo", activity!!)
                        }
                    }
                    else{
                        controller.notificacion("No se pudo insertar el docente, intente de nuevo", activity!!)
                    }
                } else {
                    controller.notificacion("Error con la base de datos, intente de nuevo", activity!!)
                }
            }
        }
    }

    private fun insertadoExitoso(insertar:Boolean) {
        cedulaCrearDocente.text.clear()
        nombreCrearDocente.text.clear()
        primerApellidoCrearDocente.text.clear()
        segundoApellidoCrearDocente.text.clear()
        correoCrearDocente.text.clear()
        contrasennaCrearDocente.text.clear()
        if (insertar) {
            val listaCursos = AdminGdListaDocentes()
            controller.cambiarFragment(listaCursos, R.id.contenedor, activity!!)
        }
    }



}