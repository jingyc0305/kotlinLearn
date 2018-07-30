package chenrui.com.kotlindemo.kotlin.mpc.model

import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import chenrui.com.kotlindemo.kotlin.http.RetrofitManager
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeContract
import chenrui.com.kotlindemo.kotlin.rxhelper.RxSchedulersHelper
import io.reactivex.Observable

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:获取banner 数据层model
 */
class HomeModelImpl:HomeContract.HomeModle{
    override fun getArticals(page: Int): Observable<HomeArticalBean> {
        return RetrofitManager.service.getHomeArticals(page).compose(RxSchedulersHelper.ioToMain())
    }

    override fun getBanners():Observable<HomeBannerBean> {
        return RetrofitManager.service.getHomeBanners().compose(RxSchedulersHelper.ioToMain())
    }
}