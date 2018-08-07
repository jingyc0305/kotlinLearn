package chenrui.com.kotlindemo.kotlin.mpc.presenter

import android.os.Handler
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.http.exception.ErrorCode
import chenrui.com.kotlindemo.kotlin.http.exception.ExceptionHandle
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.mpc.model.ProjectModelImpl
import io.reactivex.Observable
import io.reactivex.functions.Function

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
//    projectModel.getProjectTrees().flatMap(Function<ProjectTreeBean,Observable<ProjectsBean>>{
//        return@Function projectModel.getProjectLists(page,it.data[0].id)
//    })
    //嵌套请求 拿到标签的cid后去取对应的projects 默认取第0个
    override fun getProjectTrees() {
        if (!isViewAttached()) return
        view?.showLoading()
        projectModel.getProjectTrees()?.compose(this.getActivity()?.bindToLifecycle()).flatMap(
            Function<ProjectTreeBean,Observable<ProjectsBean>>{
                view?.showTrees(it.data)
                return@Function projectModel.getProjectLists(0,it.data[0].id)
            }).subscribe({ projectlBean ->
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
                hideLoading()
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
                    hideLoading()
                    when {
                        projectlBean.errorCode<0 -> showErrorView("状态码返回错误",ErrorCode.UNKNOWN_ERROR)
                        projectlBean.data.datas.size == 0 -> showEmptyView()
                        projectlBean.errorCode == 0 -> showProjectsList(projectlBean)
                    }
                },1000)

            }

        }, { error ->
            view?.apply {
                hideLoading()
                showErrorView(ExceptionHandle.handleException(error),ErrorCode.UNKNOWN_ERROR)
            }
        })
    }
}