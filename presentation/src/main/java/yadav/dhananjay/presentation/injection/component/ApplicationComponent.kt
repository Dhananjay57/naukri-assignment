package yadav.dhananjay.presentation.injection.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import yadav.dhananjay.presentation.MyApplication
import yadav.dhananjay.presentation.injection.module.ApplicationModule
import yadav.dhananjay.presentation.injection.module.DataModule
import yadav.dhananjay.presentation.injection.module.PresentationModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class,
        ApplicationModule::class,
        PresentationModule::class,
        DataModule::class))
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: MyApplication)
}