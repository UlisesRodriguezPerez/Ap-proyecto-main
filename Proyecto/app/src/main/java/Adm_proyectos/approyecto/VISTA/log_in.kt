package Adm_proyectos.approyecto.VISTA

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.ADMIN.ADMIN.adminPricipal
import Adm_proyectos.approyecto.VISTA.DOCENTE.DocentePrincipal
import Adm_proyectos.approyecto.VISTA.ESTUDIANTE.estudiantesPrincipal
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main._log_in.*
import kotlinx.android.synthetic.main._log_in.view.*
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class log_in : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._log_in)
        show.setOnClickListener() {
            notshow.visibility = View.VISIBLE
            show.visibility = View.INVISIBLE
            contrasenna.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        notshow.setOnClickListener() {
            show.show.visibility = View.VISIBLE
            notshow.visibility = View.INVISIBLE
            contrasenna.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }

        iniciarSesion.setOnClickListener() {
            comprobarInicioSesion(correoInicioSesion.text.toString().lowercase().replace(" ", ""), contrasenna.text.toString())
        }

        login.setOnClickListener{view ->
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

    }

    private fun comprobarInicioSesion(correo: String, contrasenna: String){
        CoroutineScope(Dispatchers.IO).launch {

            val call = RetroInstance.api.getLogin(correo)
            val usuario = call.body()?.get(0)
            val nombre = usuario?.get("nombre").toString().replace("\"", "")
            val idUsuario = usuario?.get("ID").toString()
            val apellido = usuario?.get("apellido").toString()
            val cedula = usuario?.get("cedula").toString().replace("\"", "")
            val callTipoUsuario = RetroInstance.api.getTipoUsuario(idUsuario)
            val tipoUsuario = callTipoUsuario.body()?.get(0)?.get("tipousuario").toString().replace("\"", "")
            val datos = ArrayList<String>()

            datos.add(nombre)
            datos.add(tipoUsuario)
            datos.add(apellido)
            datos.add(cedula)

            runOnUiThread {
                if (call.isSuccessful) {
                    val miContrasenna = call.body()?.get(0)?.get("contrasenna").toString().replace("\"", "")
                    if(contrasenna.equals(miContrasenna)){
                        iniciarSesion(datos)
                    } else{
                        errorSesion()
                    }
                } else {
                    errorApi()
                }
            }
        }
    }

    private fun iniciarSesion(datos: ArrayList<String>){
        val admin = adminPricipal()
        val docentePrincipal = DocentePrincipal()
        val tipo = datos[1]
        val nombre = datos[0]
        val apellido = datos[2]
        val cedula = datos[3]

        if (tipo.equals("administrador")){
            Intent(this, admin::class.java).also{
                it.putExtra("nombre", nombre)
                startActivity(it)
            }
        }

        else if (tipo.equals("profesor")){
            Intent(this, docentePrincipal::class.java).also{
                it.putExtra("nombre", nombre)
                it.putExtra("correo", correoInicioSesion.text.toString().lowercase().replace(" ", ""))
                it.putExtra("apellido", apellido)
                startActivity(it)
            }
        }

        else if(tipo.equals("estudiante")){
            Intent(this, estudiantesPrincipal::class.java).also{
                it.putExtra("nombre", nombre)
                it.putExtra("correo", correoInicioSesion.text.toString().lowercase().replace(" ", ""))
                it.putExtra("apellido", apellido)
                it.putExtra("cedula", cedula)
                startActivity(it)
            }
        }
        else{
            imprimir(tipo)
        }
    }

    private fun errorSesion() {
        Toast.makeText(this, "Usuario o contraseña incorrectas, intente de nuevo", Toast.LENGTH_LONG).show()
    }

    private fun errorApi() {
        Toast.makeText(this, "Error de conexión, intente de nuevo", Toast.LENGTH_LONG).show()
    }

    private fun imprimir(miContrasenna: String?) {
        Toast.makeText(this, miContrasenna, Toast.LENGTH_LONG).show()
    }


}