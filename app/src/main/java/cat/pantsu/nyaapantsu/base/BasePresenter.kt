package cat.pantsu.nyaapantsu.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<T : BaseView> {
    var compositeObservable = CompositeDisposable()
    var view: T? = null
    val isSubscribed: Boolean
        get() = view != null

    fun subscribe(view: T) {
        compositeObservable.dispose()
        compositeObservable = CompositeDisposable()
        this.view = view
    }

    fun unsubscribe() {
        view = null
        compositeObservable.dispose()
    }
}