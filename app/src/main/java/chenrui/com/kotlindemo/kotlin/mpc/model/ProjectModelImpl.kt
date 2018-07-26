package chenrui.com.kotlindemo.kotlin.mpc.model

import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.http.RetrofitManager
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.rxhelp.RxSchedulersHelper
import io.reactivex.Observable

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:获取project 数据层model
 */
class ProjectModelImpl:HomeProjectContract.ProjectModle{
    override fun getProjectTrees(): Observable<ProjectTreeBean> {
        return RetrofitManager.service.getProjectTrees().compose(RxSchedulersHelper.ioToMain())
    }

    override fun getProjectLists(page: Int,cid : Int):Observable<ProjectsBean> {
        return RetrofitManager.service.getProjectList(page,cid).compose(RxSchedulersHelper.ioToMain())
    }
}