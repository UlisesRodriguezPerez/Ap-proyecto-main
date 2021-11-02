package Adm_proyectos.approyecto.VISTA.ADMIN

import Adm_proyectos.approyecto.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.pop_up_lista_cursos.*
import java.util.ArrayList

class popUpCursos: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pop_up_lista_cursos, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ids = ArrayList<TextView>()
        ids.add(idp1)
        ids.add(idp2)
        ids.add(idp3)
        ids.add(idp4)

        val noms = ArrayList<TextView>()
        noms.add(nombrep1)
        noms.add(nombrep2)
        noms.add(nombrep3)
        noms.add(nombrep4)

        val cursos = arguments?.getStringArrayList("cursos_docente")
        llenarTabla(cursos, ids, noms)
    }

    private fun llenarTabla(
        cursos: ArrayList<String>?,
        ids: ArrayList<TextView>,
        noms: ArrayList<TextView>
    ) {
        limpiarTabla(ids, noms)
        val nomsB = ArrayList<String>()
        val idsB = ArrayList<String>()
        for(curso in cursos!!){
            val datos = curso.split("_")
            nomsB.add(datos[0])
            idsB.add(datos[1])
        }
        var indice = 0
        if(cursos.size >= 8){
            for(id in ids){
                id.text = idsB[indice]
                noms[indice].text = nomsB[indice]
                indice++
            }
        }else{
            for(id in idsB){
                ids[indice].text = id
                noms[indice].text = nomsB[indice]
                indice++
            }
        }
    }

    private fun limpiarTabla(ids: ArrayList<TextView>, noms: ArrayList<TextView>) {
        for(ind in 0 until ids.size){
            ids[ind].text = ""
            noms[ind].text = ""
        }
    }
}