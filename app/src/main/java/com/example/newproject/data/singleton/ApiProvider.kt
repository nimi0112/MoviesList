package com.example.newproject.data.singleton

import androidx.databinding.ktx.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.newproject.data.api.FeedApi
import com.example.newproject.data.singleton.DataConstants.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Nimish Nandwana on 07/09/2021.
 * Description - Provides the Api related data
 */

object ApiProvider {

    private fun getOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(networkInterceptor)
            .addInterceptor(getChucker())
        return builder.build()
    }

    private val networkInterceptor: Interceptor
        get() {
            //adding `key` in every requests header
            return Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                val header = request.headers.newBuilder().build()
                request = builder.headers(header).build()
                chain.proceed(request)
            }
        }
    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            //Enable logging interceptor in case of debug variants
            loggingInterceptor.level =  HttpLoggingInterceptor.Level.BODY

            return loggingInterceptor
        }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttp())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun getChucker() = ChuckerInterceptor.Builder(ApplicationProvider.getApplication()).build()


    fun getFeedApi(): FeedApi = getRetrofit().create(FeedApi::class.java)
}