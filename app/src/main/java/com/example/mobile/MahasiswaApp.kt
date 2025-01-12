package com.example.mobile

import android.app.Application
import com.example.mobile.di.MahasiswaContainer

class MahasiswaApp : Application(){
    lateinit var containerApp: MahasiswaContainer

    override fun onCreate() {
        super.onCreate()
        containerApp = MahasiswaContainer(this)
    }
}