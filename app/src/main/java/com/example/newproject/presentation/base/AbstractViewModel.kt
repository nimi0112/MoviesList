package com.example.newproject.presentation.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.UnicastSubject

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description -
 */

abstract class AbstractViewModel<S : MavericksState>(
    initialState: S,
) : MavericksViewModel<S>(initialState), LifecycleObserver {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * This disposable is for subscriptions that are
     * dependent on view's lifecycle or need to be
     * cleared during activity
     **/
    private var viewLifecycleCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun Disposable.bindToLifecycle() = apply {
        compositeDisposable.add(this)
    }

    protected fun Disposable.bindToViewLifecycle() = apply {
        viewLifecycleCompositeDisposable.add(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onViewInactive() {
        viewLifecycleCompositeDisposable.clear()
        compositeDisposable.clear()
    }

    /*****************************************************************************************
     *
     * NavigationAction
     * Responsible for emitting actions for view components
     * Gets triggered from ViewModel by invoking `onNavigationAction` method
     */
    private var navigationActionPublisher: UnicastSubject<NavigationAction> =
        UnicastSubject.create()

    open fun getNavigationActionObservable(): Observable<NavigationAction> =
        navigationActionPublisher
            .doOnDispose {
                navigationActionPublisher = UnicastSubject.create()
            }

    protected fun onNavigationAction(action: NavigationAction) {
        navigationActionPublisher.onNext(action)
    }


}
