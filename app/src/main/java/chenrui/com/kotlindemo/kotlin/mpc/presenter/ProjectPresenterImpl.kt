package chenrui.com.kotlindemo.kotlin.mpc.presenter

import android.os.Handler
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.http.exception.ErrorCode
import chenrui.com.kotlindemo.kotlin.http.exception.ExceptionHandle
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.mpc.model.ProjectModelImpl

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:获取project 业务Presenter
 */
class ProjectPresenterImpl :
    BasePresenter<HomeProjectContract.ProjectView>(),
    HomeProjectContract.ProjectPresenter {
    private val projectModel: ProjectModelImpl by lazy {
        ProjectModelImpl()
    }
    /**
     * 获取project trees
     */
    override fun getProjectTrees() {
        if (!isViewAttached()) return
        projectModel.getProjectTrees()?.compose(view?.bindToLifecycle()).subscribe({ treeBean ->
            view?.apply {
                showTrees(treeBean.data)
            }

        }, { error ->
            view?.apply {
                showErrorView(ExceptionHandle.handleException(error),ErrorCode.UNKNOWN_ERROR)
            }
        })
    }
    /**
     * 获取 project list
     */
    override fun getProjectLists(page: Int,cid:Int) {
        if (!isViewAttached()) return
        view?.showLoading()
        projectModel.getProjectLists(page,cid)?.subscribe({ projectlBean ->
            view?.apply {
                Handler().postDelayed({
                    view?.hideLoading()
                    when {
                        projectlBean.errorCode<0 -> showErrorView("状态码返回错误",ErrorCode.UNKNOWN_ERROR)
                        projectlBean.data.datas.size == 0 -> showEmptyView()
                        projectlBean.errorCode == 0 -> showProjectsList(projectlBean)
                    }
                },1000)

            }

        }, { error ->
            view?.apply {
                view?.hideLoading()
                showErrorView(ExceptionHandle.handleException(error),ErrorCode.UNKNOWN_ERROR)
            }
        })
    }
}