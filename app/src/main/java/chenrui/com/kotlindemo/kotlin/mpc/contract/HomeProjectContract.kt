package chenrui.com.kotlindemo.kotlin.mpc.contract

import chenrui.com.kotlindemo.kotlin.base.IModle
import chenrui.com.kotlindemo.kotlin.base.IPresenter
import chenrui.com.kotlindemo.kotlin.base.IView
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import io.reactivex.Observable
/**
 * @Author:JIngYuchun
 * @Date:2018/7/26
 * @Description:项目契约类
 */
interface HomeProjectContract{

    interface ProjectModle : IModle {
        //获取project trees
        fun getProjectTrees(): Observable<ProjectTreeBean>
        //获取projects
        fun getProjectLists(page:Int,cid : Int): Observable<ProjectsBean>

    }
    interface ProjectView : IView {
        //显示trees
        fun showTrees(mProjects: MutableList<ProjectTreeBean.Data>)
        //显示projects
        fun showProjectsList(mProjectsBean: ProjectsBean)
        //显示空
        fun showEmptyView()
        //显示加载失败
        fun showErrorView(msg:String,errorcode:Int)
    }
    interface ProjectPresenter : IPresenter<ProjectView> {
        //获取banner
        fun getProjectTrees()
        fun getProjectLists(page:Int,cid : Int)
    }
}