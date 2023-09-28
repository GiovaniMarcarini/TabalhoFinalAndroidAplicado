package br.edu.utfpr.tabalhofinalandroidaplicado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.tabalhofinalandroidaplicado.adapter.LancamentosAdapter
import br.edu.utfpr.tabalhofinalandroidaplicado.database.DataBaseHandler

class LancamentosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lancamentos)

        val banco = DataBaseHandler(this)
        val lancamentos = banco.listar()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewLancamentos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LancamentosAdapter(lancamentos)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

    }
}
