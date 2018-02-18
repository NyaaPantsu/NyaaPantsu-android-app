package cat.pantsu.nyaapantsu.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


class MainAppSchedulers : Schedulers {
    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun backgroundThread(): Scheduler {
        return io.reactivex.schedulers.Schedulers.io()
    }
}