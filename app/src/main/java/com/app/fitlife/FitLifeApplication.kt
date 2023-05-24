package com.app.fitlife

import android.app.Application
import androidx.room.Room
import com.app.fitlife.data.AppDataBase

class FitLifeApplication:Application() {

    //classe responsavel por fazer o banco de dados inicializar somente uma vez, salvando memoria
    private lateinit var dataBase: AppDataBase

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "database-fitlife"
        ).build()


    }

    fun getAppDataBase():AppDataBase{
        return dataBase
    }
}