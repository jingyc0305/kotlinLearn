package chenrui.com.kotlindemo.kotlin.http

import chenrui.com.kotlindemo.kotlin.api.ApiService
import chenrui.com.kotlindemo.kotlin.app.MyApplication
import chenrui.com.kotlindemo.kotlin.app.MyApplication.Companion.context
import chenrui.com.kotlindemo.kotlin.app.UrlContant
import chenrui.com.kotlindemo.kotlin.util.NetWorkUtil
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit



/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:网络请求管理类 Retrifit + okhttp
 */
object RetrofitManager {
    private var retrofit:Retrofit? = null
    private var okhttpclient : OkHttpClient? = null

    val service : ApiService by lazy { getAppRetrofit()!!.create(ApiService::class.java) }
    private fun getAppRetrofit():Retrofit?{
        if(retrofit == null){
            synchronized(RetrofitManager::class.java){
                if(retrofit == null){
                    //添加打印拦截
                    var httpLoggingInterceptor = HttpLoggingInterceptor()
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    val cookie = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
                    //设置缓存
                    //设置 请求的缓存的大小跟位置
                    val cacheFile = File(context.cacheDir, "cache")
                    val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小
                    okhttpclient = OkHttpClient.Builder()
//                        .addInterceptor(addQueryParameterInterceptor())  //添加请求公参
//                        .addInterceptor(addHeaderInterceptor()) // 添加请求头
                        .addInterceptor(addCacheInterceptor())
                        .addInterceptor(httpLoggingInterceptor)
                        .cache(cache)
                        .connectTimeout(60L, TimeUnit.SECONDS)
                        .readTimeout(60L, TimeUnit.SECONDS)
                        .writeTimeout(60L, TimeUnit.SECONDS)
                        .cookieJar(cookie)
                        .build()

                    retrofit = Retrofit
                        .Builder()
                        .baseUrl(UrlContant.BASE_URL)
                        .client(okhttpclient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }

            }

        }
        return retrofit
    }
    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            var modifiedUrl:HttpUrl? = null
            var formBody:FormBody? = null
            if(originalRequest.method() == "GET"){
                    modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("phoneSystem", "")
                    .addQueryParameter("phoneModel", "")
                    .build()
            }else if(originalRequest.method() == "POST"){
                if (originalRequest.body() is FormBody) {
                    val bodyBuilder = FormBody.Builder()
                    formBody = originalRequest.body() as FormBody
                    for (i in 0 until formBody.size()) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
                    }
                    formBody = bodyBuilder
                        .addEncoded("phoneSystem", "xxx")
                        .addEncoded("phoneModel", "xxx")
                        .build()
                }
            }
            when (originalRequest.method()){
                "GET" -> {
                    chain.proceed(originalRequest.newBuilder().url(modifiedUrl).build())
                }
                "POST" -> {
                    chain.proceed(originalRequest.newBuilder().post(formBody).build())
                }
                else -> {
                    null
                }
            }
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                // Provide your custom header here
                .header("token", "")
                .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            //当前无网络时候 使用本地缓存
            if (!NetWorkUtil.isNetworkAvailable(MyApplication.context)) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if (NetWorkUtil.isNetworkAvailable(MyApplication.context)) {
                val maxAge = 8
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("nyn")
                    .build()
            }
            response
        }
    }

}


