package com.example.newproject.presentation.base

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description - basic Navigation Actions
 */

sealed class NavigationAction {


    data class DisplayScreen(val screenName: String, val data: Any? = null) : NavigationAction()

    data class DisplayError(val errorMessage: String, val data: Any? = null) : NavigationAction()

    data class Dialog(val screenName: String, val data: Any? = null) : NavigationAction()

    data class SnackBar(val message: String) : NavigationAction()

    data class DisplayToast(val message: String) : NavigationAction()

    data class CloseScreen(val message: String? = null) : NavigationAction()

    data class DispatchAction(val actionName: String, val data: Any? = null) : NavigationAction()

}