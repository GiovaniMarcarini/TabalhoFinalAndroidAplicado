package br.edu.utfpr.tabalhofinalandroidaplicado.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHandler (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private val DATABASE_NAME = "registroCaixa"
        private  val DATABASE_VERSION = 1
        private val TABLE_NAME = "caixa"
        private val KEY_ID = "_id"
        private val KEY_TIPO = "tipo"
        private val KEY_DETALHE = "detalhe"
        private val KEY_VALOR = "valor"
        private val KEY_DATA = "data"
    }
    fun incluir(tipo : String, detalhe : String, valor : Float, data : String ){
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put(KEY_TIPO, tipo)
        registro.put(KEY_DETALHE, detalhe)
        registro.put(KEY_VALOR, valor)
        registro.put(KEY_DATA, data)

        db.insert(TABLE_NAME, null, registro)
    }

    fun listar(): List<ContentValues> {
        val registros = mutableListOf<ContentValues>()

        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        val colIndexTipo = cursor.getColumnIndex(KEY_TIPO)
        val colIndexDetalhe = cursor.getColumnIndex(KEY_DETALHE)
        val colIndexValor = cursor.getColumnIndex(KEY_VALOR)
        val colIndexData = cursor.getColumnIndex(KEY_DATA)

        if (cursor.moveToFirst()) {
            do {
                val registro = ContentValues()
                registro.put(KEY_TIPO, cursor.getString(colIndexTipo))
                registro.put(KEY_DETALHE, cursor.getString(colIndexDetalhe))
                registro.put(KEY_VALOR, cursor.getFloat(colIndexValor))
                registro.put(KEY_DATA, cursor.getString(colIndexData))

                registros.add(registro)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return registros
    }

    fun getSaldo(): Float {
        val db = this.readableDatabase

        val cursorCredit = db.rawQuery("SELECT SUM(valor) FROM $TABLE_NAME WHERE tipo='Crédito'", null)
        cursorCredit.moveToFirst()
        val totalCredit = cursorCredit.getFloat(0)
        cursorCredit.close()

        val cursorDebit = db.rawQuery("SELECT SUM(valor) FROM $TABLE_NAME WHERE tipo='Débito'", null)
        cursorDebit.moveToFirst()
        val totalDebit = cursorDebit.getFloat(0)
        cursorDebit.close()

        return totalCredit - totalDebit
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} ( ${KEY_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${KEY_TIPO} TEXT, ${KEY_DETALHE} TEXT, ${KEY_VALOR} FLOAT, ${KEY_DATA} TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}