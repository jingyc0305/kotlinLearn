package chenrui.com.kotlindemo.kotlin.mpc.presenter

import android.os.Handler
import chenrui.com.kotlindemo.kotlin.app.MainApplication
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.http.exception.ErrorCode
import chenrui.com.kotlindemo.kotlin.http.exception.ExceptionHandle
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeContract
import chenrui.com.kotlindemo.kotlin.mpc.model.HomeModelImpl
import chenrui.com.kotlindemo.kotlin.util.NetWorkUtil

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:获取banner 业务Presenter
 */
class HomePresenterImpl :
    BasePresenter<HomeContract.HomeView>(),
    HomeContract.HomeBannerPresenter {
    private val homeBannerModle: HomeModelImpl by lazy {
        HomeModelImpl()
    }

    /**
     * 获取banners
     */
    override fun getBanners() {
        if (!isViewAttached()) return
        homeBannerModle.getBanners()?.compose(getFragment().bindToLifecycle()).subscribe({ homeBannerBean ->
            view?.apply {
                showBanner(homeBannerBean.data)
            }

        }, { error ->
            view?.apply {
                error.printStackTrace()
            }
        })
    }

    /**
     * 获取articals
     */
    override fun getArticals(page: Int) {
        if (!isViewAttached()) return
        if (!NetWorkUtil.isNetworkAvailable(MainApplication.context))
            view?.showErrorView("网络出错,请检查您的网络.", ErrorCode.NO_NETWORK)
        view?.showLoading()
        homeBannerModle.getArticals(page)?.subscribe({ homeArticalBean ->
            view?.apply {
                Handler().postDelayed({
                    view?.hideLoading()
                    when {
                        homeArticalBean.errorCode < 0 -> showErrorView(
                            "状态码返回错误",
                            ErrorCode.UNKNOWN_ERROR
                        )
                        homeArticalBean.data.datas.size == 0 -> showEmptyView()
                        homeArticalBean.errorCode == 0 -> showArticalList(homeArticalBean)
                    }
                }, 1000)

            }

        }, { error ->
            view?.apply {
                view?.hideLoading()
                showErrorView(ExceptionHandle.handleException(error), ErrorCode.UNKNOWN_ERROR)
            }
        })
    }
}