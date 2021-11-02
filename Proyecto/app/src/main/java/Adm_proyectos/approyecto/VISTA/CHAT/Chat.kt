package Adm_proyectos.approyecto.VISTA.CHAT

import Adm_proyectos.approyecto.API.RetroInstance
import Adm_proyectos.approyecto.CONTROLADOR.ControladorComponentesVista
import Adm_proyectos.approyecto.MODELO.MensajeM
import Adm_proyectos.approyecto.MODELO.Usuario
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Adm_proyectos.approyecto.R
import Adm_proyectos.approyecto.VISTA.DOCENTE.DocenteDetallesCurso
import Adm_proyectos.approyecto.VISTA.INTERFACES.DatosDocente
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main._chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Chat : Fragment() {

    private lateinit var idCurso: String
    private lateinit var grado: String
    private lateinit var correo: String
    private lateinit var nombreP: String
    private lateinit var apellidoP: String
    private lateinit var comunicador: DatosDocente
    private val controller = ControladorComponentesVista()
    private var mensajesEnviados = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout._chat, container, false)
        comunicador = activity as DatosDocente
        val datos = arguments?.getStringArray("datosCursoMed")
        idCurso = datos?.get(0).toString()
        grado = datos?.get(1).toString()
        correo = datos?.get(2).toString()
        nombreP = datos?.get(3).toString()
        apellidoP = datos?.get(4).toString()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idCursoDocentes.text = idCurso
        val recibidos = listOf<EditText>(recibido1, recibido2, recibido3, recbido4, recibido5, recibido6)
        val enviados = listOf<EditText>(enviado1, enviado2, enviado3, enviado4, enviado5, enviado6)

        for (edit in recibidos.indices){
            recibidos[edit].visibility = View.INVISIBLE
            enviados[edit].visibility = View.INVISIBLE
        }

        mensajesChat(idCurso, grado, recibidos, enviados, false)

        anteriores.setOnClickListener {
            mensajesChat(idCurso, grado, recibidos, enviados, true)
        }

        enviarMensaje.setOnClickListener {
            val mensaje = mensajeEscrito.text.toString()
            insertarMensajeChat(idCurso, grado, correo, mensaje, recibidos, enviados)
        }
        atrasChat.setOnClickListener {
            cursoInfo(idCurso, grado)
        }
    }

    private fun cargarMensajes(mensajes: ArrayList<MensajeM>, recibidos: List<EditText>, enviados: List<EditText>, viejos: Boolean) {
        if (!viejos){
            if (mensajes.size >= 5) {
                for (ind in recibidos.indices-1) {
                    val nombre = mensajes[ind].remitente.nombre
                    val apellido = mensajes[ind].remitente.apellidos
                    if (("$nombre $apellido") == ("$nombreP $apellidoP")) {
                        enviados[ind].visibility = View.VISIBLE
                        enviados[ind].setText(mensajes[ind].mensaje)
                    } else {
                        recibidos[ind].visibility = View.VISIBLE
                        recibidos[ind].setText(mensajes[ind].mensaje)
                    }
                }
            } else {
                for (ind in mensajes.indices) {
                    val nombre = mensajes[ind].remitente.nombre
                    val apellido = mensajes[ind].remitente.apellidos
                    if (("$nombre $apellido") == ("$nombreP $apellidoP")) {
                        enviados[ind].visibility = View.VISIBLE
                        enviados[ind].setText(mensajes[ind].mensaje)
                    } else {
                        recibidos[ind].visibility = View.VISIBLE
                        recibidos[ind].setText(mensajes[ind].mensaje)
                    }
                }
            }
        }else{ // mejor no usar si queda tiempo revisar
            if (mensajes.size>5){
                var indAct = obtenerIndiceActual(mensajes, recibidos, enviados)
                var ind = 0
                limpiarLista(recibidos, enviados)
                val restantes = (mensajes.size - (indAct+1))
                if(restantes >=8){
                    for(id in recibidos.indices){
                        val nombre = mensajes[ind].remitente.nombre
                        val apellido = mensajes[ind].remitente.apellidos
                        if (("$nombre $apellido") == ("$nombreP $apellidoP")) {
                            enviados[ind].visibility = View.VISIBLE
                            enviados[ind].setText(mensajes[ind].mensaje)
                        } else {
                            recibidos[ind].visibility = View.VISIBLE
                            recibidos[ind].setText(mensajes[ind].mensaje)
                        }
                        ind++
                        indAct++
                    }
                }
                else{
                    indAct+=1
                    for(i in 0 until restantes){
                        val nombre = mensajes[ind].remitente.nombre
                        val apellido = mensajes[ind].remitente.apellidos
                        if (("$nombre $apellido") == ("$nombreP $apellidoP")) {
                            enviados[ind].visibility = View.VISIBLE
                            enviados[ind].setText(mensajes[ind].mensaje)
                        } else {
                            recibidos[ind].visibility = View.VISIBLE
                            recibidos[ind].setText(mensajes[ind].mensaje)
                        }
                        ind++
                        indAct++
                    }
                }
            }
        }
    }

    private fun limpiarLista(recibidos: List<EditText>, enviados: List<EditText>) {
        var indice = 0
        for(elemento in recibidos.indices){
            recibidos[elemento].visibility = View.INVISIBLE
            enviados[elemento].visibility = View.INVISIBLE
            indice ++
        }
    }

    private fun insertarMensajeChat(codigoCurso: String, grado: String, correo: String, mensaje:String, recibidos: List<EditText>, enviados: List<EditText>){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.insertarMensaje(codigoCurso, grado, correo, mensaje)
            activity!!.runOnUiThread {
                if (call.isSuccessful) {
                    val resultados = call.body()
                    if (resultados != null) {
                        val resultado = resultados[0].get("insertarMensaje")
                        mensajeInsertado(recibidos, enviados)
                    }
                    else{
                        controller.notificacion("Error al insertar el mensaje", activity!!)                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)                }
            }
        }
    }

    private fun mensajeInsertado(recibidos: List<EditText>, enviados: List<EditText>) {
        val mensaje = mensajeEscrito.text.toString()
        mensajesEnviados.add(mensaje)
        moverTextos(recibidos, enviados)
        enviado1.visibility = View.VISIBLE
        enviado1.setText(mensaje)
        recibido1.visibility = View.INVISIBLE
        mensajeEscrito.text.clear()

    }

    private fun moverTextos(recibidos: List<EditText>, enviados: List<EditText>) {
        val mensajesRecibidos = ArrayList<String>()
        val mensajesEnviados = ArrayList<String>()
        for(ind in recibidos.indices){
            mensajesRecibidos.add(recibidos[ind].text.toString())
            mensajesEnviados.add(enviados[ind].text.toString())
        }

        for (ind in enviados.indices){
            if(ind+1 < 6) {
                recibidos[ind + 1].setText(mensajesRecibidos[ind])
                enviados[ind + 1].setText(mensajesEnviados[ind])
                if (recibidos[ind + 1].text.toString() != "") {
                    recibidos[ind + 1].visibility = View.VISIBLE
                    enviados[ind + 1].visibility = View.INVISIBLE
                }
                if (enviados[ind + 1].text.toString() != "") {
                    enviados[ind + 1].visibility = View.VISIBLE
                    recibidos[ind + 1].visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun mensajesChat(codigo: String, grado: String, recibidos: List<EditText>, enviados: List<EditText>, viejos:Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetroInstance.api.getChat(codigo, grado)
            activity!!.runOnUiThread{
                if (call.isSuccessful) {
                    val chats = call.body()
                    if (chats != null) {
                        val mensajes = ArrayList<MensajeM>()
                        for (chat in chats) {
                            val nombre = chat.get("nombre").toString().replace("\"", "")
                            val apellido = chat.get("apellido").toString().replace("\"", "")
                            val mensaje = chat.get("texto").toString().replace("\"", "")
                            val usuario = Usuario(nombre, apellido)
                            val mensajeM = MensajeM(mensaje, usuario)
                            mensajes.add(mensajeM)
                        }
                        cargarMensajes(mensajes, recibidos, enviados, viejos)
                    }
                } else {
                    print("Error! Conexion con el Adm_proyectos.approyecto.API Fallida")
                }
            }
        }
    }

    private fun obtenerIndiceActual(listaMensajesA: ArrayList<MensajeM>, recibidos: List<TextView>, enviados: List<TextView>): Int {
        var nuevoInd = 0
        for(id in 0 until listaMensajesA.size){
            if(recibidos[5].text.equals(listaMensajesA[id].mensaje) || enviados[5].text.equals(listaMensajesA[id].mensaje)){
                nuevoInd = id
                break
            }
        }
        return nuevoInd
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
                            comunicador.enviarDatosCurso(id, nombre, grado, horario, detalles, correo, nombreP, apellidoP)

                        }
                    }
                } else {
                    controller.notificacion("Error al conectar con la base de datos", activity!!)
                }
            }
        }
    }

}