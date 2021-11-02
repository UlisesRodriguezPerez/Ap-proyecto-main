package Adm_proyectos.approyecto.CONTROLADOR

import android.widget.TextView

class ControladorAdmin {

//    var actual = 0

    fun llenarListasCursos(listaIds: List<TextView>, listaNoms: List<TextView>){
        //base de datos
        //listas de nombres y ids
        //por ahora usar:
        val listaIdsStr = listOf("ma1", "ci1", "ig1", "esp1", "li1", "fi1", "es1", "qm1")
        val listaNomsStr = listOf("Matemática", "Ciencias", "Inglés", "Español", "Literatura", "Física matemática", "Estudios sociales", "Química")

        for (i in listaIdsStr.indices){
            listaIds[i].text = listaIdsStr[i]
            listaNoms[i].text = listaNomsStr[i]
        }
    }

    fun llenarListaDocentes(listaCeds: List<TextView>, listaNoms: List<TextView>){
        //base de datos
        //listas de nombres y ids
        //por ahora usar:
        val listaCedsStr = listOf("11111111", "22222222", "3333333", "44444444", "55555555", "66666666", "77777777", "88888888")
        val listaNomsStr = listOf("Fabrizio Ferreto", "Crystel Montero", "Edgar Melendez", "Keyla Rojas", "Conrad Umaña", "María Madrigal", "Luis Perez", "Daniela Solís")

        for (i in listaCedsStr.indices){
            listaCeds[i].text = listaCedsStr[i]
            listaNoms[i].text = listaNomsStr[i]
        }
    }

    fun llenarListasEstudiantes(listaGrados: List<TextView>, listaNoms: List<TextView>){
        //base de datos
        //listas de nombres y ids
        //por ahora usar:
        val listaGradosStr = listOf("5", "4", "1", "2", "2", "1", "5", "6")
        val listaNomsStr = listOf("Julio César", "María Gonzáles", "Benita Sánchez", "Luis Méndez", "Austin Richards", "Max Fuentes", "Luisa Elizondo", "Bruno Ferreto")

        for (i in listaGradosStr.indices){
            listaGrados[i].text = listaGradosStr[i]
            listaNoms[i].text = listaNomsStr[i]
        }
    }

}

