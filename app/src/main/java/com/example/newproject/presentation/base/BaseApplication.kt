package com.example.newproject.presentation.base

import android.app.Application
import com.airbnb.mvrx.Mavericks
import com.example.newproject.data.singleton.ApplicationProvider

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description -
 */

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationProvider.initApplication(this)
        Mavericks.initialize(this)
    }
}