package yadav.dhananjay.data.remote.retrofit.factory

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import yadav.dhananjay.data.remote.retrofit.service.GithubService
import java.util.concurrent.TimeUnit

object GithubServiceFactory {

    fun makeRetrofitClient(isDebug: Boolean,
                           chuckInterceptor: ChuckInterceptor): GithubService {
        val okHttpClient = makeOkHttpClient(makeLoggingInterceptor(isDebug), chuckInterceptor)
        return makeRetrofitClient(okHttpClient)
    }

    private fun makeRetrofitClient(okHttpClient: OkHttpClient): GithubService {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://jobs.github.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(GithubService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                                 chuckInterceptor: ChuckInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
    }

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (isDebug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }
}