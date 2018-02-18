package cat.pantsu.nyaapantsu


import cat.pantsu.nyaapantsu.di.component.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class MainApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerMainComponent.builder().create(this)

}
