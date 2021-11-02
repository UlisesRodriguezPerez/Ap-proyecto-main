package Adm_proyectos.approyecto.CONTROLADOR
//
//import Adm_proyectos.approyecto.API.RetroInstance
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class controladorLogIn {
//
//    fun escogerUsuario(
//        activity: AppCompatActivity,
//        correo: String,
//        contrasenna: String,
//        tipo_usuario: TextView
//    ) {
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val call = RetroInstance.api.getLogin(correo)
//            val usuario = call.body()?.get(0)
//            val idUsuario = usuario?.get("ID").toString()
//            val callTipoUsuario = RetroInstance.api.getTipoUsuario(idUsuario)
//            val tipoUsuario = callTipoUsuario.body()?.get(0)?.get("tipousuid").toString()
//
//            activity.runOnUiThread {
//                var datos = ""
//                if (call.isSuccessful) {
//                    val miContrasenna = call.body()?.get(0)?.get("contrasenna")
//                    datos = usuario?.get("nombre").toString() + "_" + tipoUsuario
//                    prueba(contrasenna, miContrasenna.toString(), datos)
//                    // Obtiene el primer elemento de la lista (json) y la contrasenna del Json
//
//
//                } else {
//                    Toast.makeText(activity, "error api", Toast.LENGTH_LONG).show()
//                    tipo_usuario.text = "x"
//                }
//            }
//        }
//    }
//
//    fun prueba(contrasenna: String, miContrasenna:String, datos: String){
//
//    }
//
//
//}