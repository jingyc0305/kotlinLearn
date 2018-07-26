package chenrui.com.kotlindemo.kotlin.api

import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    //获取项目分类
    @GET("project/tree/json")
    fun getProjectTrees():Observable<ProjectTreeBean>
    //获取项目列表
    @GET("project/list/{page}/json")
    fun getProjectList(@Path("page")page: Int,@Query("cid") cid : Int):Observable<ProjectsBean>
}