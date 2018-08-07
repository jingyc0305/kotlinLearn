package chenrui.com.kotlindemo.kotlin.ui.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.View
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseActivity
import chenrui.com.kotlindemo.kotlin.bean.ProjectTreeBean
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import chenrui.com.kotlindemo.kotlin.bean.TabEntites
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeProjectContract
import chenrui.com.kotlindemo.kotlin.ui.fragment.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

/**
 * 主界面
 */
@Route(path = "/home/mian")
class MainActivity : BaseActivity(), HomeProjectContract.ProjectView {
    private val mTitles = arrayOf("首页", "导航", "项目", "体系", "我")
    private val mTabs = ArrayList<CustomTabEntity>()
    private var mHomeFragment: HomeFragment? = null
    private var mNaviFragment: NaviFragment? = null
    private var mProjectFragment: ProjectFragment? = null
    private var mSystemFragment: SystemFragment? = null
    private var mPersionalFragment: PersionalFragment? = null

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mSelectIndex = savedInstanceState.getInt("currTabIndex")
            //防止fragment重新添加
            mHomeFragment = supportFragmentManager.findFragmentByTag("home") as HomeFragment
            mNaviFragment = supportFragmentManager.findFragmentByTag("nav") as NaviFragment
            mProjectFragment =
                    supportFragmentManager.findFragmentByTag("project") as ProjectFragment
            mSystemFragment = supportFragmentManager.findFragmentByTag("system") as SystemFragment
            mPersionalFragment =
                    supportFragmentManager.findFragmentByTag("personal") as PersionalFragment
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mSelectIndex
        swichFragment(mSelectIndex)
    }

    override fun initView() {
        //设置标题栏 不显示右侧和左侧
        //toolbar.title = mTitles[mSelectIndex]
        toolbar_title_tv.text = mTitles[mSelectIndex]
        toolbar_back_iv.visibility = View.GONE
        toolbar_right_tv.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState?.putInt("currTabIndex", mSelectIndex)
        }
        super.onSaveInstanceState(outState)
    }


    private fun swichFragment(index: Int) {
        val mTransaction = supportFragmentManager.beginTransaction()
        hideFragments(mTransaction)
        when (index) {
            0 -> mHomeFragment?.let {
                mTransaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[index]).let {
                mHomeFragment = it
                mTransaction.add(R.id.fl_container, it, "home")
            }
            1 -> mNaviFragment?.let {
                mTransaction.show(it)
            } ?: NaviFragment.getInstance(mTitles[1]).let {
                mNaviFragment = it
                mTransaction.add(R.id.fl_container, it, "nav")
            }
            2 -> mProjectFragment?.let {
                mTransaction.show(it)
            } ?: ProjectFragment.getInstance(mTitles[2]).let {
                mProjectFragment = it
                mTransaction.add(R.id.fl_container, it, "project")
            }

            3 -> mSystemFragment?.let {
                mTransaction.show(it)
            } ?: SystemFragment.getInstance(mTitles[3]).let {
                mSystemFragment = it
                mTransaction.add(R.id.fl_container, it, "system")
            }
            4 -> mPersionalFragment?.let {
                mTransaction.show(it)
            } ?: PersionalFragment.getInstance(mTitles[4]).let {
                mPersionalFragment = it
                mTransaction.add(R.id.fl_container, it, "personal")
            }
            else -> {

            }

        }
        mSelectIndex = index
        tab_layout.currentTab = mSelectIndex
        toolbar_title_tv.text = mTitles[index]
        mTransaction.commitAllowingStateLoss()
    }

    private fun initTab() {
        (0 until mTitles.size).mapTo(mTabs) {
            TabEntites(
                mTitles[it],
                mIconSelectIds[it],
                mIconUnSelectIds[it]
            )
        }
        tab_layout.setTabData(mTabs)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabReselect(position: Int) {
                swichFragment(position)
            }

            override fun onTabSelect(position: Int) {
                swichFragment(position)
            }
        })

    }

    private fun hideFragments(mTransaction: FragmentTransaction) {
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

    override fun showErrorView(msg: String, errorcode: Int) {
    }

}