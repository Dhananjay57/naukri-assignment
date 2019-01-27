package yadav.dhananjay.presentation.injection.module

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import yadav.dhananjay.data.factory.GithubRepositoryFactory
import yadav.dhananjay.data.local.sharedpref.IPreferenceStorage
import yadav.dhananjay.data.local.sharedpref.SharedIPreferenceStorage
import yadav.dhananjay.data.remote.retrofit.factory.GithubServiceFactory
import yadav.dhananjay.data.remote.retrofit.service.GithubService
import yadav.dhananjay.domain.repository.factory.IGithubRepositoryFactory
import yadav.dhananjay.presentation.BuildConfig
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Module
    companion object {

        // JvsStatic provides static method
        @Provides
        @Singleton
        @JvmStatic
        fun provideGithubService(chuckInterceptor: ChuckInterceptor): GithubService {
            return GithubServiceFactory.makeRetrofitClient(BuildConfig.DEBUG, chuckInterceptor)
        }

        @Provides
        @JvmStatic
        fun provideChuckInterceptor(context: Context): ChuckInterceptor = ChuckInterceptor(context)

        @Provides
        @Singleton
        @JvmStatic
        fun providesGson(): Gson = Gson()
    }

    @Binds
    abstract fun bindPreferenceStorage(sharedPreferenceStorage: SharedIPreferenceStorage): IPreferenceStorage

    @Binds
    abstract fun bindRepositoryFactory(repositoryFactory: GithubRepositoryFactory): IGithubRepositoryFactory
}