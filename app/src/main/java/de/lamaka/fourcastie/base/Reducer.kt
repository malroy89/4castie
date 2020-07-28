package de.lamaka.fourcastie.base

interface Reducer<VS : ViewState, AR : ActionResult> {
    fun reduce(currentState: VS, actionResult: AR): VS
}