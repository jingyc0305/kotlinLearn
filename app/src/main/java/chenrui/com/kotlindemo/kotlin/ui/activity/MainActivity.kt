package chenrui.com.kotlindemo.kotlin.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseActivity
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.bean.TabEntites
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.mpc.presenter.ProjectPresenterImpl
import chenrui.com.kotlindemo.kotlin.ui.fragment.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 主界面
 */
@Route(path = "/home/mian")
class MainActivity: BaseActivity(),HomeProjectContract.ProjectView{
    private val mTitles = arrayOf("首页","导航","项目","体系","我")
    private val mTabs = ArrayList<CustomTabEntity>()
    private var mHomeFragment:HomeFragment?=null
    private var mNaviFragment:NaviFragment?=null
    private var mProjectFragment:ProjectFragment?=null
    private var mSystemFragment:SystemFragment?=null
    private var mPersionalFragment:PersionalFragment?=null

    private var mProjectPresenter: ProjectPresenterImpl = ProjectPresenterImpl()
    private var mProjects: MutableList<ProjectTreeBean.Data>? = null
    // 未选中的图标
    private val mIconUnSelectIds = intArrayOf(
        R.mipmap.ic_home_normal,
        R.mipmap.ic_discovery_normal,
        R.mipmap.ic_discovery_normal,
        R.mipmap.ic_hot_normal,
        R.mipmap.ic_mine_normal

    )
    // 被选中的图标
    private val mIconSelectIds = intArrayOf(
        R.mipmap.ic_home_selected,
        R.mipmap.ic_discovery_selected,
        R.mipmap.ic_discovery_selected,
        R.mipmap.ic_hot_selected,
        R.mipmap.ic_mine_selected
    )
    //默认选中0
    private var mSelectIndex = 0

    override fun initLayoutResId() = R.layout.activity_main

    override fun initData() {
        mProjectPresenter.getProjectTrees()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mSelectIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mSelectIndex
        swichFragment(mSelectIndex)
    }
    override fun initView() {
        mProjectPresenter.attachView(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState?.putInt("currTabIndex", mSelectIndex)
        }
        super.onSaveInstanceState(outState)
    }

    override fun createPresenters(): Array<out BasePresenter<*>>? {
        return arrayOf(mProjectPresenter)
    }
    private fun swichFragment(index : Int) {
        val mTransaction = supportFragmentManager.beginTransaction()
        hideFragments(mTransaction)
        when(index){
            0 -> mHomeFragment?.let {
                mTransaction.show(it)
            }?:HomeFragment.getInstance(mTitles[index]).let {
                mHomeFragment = it
                mTransaction.add(R.id.fl_container,it,"home")
            }
            1 -> mNaviFragment?.let {
                mTransaction.show(it)
            }?:NaviFragment.getInstance(mTitles[1]).let {
                mNaviFragment = it
                mTransaction.add(R.id.fl_container,it,"nav")
            }
            2 -> mProjectFragment?.let {
                mTransaction.show(it)
            }?: mProjects?.let {
                ProjectFragment.getInstance(mTitles[2], it).let {
                    mProjectFragment = it
                    mTransaction.add(R.id.fl_container,it,"project")
                }
            }
            3 -> mSystemFragment?.let {
                mTransaction.show(it)
            }?:SystemFragment.getInstance(mTitles[3]).let {
                mSystemFragment = it
                mTransaction.add(R.id.fl_container,it,"system")
            }
            4 -> mPersionalFragment?.let {
                mTransaction.show(it)
            }?:PersionalFragment.getInstance(mTitles[4]).let {
                mPersionalFragment = it
                mTransaction.add(R.id.fl_container,it,"persional")
            }
            else ->{

            }
        }
        mSelectIndex = index
        tab_layout.currentTab = mSelectIndex
        mTransaction.commitAllowingStateLoss()
    }

    private fun initTab() {
        (0 until mTitles.size).mapTo(mTabs){
            TabEntites(
                mTitles[it],
                mIconSelectIds[it],
                mIconUnSelectIds[it]
            )
        }
        tab_layout.setTabData(mTabs)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
            override fun onTabReselect(position: Int) {
                swichFragment(position)
            }

            override fun onTabSelect(position: Int) {
                swichFragment(position)
            }
        })

    }
    private fun hideFragments(mTransaction : FragmentTransaction){
        mHomeFragment?.let { mTransaction.hide(it) }
        mNaviFragment?.let { mTransaction.hide(it) }
        mProjectFragment?.let { mTransaction.hide(it) }
        mSystemFragment?.let { mTransaction.hide(it) }
        mPersionalFragment?.let { mTransaction.hide(it) }
    }
    override fun showTrees(mProjects: MutableList<ProjectTreeBean.Data>) {
        this.mProjects = mProjects
    }

    override fun showProjectsList(mProjectsBean: ProjectsBean) {
    }

    override fun showEmptyView() {
    }

    override fun showErrorView(msg: String,errorcode:Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mProjectPresenter.detachView()
    }

}