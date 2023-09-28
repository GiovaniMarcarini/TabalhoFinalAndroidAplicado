package br.edu.utfpr.tabalhofinalandroidaplicado.adapter

import android.content.ContentValues
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.tabalhofinalandroidaplicado.R

class LancamentosAdapter(private val lancamentos: List<ContentValues>) :
    RecyclerView.Adapter<LancamentosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLancamento: TextView = view.findViewById(R.id.tvLancamento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lancamento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = lancamentos[position]
        val tipo = if (registro.getAsString("tipo") == "Crédito") "C" else "D"
        val data = registro.getAsString("data")
        val detalhe = registro.getAsString("detalhe")
        val valor = registro.getAsFloat("valor")

        holder.tvLancamento.text = "$tipo $data - $detalhe - $valor"
    }

    override fun getItemCount() = lancamentos.size
}
