package Adm_proyectos.approyecto.MODELO

import java.time.LocalTime

class MensajeM(mensaje: String, remitente: Usuario) {
    var mensaje : String
    var remitente : Usuario

    init {
        this.mensaje = mensaje
        this.remitente = remitente
    }
}