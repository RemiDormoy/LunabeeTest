package com.rdo.octo.lunabee

interface MainView {
    fun displayError()
    fun displayUsers(list: List<User>)
}