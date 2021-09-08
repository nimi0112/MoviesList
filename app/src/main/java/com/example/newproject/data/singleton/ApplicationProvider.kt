package com.example.newproject.data.singleton

import android.app.Application

/**
 * Created by Nimish Nandwana on 26/02/2021.
 * Description -
 */

object ApplicationProvider  {
    private lateinit var application: Application

    fun initApplication(app: Application) {
        application = app
    }

    fun getApplication() = application
}