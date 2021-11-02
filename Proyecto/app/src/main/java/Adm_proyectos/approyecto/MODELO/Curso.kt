package Adm_proyectos.approyecto.MODELO

class Curso (nombreCurso: String, idCurso: String, chat: Chat) {

    var nombreCurso : String
    var idCurso : String
    var chat : Chat

    init {
        this.nombreCurso = nombreCurso
        this.idCurso = idCurso
        this.chat = chat
    }
}