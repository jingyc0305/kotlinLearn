package chenrui.com.kotlindemo.kotlin.ui.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.R.layout.empty_view
import chenrui.com.kotlindemo.R.layout.loading_view
import chenrui.com.kotlindemo.kotlin.adapter.HomeArticalsAdapter
import chenrui.com.kotlindemo.kotlin.app.MyApplication
import chenrui.com.kotlindemo.kotlin.base.BaseFragment
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import chenrui.com.kotlindemo.kotlin.bean.HomeBannerBean
import chenrui.com.kotlindemo.kotlin.mpc.contract.HomeContract
import chenrui.com.kotlindemo.kotlin.mpc.model.HomeModelImpl
import chenrui.com.kotlindemo.kotlin.mpc.presenter.HomePresenterImpl
import chenrui.com.kotlindemo.kotlin.util.NetWorkUtil
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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
    var homeAdapter : HomeArticalsAdapter? = null
    var data: MutableList<HomeArticalBean.Data.Data>? = null
    var curPage : Int = 0//默认读取第一页数据
    var dataEmptyView : View? = null
    var netErrorView : View? = null
    var loadingView : View? = null
    var bannerView : View? = null
    var mBGABanner : BGABanner? = null
    companion object {
        fun getInstance(title : String?) : HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    /**
     * todo // 这里 强行的又把view和model耦合在一起了 正常还是应该放在p层初始化的  后面改下
     */
    private val homeBannerModle: HomeModelImpl by lazy {
        HomeModelImpl()
    }
    override fun initLayoutResId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        //绑定view 和 modle
        mPresenter.attachView(this,homeBannerModle)
        //初始化
        loadingView = layoutInflater?.inflate(loading_view,null,false)
        dataEmptyView = layoutInflater?.inflate(empty_view,null,false)
        netErrorView = layoutInflater?.inflate(R.layout.error_view,null,false)
        bannerView = layoutInflater?.inflate(R.layout.home_banner,home_banner_rv.parent as ViewGroup,false)
        mBGABanner = bannerView?.findViewById(R.id.home_banner_content)
        //重试加载数据 同刷新
        netErrorView?.setOnClickListener { onFailedOrEmptyRetry() }
        dataEmptyView?.setOnClickListener { onFailedOrEmptyRetry() }
        //初始化adpater
        homeAdapter = HomeArticalsAdapter(R.layout.item_home_artical_list,data)
        //布局管理器
        home_banner_rv.layoutManager = LinearLayoutManager(activity)
        //默认分割线
        home_banner_rv.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        //设置适配器
        home_banner_rv.adapter = homeAdapter
        //设置头布局
        homeAdapter?.addHeaderView(bannerView)
        //设置脚部布局
    }

    /**
     * 重新获取数据 同刷新
     */
    private fun onFailedOrEmptyRetry() {
        //if(data!=null) data.removeAll(data)
        initData()
    }

    override fun initData() {
        if(!NetWorkUtil.isNetworkAvailable(MyApplication.context)) showErrorView("加载失败,点我重试")
        //获取banner
        mPresenter.getBanners()
        //获取文章 分页获取
        mPresenter.getArticals(curPage)
    }

    /**
     * 提供需要与此页面相绑定的Presenter, 可绑定多个Presenter
     */
    override fun createPresenters(): Array<out BasePresenter<*, *>>? {
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
    override fun showArticalList(list: MutableList<HomeArticalBean.Data.Data>) {
        data = list
        homeAdapter?.setNewData(list)
    }

    override fun showLoading() {
        homeAdapter?.emptyView = loadingView
    }
    override fun hideLoading() {
    }
    override fun showErrorView(str:String) {
        homeAdapter?.emptyView = netErrorView
    }
    override fun showEmptyView() {
        homeAdapter?.emptyView = dataEmptyView
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}