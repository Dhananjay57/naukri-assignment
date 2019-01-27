package yadav.dhananjay.presentation.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import yadav.dhananjay.presentation.injection.ViewModelFactory
import yadav.dhananjay.presentation.ui.searchjob.SearchForJobViewModel
import yadav.dhananjay.presentation.ui.searchjob.SearchJobActivity
import yadav.dhananjay.presentation.ui.searchjob.SearchLocationActivity
import yadav.dhananjay.presentation.ui.searchjob.SearchLocationViewModel
import yadav.dhananjay.presentation.ui.splash.SplashActivity
import yadav.dhananjay.presentation.ui.splash.SplashViewModel
import kotlin.reflect.KClass

@Module
abstract class PresentationModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesSplashActivity(): SplashActivity

    @Binds
    @IntoMap
    @ViewModelKey(SearchForJobViewModel::class)
    abstract fun bindSearchJobViewModel(viewModel: SearchForJobViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesSearchJobActivity(): SearchJobActivity

    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel::class)
    abstract fun bindSearchLocationModel(viewModel: SearchLocationViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeSearchLocationActivity(): SearchLocationActivity
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
