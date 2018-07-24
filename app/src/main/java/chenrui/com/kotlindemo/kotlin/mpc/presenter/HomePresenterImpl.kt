package chenrui.com.kotlindemo.kotlin.mpc.presenter

import android.os.Handler
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeContract

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:获取banner 业务Presenter
 */
class HomePresenterImpl :
    BasePresenter<HomeContract.HomeView, HomeContract.HomeModle>(),
    HomeContract.HomeBannerPresenter {
    /**
     * 获取banners
     */
    override fun getBanners() {
        if (!isViewAttached()) return
        model?.getBanners()?.subscribe({ homeBannerBean ->
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
        view?.showLoading()
        model?.getArticals(page)?.subscribe({ homeArticalBean ->
            view?.apply {
                Handler().postDelayed({
                    view?.hideLoading()
                    when {
                        homeArticalBean.errorCode<0 -> showErrorView(homeArticalBean.errorMsg)
                        homeArticalBean.data.datas.size == 0 -> showEmptyView()
                        else -> showArticalList(homeArticalBean.data.datas)
                    }
                },1000)

            }

        }, { error ->
            view?.apply {
                view?.hideLoading()
                showErrorView("加载失败,点我重试")
                error.printStackTrace()
            }
        })
    }
}