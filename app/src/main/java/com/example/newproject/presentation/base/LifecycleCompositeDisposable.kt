package com.example.newproject.presentation.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Nimish Nandwana on 08/05/2021.
 * Description - Manages view composite disposables as per lifecycle events
 */

class LifecycleCompositeDisposable : LifecycleObserver {

    private val disposableContainer: MutableMap<Lifecycle.Event, CompositeDisposable> =
        mutableMapOf()

    fun addDisposable(
        disposable: Disposable,
        disposeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) {
        disposableContainer.getOrPut(disposeEvent, { CompositeDisposable() }).add(disposable)
    }
}