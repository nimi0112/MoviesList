package com.example.newproject.presentation.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksView
import com.example.newproject.data.singleton.DataConstants.DOUBLE_BACK_PRESS_WAIT_TIME
import com.example.newproject.presentation.extensions.hideSoftKeyboard
import com.example.newproject.presentation.extensions.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description - AbstractFragment, All new Fragments in the app should extends this
 */
abstract class AbstractFragment<S : MavericksState, VM : AbstractViewModel<S>> : Fragment(), MavericksView {

    protected abstract val binding: ViewDataBinding

    /**
     * Returns the class for the view model associated to this fragment.
     */
    protected abstract val viewModel: VM


    protected open val rootView: View
        get() = view ?: throw IllegalStateException("View not inflated yet")

    /*****************************************************************************************
     *
     * For double back press to exit
     */
    private var isBackPressedOnceToExit: Boolean = false


    /*****************************************************************************************
     *
     * Tells that if there fragment which is displaying on the screen is shown for the forst time
     *
     * Useful if you wanna do a one time thing such as api call
     */
    private var isVeryFirstRun: Boolean = false

    /*****************************************************************************************
     *
     * Composite Disposable
     */
    @JvmField
    protected val compositeDisposable = LifecycleCompositeDisposable()

    /**
     * Adds this [Disposable] to [compositeDisposable] to insure that it will be disposed when
     * the view is destroyed avoiding any memory leaks.
     */
    protected fun Disposable.bindToLifecycle(disposeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY) =
        apply {
            compositeDisposable.addDisposable(this, disposeEvent)
        }

    /**
     * Called when a fragment is first attached to its context.
     * [.onCreate] will be called after this.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        isVeryFirstRun = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = binding.root
        setupView(isVeryFirstRun)
        if (isVeryFirstRun) {
            isVeryFirstRun = false
        }
        return view
    }

    override fun onResume() {
        subscribeToNavigationActions()
        super.onResume()
    }

    fun onBackPressed() {
        val fragmentCount = fragmentManager?.backStackEntryCount ?: 0

        // Given that we are using single activity architecture,
        // app should ideally finish when we are navigating back from last fragment
        if (fragmentCount > 0) {
            navigateBack()
        } else if (!isBackPressedOnceToExit) {
            isBackPressedOnceToExit = true
            showToast("Double press to exit")
            Handler().postDelayed({ isBackPressedOnceToExit = false }, DOUBLE_BACK_PRESS_WAIT_TIME)
        } else {
            requireActivity().finish()
        }

    }


    /*****************************************************************************************
     *
     * Navigation Actions
     */
    private var navigationActionDisposable: Disposable? = null

    private fun subscribeToNavigationActions() {
        if (navigationActionDisposable?.isDisposed != false) {
            navigationActionDisposable = viewModel.getNavigationActionObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNavigationAction, Timber::e)
                .bindToLifecycle(Lifecycle.Event.ON_PAUSE)
        }
    }

    /**
     * Called when the layout for this fragment has been inflated to populate it with
     * the current fragment data
     */

    abstract fun setupView(isVeryFirstRun: Boolean)

    /**
     * This method is invoked when a new navigation action is received from the view model.
     * Don't forget to invoke super method in order to handle below actions by default
     */
    protected open fun onNavigationAction(action: NavigationAction) {
        when (action) {
            is NavigationAction.DisplayToast -> showToast(action.message)

            is NavigationAction.CloseScreen -> {
                action.message?.let { showToast(it) }
                navigateBack()
            }
        }
    }

    protected fun navigateBack() {
        hideSoftKeyboard()
        findNavController().navigateUp()
    }

    protected fun navigateTo(directions: NavDirections) {
        hideSoftKeyboard()
        findNavController().navigate(directions)
    }


}