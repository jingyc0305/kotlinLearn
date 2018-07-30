package chenrui.com.kotlindemo.kotlin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.adapter.ProjectsAdapter
import chenrui.com.kotlindemo.kotlin.app.IntentKeyConstant
import chenrui.com.kotlindemo.kotlin.base.BaseFragment
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.mpc.presenter.ProjectPresenterImpl
import chenrui.com.kotlindemo.kotlin.ui.activity.ArticalDetailActivity
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_nav.*

/**
 * @Author:JIngYuchun
 * @Date:2018/7/26
 * @Description:项目列表 通用Fragment
 */
class ProjectContentFragment : BaseFragment(),HomeProjectContract.ProjectView{
    private var cid : Int? = null
    private var mPresenter : ProjectPresenterImpl = ProjectPresenterImpl()
    private var projectAdapter:ProjectsAdapter? = null
    var data:MutableList<ProjectsBean.Data.Data>? = null
    var curPage : Int = 0//默认读取第一页数据
    var totalPages : Int = 0//总页数
    private var errorInfoTv: TextView? = null
    companion object {
        fun getInstance(cid:Int):ProjectContentFragment{
            val fragment = ProjectContentFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.cid = cid
            return fragment
        }
    }
    override fun initLayoutResId(): Int {
        return R.layout.fragment_nav
    }

    override fun initView() {
        mPresenter.attachView(this)
        //初始化adpater
        projectAdapter = ProjectsAdapter(R.layout.item_home_projects_list,data)
        //布局管理器
        home_project_nav_rv.layoutManager = LinearLayoutManager(activity)
        //设置适配器
        home_project_nav_rv.adapter = projectAdapter
        //设置脚部布局
        //重试加载数据点击事件 同刷新
        netErrorView?.setOnClickListener { onFailedOrEmptyRetry() }
        dataEmptyView?.setOnClickListener { onFailedOrEmptyRetry() }
        //设置item点击事件 进入详情
        projectAdapter?.setOnItemClickListener { _, _, position ->
            var intent = Intent(activity, ArticalDetailActivity::class.java)
            intent.putExtra(IntentKeyConstant.artical_url_key, projectAdapter!!.data[position].link)
            intent.putExtra(IntentKeyConstant.artical_title_key, projectAdapter!!.data[position].title)
            startActivity(intent)
        }
        //设置 Header样式
        project_refreshLayout.run {
            setRefreshHeader(ClassicsHeader(activity))
            //设置 Footer样式
            setRefreshFooter(ClassicsFooter(activity!!).setSpinnerStyle(SpinnerStyle.Scale))
            //设置 刷新监听
            project_refreshLayout.setOnRefreshListener { refresh ->
                mPresenter.getProjectLists(0,cid!!)
                refresh.setNoMoreData(false)
                curPage = 0
                refresh.finishRefresh(2000,true)//传入false表示刷新失败
            }
            project_refreshLayout.setOnLoadMoreListener { refresh ->
                //refresh.finishLoadMore(2000/*,false*/)//传入false表示加载失败
                curPage++
                if(curPage <= totalPages-1){
                    mPresenter.getProjectLists(curPage,cid!!)
                    refresh.finishLoadMore(1000,true,false)
                }else if(curPage == totalPages-2){
                    mPresenter.getProjectLists(curPage,cid!!)
                    //如果是最后一页了
                    refresh.finishLoadMoreWithNoMoreData()
                }

            }
        }
    }

    override fun createPresenters(): Array<out BasePresenter<*>>? {
        return arrayOf(mPresenter)
    }
    private fun onFailedOrEmptyRetry() {
        initData()
    }

    override fun initData() {
        mPresenter?.getProjectLists(0,cid!!)
    }
    override fun showTrees(mProjects: MutableList<ProjectTreeBean.Data>) {
    }

    override fun showProjectsList(mProjectsBean: ProjectsBean) {
        totalPages = mProjectsBean.data.pageCount
        projectAdapter?.addData(mProjectsBean.data.datas)
    }

    override fun showLoading() {
        projectAdapter?.emptyView = loadingView
    }
    override fun hideLoading() {
    }
    override fun showErrorView(str:String,errorCode: Int) {
        projectAdapter?.emptyView = netErrorView
        errorInfoTv = netErrorView?.findViewById(R.id.error_info)
        errorInfoTv?.text = """$str($errorCode)"""
    }
    override fun showEmptyView() {
        projectAdapter?.emptyView = dataEmptyView
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}