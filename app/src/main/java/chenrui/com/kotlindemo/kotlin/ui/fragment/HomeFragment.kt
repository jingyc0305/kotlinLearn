package chenrui.com.kotlindemo.kotlin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import chenrui.com.componentbase.ServiceFactory
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.adapter.HomeArticalsAdapter
import chenrui.com.kotlindemo.kotlin.app.IntentKeyConstant
import chenrui.com.kotlindemo.kotlin.base.BaseFragment
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeContract
import chenrui.com.kotlindemo.kotlin.mpc.presenter.HomePresenterImpl
import chenrui.com.kotlindemo.kotlin.ui.activity.ArticalDetailActivity
import cn.bingoogolapple.bgabanner.BGABanner
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * @Author:JIngYuchun
 * @Date:2018/7/23
 * @Description:首页
 */
class HomeFragment : BaseFragment(),HomeContract.HomeView{
    var mTitle : String? = null
    private var mPresenter : HomePresenterImpl = HomePresenterImpl()
    private var homeAdapter : HomeArticalsAdapter? = null
    var data: MutableList<HomeArticalBean.Data.Data>? = null
    private var curPage : Int = 0//默认读取第一页数据
    private var totalPages : Int = 0//总页数
    private var bannerView : View? = null
    private  var mBGABanner : BGABanner? = null
    private var errorInfoTv:TextView? = null
    companion object {
        fun getInstance(title : String?) : HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun initLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        mPresenter.attachView(this)
        bannerView = layoutInflater?.inflate(R.layout.home_banner, home_banner_rv.parent as ViewGroup,false)
        //初始化banner
        mBGABanner = bannerView?.findViewById(R.id.home_banner_content)
        //初始化adpater
        homeAdapter = HomeArticalsAdapter(R.layout.item_home_artical_list,data)
        //布局管理器
        home_banner_rv?.layoutManager = LinearLayoutManager(context)
        //默认分割线 如果是卡片式 就不需要了
        //home_banner_rv.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        //设置适配器
        home_banner_rv?.adapter = homeAdapter
        //设置头布局
        homeAdapter?.addHeaderView(bannerView)
        //设置脚部布局
        //重试加载数据点击事件 同刷新
        netErrorView?.setOnClickListener { onFailedOrEmptyRetry() }
        dataEmptyView?.setOnClickListener { onFailedOrEmptyRetry() }
        //设置item点击事件 进入详情
        homeAdapter?.setOnItemClickListener { _, _, position ->
            if(ServiceFactory.getInstance().accountService.isLogin){
                var intent = Intent(activity,ArticalDetailActivity::class.java)
                intent.putExtra(IntentKeyConstant.artical_url_key, homeAdapter!!.data[position].link)
                intent.putExtra(IntentKeyConstant.artical_title_key, homeAdapter!!.data[position].title)
                startActivity(intent)
            }else{
                ARouter.getInstance().build("/account/login").withString("test_key", "我是跳转登录携带的参数").navigation()
            }
        }
        //设置 Header样式
        refreshLayout?.setRefreshHeader(ClassicsHeader(activity))
        //设置 Footer样式
        refreshLayout?.setRefreshFooter(ClassicsFooter(activity!!).setSpinnerStyle(SpinnerStyle.Scale))
        //设置 刷新监听
        refreshLayout?.setOnRefreshListener { refresh ->
            mPresenter.getArticals(0)
            refresh.setNoMoreData(false)
            curPage = 0
            refresh.finishRefresh(2000,true)//传入false表示刷新失败
        }
        refreshLayout?.setOnLoadMoreListener { refresh ->
            //refresh.finishLoadMore(2000/*,false*/)//传入false表示加载失败
            curPage++
            if(curPage <= totalPages-1){
                mPresenter.getArticals(curPage)
                refresh.finishLoadMore(1000,true,false)
            }else if(curPage == totalPages-2){
                mPresenter.getArticals(curPage)
                //如果是最后一页了
                refresh.finishLoadMoreWithNoMoreData()
            }

        }
    }

    /**
     * 重新获取数据 同刷新
     */
    private fun onFailedOrEmptyRetry() {
        //if(data!=null) data.removeAll(data)
        initData()
    }

    override fun initData() {
        //获取banner
        mPresenter.getBanners()
        //获取文章 分页获取
        mPresenter.getArticals(curPage)
    }

    /**
     * 提供需要与此页面相绑定的Presenter, 可绑定多个Presenter
     */
    override fun createPresenters(): Array<out BasePresenter<*>>? {
        return arrayOf(mPresenter)
    }

    /**
     * 显示banner
     */
    override fun showBanner(list: MutableList<HomeBannerBean.Data>) {
        mBGABanner?.setAdapter(object : BGABanner.Adapter<ImageView, String> {
            override fun fillBannerItem(bgaBanner: BGABanner?, imageView: ImageView?, feedImageUrl: String?, position: Int) {
                Glide.with(activity)
                    .load(feedImageUrl)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(RequestOptions().error(R.mipmap.placeholder_banner))
                    .apply(RequestOptions().placeholder(R.mipmap.placeholder_banner))
                    .into(imageView)

            }
        })
        val bannerUrlList = ArrayList<String>()
        val bannerTitleList = ArrayList<String>()
        //获取banner数据中的url 和 tip
        Observable.fromIterable(list).subscribe { list ->
            bannerUrlList.add(list.imagePath)
            bannerTitleList.add(list.title)
        }
        //给banner设置数据
        mBGABanner?.setData(bannerUrlList,bannerTitleList)
    }

    /**
     * 显示文章列表
     */
    override fun showArticalList(homeArticalBean: HomeArticalBean) {
        totalPages = homeArticalBean.data.pageCount
        homeAdapter?.addData(homeArticalBean.data.datas)

    }

    override fun showLoading() {
        homeAdapter?.emptyView = loadingView
    }
    override fun hideLoading() {
    }
    override fun showErrorView(str:String,errorcode:Int) {
        homeAdapter?.emptyView = netErrorView
        errorInfoTv = netErrorView?.findViewById(R.id.error_info)
        errorInfoTv?.text = """$str($errorcode)"""
    }
    override fun showEmptyView() {
        homeAdapter?.emptyView = dataEmptyView
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}