package Adm_proyectos.approyecto.VISTA.ESTUDIANTE

import Adm_proyectos.approyecto.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main._admin_pricipal.*
import kotlinx.android.synthetic.main.pop_up_noticia.*
import kotlinx.android.synthetic.main.pop_up_noticia.view.*

class popUpNoticia: DialogFragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pop_up_noticia, container, false)
        val noticia = arguments?.getStringArray("noticiaEst")
        val titulo = noticia?.get(0)
        val descripcion = noticia?.get(1)
        val fecha = noticia?.get(2)
        view.tituloNot.text = titulo
        view.contentNot.text = descripcion
        view.fechaNot.text = fecha
        return view

    }
}