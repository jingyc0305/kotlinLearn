package chenrui.com.kotlindemo.kotlin.mpc.contract

import chenrui.com.kotlindemo.kotlin.base.IModle
import chenrui.com.kotlindemo.kotlin.base.IPresenter
import chenrui.com.kotlindemo.kotlin.base.IView
import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import io.reactivex.Observable

interface HomeContract {

    interface HomeModle : IModle{
        //获取banner
        fun getBanners():Observable<HomeBannerBean>
        //获取首页文章
        fun getArticals(page:Int):Observable<HomeArticalBean>

    }
    interface HomeView : IView{
        //显示banner
        fun showBanner(list: MutableList<HomeBannerBean.Data>)
        //显示文章列表
        fun showArticalList(homeArticalBean: HomeArticalBean)
        //显示空
        fun showEmptyView()
        //显示加载失败
        fun showErrorView(msg:String,errorcode:Int)
    }
    interface HomeBannerPresenter : IPresenter<HomeView> {
        //获取banner
        fun getBanners()
        fun getArticals(page:Int)
    }
}