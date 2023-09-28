package br.edu.utfpr.tabalhofinalandroidaplicado

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import br.edu.utfpr.tabalhofinalandroidaplicado.database.DataBaseHandler
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var banco : DataBaseHandler

    private lateinit var spTipo : Spinner
    private lateinit var spDetalhe : Spinner
    private lateinit var etValor : EditText
    private lateinit var etData : EditText
    private lateinit var btLancar : Button
    private lateinit var btVerLancamento : Button
    private lateinit var btSaldo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spTipo = findViewById(R.id.spTipo)
        spDetalhe = findViewById(R.id.spDetalhe)
        etValor = findViewById(R.id.etValor)
        etData = findViewById(R.id.etData)
        btLancar = findViewById(R.id.btLancar)
        btVerLancamento = findViewById(R.id.btVerLancamento)
        btSaldo = findViewById(R.id.btSaldo)

        banco = DataBaseHandler(this)

        val listTipo = mutableListOf("Crédito", "Débito")
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTipo)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = adapterTipo

        spTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val listDetalhe: List<String> = when (listTipo[position]) {
                    "Crédito" -> listOf("Salário", "Extras")
                    "Débito" -> listOf("Alimentação", "Transporte", "Saúde", "Moradia")
                    else -> emptyList()
                }

                val adapterDetalhe = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    listDetalhe
                )
                adapterDetalhe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDetalhe.adapter = adapterDetalhe
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
        etData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Cria um novo DatePickerDialog com a data atual como data inicial
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "${selectedDay.padZero()}/${(selectedMonth + 1).padZero()}/$selectedYear"
                    etData.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun Int.padZero() = if (this < 10) "0$this" else this.toString()

    fun lancarRegistro(view: View) {
        var tipo = spTipo.selectedItem.toString()
        var detalhe = spDetalhe.selectedItem.toString()
        var valor = etValor.text.toString()
        var data = etData.text.toString()

        if (valor.isEmpty()){
            etValor.setError("Por favor preencha o valor.")
            return
        }
        if (data.isEmpty()){
            etData.setError("Por favot preencha a data.")
            return
        }
        var valorFloat = valor.toFloatOrNull()

        if (valorFloat != null) {
            banco.incluir(tipo, detalhe, valorFloat, data)
             etData.setText("")
            etValor.setText("")
        }
    }
    fun onClickVerLancamentos(view: View) {
        val intent = Intent(this, LancamentosActivity::class.java)
        startActivity(intent)
    }
    fun onClickSaldo(view: View) {
        showSaldoDialog()
    }
    fun showSaldoDialog() {
        val saldo = banco.getSaldo()
        val saldoStr = String.format("%.2f", saldo)

        AlertDialog.Builder(this)
            .setTitle("Saldo")
            .setMessage("Seu saldo é: R$$saldoStr")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}