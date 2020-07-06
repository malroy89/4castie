package de.lamaka.fourcastie.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<A : Action, VS : ViewState, AR : ActionResult> constructor(
    initAction: A
) : ViewModel() {

    protected abstract var currentState: VS
    private val nextAction = MutableLiveData(initAction)

    val viewState: LiveData<VS> =
        Transformations.distinctUntilChanged(
            Transformations.map(Transformations.switchMap(nextAction) { perform(it) }) {
                currentState = reduce(it)
                currentState
            }
        )

    fun handle(action: A) {
        nextAction.value = action
    }

    protected abstract fun perform(action: A): LiveData<AR>
    protected abstract fun reduce(result: AR): VS
}