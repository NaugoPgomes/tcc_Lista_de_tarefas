package com.naugo.listadetarefas.service.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import com.naugo.listadetarefas.MainActivity
import com.naugo.listadetarefas.service.constants.DataBaseConstants
import com.naugo.listadetarefas.service.constants.DataBaseConstantsUsuario
import com.naugo.listadetarefas.service.model.GuestModel
import com.naugo.listadetarefas.service.model.GuestModelUsuario

class Repositore private constructor(context: Context) {

    // inicio singleton
    private var mDataBaseHelper: DataBaseHelper = DataBaseHelper(context)

    companion object {
        private lateinit var repositore: Repositore // essa variavel guarda a instancia da classe

        fun getInstance(context: Context): Repositore {
            if (!::repositore.isInitialized) {
                repositore = Repositore(context)
            }

            return repositore
        }
    }
    // fim singleton

    @SuppressLint("Range")
    fun getAll(): List<GuestModel> {
        val list: MutableList<GuestModel> = ArrayList()

        return try {
            val db = mDataBaseHelper.readableDatabase

            val cursor = db.rawQuery("SELECT id, tarefa, data, hora, concluida FROM Guest WHERE concluida = 0",
                    null)

            if(cursor != null && cursor.count > 0)
            {
                while(cursor.moveToNext())
                {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))
                    val produto = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.TAREFA))
                    val data = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.DATA))
                    val hora = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.HORA))
                    val concluida = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONCLUIDA)) == 1)

                    val guest = GuestModel(id, produto, data, hora,concluida)
                    list.add(guest)
                }

            }

            cursor?.close()

            list
        } catch (e: Exception) {
            list
        }

    }

    @SuppressLint("Range")
    fun getConcluidas(): List<GuestModel>
    {
        val list: MutableList<GuestModel> = ArrayList()

        return try {
            val db = mDataBaseHelper.readableDatabase

            val cursor = db.rawQuery("SELECT id, tarefa, data, hora, concluida FROM Guest WHERE concluida = 1",
                    null)

            if(cursor != null && cursor.count > 0)
            {
                while(cursor.moveToNext())
                {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))
                    val produto = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.TAREFA))
                    val data = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.DATA))
                    val hora = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.HORA))
                    val concluida = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONCLUIDA)) == 1)

                    val guest = GuestModel(id, produto, data, hora, concluida)
                    list.add(guest)
                }

            }

            cursor?.close()

            list
        } catch (e: Exception) {
            list
        }
    }

    @SuppressLint("Range")
    fun get(id: Int): GuestModel?
    {
        var guest: GuestModel? = null
        return try {
            val db = mDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.TAREFA,DataBaseConstants.GUEST.COLUMNS.DATA,
                    DataBaseConstants.GUEST.COLUMNS.HORA,DataBaseConstants.GUEST.COLUMNS.CONCLUIDA)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,projection, selection, args,
            null,null,null)

            if(cursor != null && cursor.count > 0)
            {
                cursor.moveToFirst()

                val produto = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.TAREFA))
                val data = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.DATA))
                val hora = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.HORA))
                val concluida = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.CONCLUIDA)) == 1)

                guest = GuestModel(id, produto, data, hora, concluida)
            }

            cursor?.close()

            guest
        } catch (e: Exception) {
            guest
        }
    }


    fun save(guest: GuestModel): Boolean {
        return try {
            val db = mDataBaseHelper.writableDatabase

            val values = ContentValues()
            values.put(DataBaseConstants.GUEST.COLUMNS.TAREFA, guest.tarefa)
            values.put(DataBaseConstants.GUEST.COLUMNS.DATA, guest.data)
            values.put(DataBaseConstants.GUEST.COLUMNS.HORA, guest.hora)
            values.put(DataBaseConstants.GUEST.COLUMNS.CONCLUIDA, guest.concluida)

            db.insert(DataBaseConstants.GUEST.TABLE_NAME, null, values)

            true
        } catch (e: Exception) {
            false
        }
    }

    fun update(guest: GuestModel): Boolean {
        return try {
            val db = mDataBaseHelper.writableDatabase

            val values = ContentValues()
            values.put(DataBaseConstants.GUEST.COLUMNS.TAREFA, guest.tarefa)
            values.put(DataBaseConstants.GUEST.COLUMNS.DATA, guest.data)
            values.put(DataBaseConstants.GUEST.COLUMNS.HORA, guest.hora)
            values.put(DataBaseConstants.GUEST.COLUMNS.CONCLUIDA, guest.concluida)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(guest.id.toString())

            db.update(DataBaseConstants.GUEST.TABLE_NAME, values, selection, args)

            true
        } catch (e: Exception) {
            false
        }
    }

    fun deletar(id: Int): Boolean {
        return try {
            val db = mDataBaseHelper.writableDatabase


            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            db.delete(DataBaseConstants.GUEST.TABLE_NAME, selection, args)

            true
        } catch (e: Exception) {
            false
        }
    }

    fun register(guest: GuestModelUsuario): Boolean
    {
        return try {
            val db =  mDataBaseHelper.writableDatabase

            val values = ContentValues()
            values.put(DataBaseConstantsUsuario.GUEST_USUARIO.COLUMNS.EMAIL, guest.email)
            values.put(DataBaseConstantsUsuario.GUEST_USUARIO.COLUMNS.SENHA, guest.senha)

            db.insert(DataBaseConstantsUsuario.GUEST_USUARIO.TABLE_NAME, null, values)

            true
        } catch (e: java.lang.Exception)
        {
            false
        }

    }


    @SuppressLint("Range")
    fun Login(): List<GuestModelUsuario>
    {

        val list: MutableList<GuestModelUsuario> = ArrayList()

        return try {
            val db = mDataBaseHelper.readableDatabase

            val cursor = db.rawQuery("SELECT id, email, senha FROM Guest_Usuario",
                null)

            if(cursor != null && cursor.count > 0)
            {
                while(cursor.moveToNext())
                {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstantsUsuario.GUEST_USUARIO.COLUMNS.ID))
                    val email = cursor.getString(cursor.getColumnIndex(DataBaseConstantsUsuario.GUEST_USUARIO.COLUMNS.EMAIL))
                    val senha = cursor.getString(cursor.getColumnIndex(DataBaseConstantsUsuario.GUEST_USUARIO.COLUMNS.SENHA))


                    val guest = GuestModelUsuario(id, email, senha)
                    list.add(guest)
                }

            }

            cursor?.close()

            list
        } catch (e: Exception) {
            list
        }

    }



}