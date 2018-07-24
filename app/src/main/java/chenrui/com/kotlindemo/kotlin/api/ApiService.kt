package chenrui.com.kotlindemo.kotlin.api

import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:服务接口
 */
interface ApiService {
    //首页文章
    @GET("article/list/{page}/json")
    fun getHomeArticals(@Path("page") page: Int ):Observable<HomeArticalBean>

    //首页广告轮播
    @GET("banner/json")
    fun getHomeBanners():Observable<HomeBannerBean>
}