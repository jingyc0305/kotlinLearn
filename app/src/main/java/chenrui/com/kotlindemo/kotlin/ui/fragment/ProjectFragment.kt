package chenrui.com.kotlindemo.kotlin.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseFragment
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.mpc.presenter.ProjectPresenterImpl
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseFragment(), HomeProjectContract.ProjectView {
    private var mTitle: String? = null
    private var tabList = ArrayList<String>()
    private val fragmentlists = ArrayList<Fragment>()
    private var mProjectPresenter: ProjectPresenterImpl = ProjectPresenterImpl()
    private var mProjects: MutableList<ProjectTreeBean.Data>? = null
    companion object {
        fun getInstance(title: String,mProjects:MutableList<ProjectTreeBean.Data>): ProjectFragment {
            val fragment = ProjectFragment()
            val bundle = Bundle()
            fragment.mTitle = title
            fragment.mProjects = mProjects
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_project
    }

    override fun initView() {
        //绑定view
        mProjectPresenter.attachView(this)
        mProjects?.let {
            for ((index, value) in mProjects!!.withIndex()) {
                tabList.add(value.name)
                fragmentlists?.add(ProjectContentFragment.getInstance(value.id))
            }
            project_viewpager?.adapter =
                    InnerPagerAdapter(childFragmentManager, fragmentlists,tabList.toTypedArray())
            project_tab_layout?.setViewPager(project_viewpager)
            project_viewpager?.offscreenPageLimit = 2
        }
    }

    override fun initData() {
        mProjectPresenter.getProjectTrees()
    }

    override fun showTrees(mProjects: MutableList<ProjectTreeBean.Data>) {
        //如果预加载分类失败的话 可能由于网络问题 这里会在当前页面重新主动获取一次 然后刷新页面
        error_layout.visibility = View.GONE
        if(this.mProjects == null || this.mProjects!!.size == 0){
            for ((index, value) in mProjects!!.withIndex()) {
                tabList.add(value.name)
                fragmentlists?.add(ProjectContentFragment.getInstance(value.id))
            }
            project_viewpager?.adapter =
                    InnerPagerAdapter(childFragmentManager, fragmentlists,tabList.toTypedArray())
            project_tab_layout?.setViewPager(project_viewpager)
        }
    }

    internal inner class InnerPagerAdapter(
        fm: FragmentManager,
        fragments: ArrayList<Fragment>,
        private val titles: Array<String>
    ) :
        FragmentStatePagerAdapter(fm) {
        private var fragments = ArrayList<Fragment>()
        init {
            this.fragments = fragments
        }
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun destroyItem(container: View, position: Int, obj: Any) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }
    override fun createPresenters(): Array<out BasePresenter<*>>? {
        return arrayOf(mProjectPresenter)
    }
    override fun showProjectsList(mProjectsBean: ProjectsBean) {

    }

    override fun showEmptyView() {
        error_layout.visibility = View.VISIBLE
    }

    override fun showErrorView(msg: String,errorcode:Int) {
        error_layout.visibility = View.VISIBLE
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mProjectPresenter.detachView()
    }
}