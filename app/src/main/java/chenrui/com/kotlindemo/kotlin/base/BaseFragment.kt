package chenrui.com.kotlindemo.kotlin.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import chenrui.com.kotlindemo.R

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:父级基础视图View Fragment [母亲一样,只管小事情 如:数据加载 视图显示 布局碎片拼接 等家庭琐事]
 */
abstract class BaseFragment : Fragment(){
    /**
     * 视图是否初始化完成
     */
    private var isViewPrepare : Boolean = false
    /**
     * 是否加载过数据
     */
    private var isLoadedData:Boolean = false
    var mDispatcher : VPDispatcher? = null
    var dataEmptyView : View? = null
    var netErrorView : View? = null
    var loadingView : View? = null
    // 由子类提供当前页面所有需要绑定的Presenter。
    open fun createPresenters():Array<out BasePresenter<*>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(initLayoutResId(),null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewPrepare = true
        initView()
        //初始化状态视图
        loadingView = layoutInflater?.inflate(R.layout.loading_view,null,false)
        dataEmptyView = layoutInflater?.inflate(R.layout.empty_view,null,false)
        netErrorView = layoutInflater?.inflate(R.layout.error_view,null,false)
        //创建VP调度分发器 用于对单页面绑定多个presenter
        mDispatcher = VPDispatcher()
        //V-P生命周期绑定
        mDispatcher?.dispatchOnCreate(savedInstanceState)
        // 创建所有的presenter实例，并通过mDispatcher进行绑定
        createPresenters()?.forEach {
            mDispatcher?.addPresenter(it)
        }
        //加载数据
        layzLoadData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            layzLoadData()
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }


    override fun onDestroy() {
        super.onDestroy()
        // 销毁时mDispatcher会自动进行所有的Presenter的解绑。
        // 所以具体的V层并不需要再自己去手动进行解绑操作了
        mDispatcher?.dipatchOnDestroy()
    }

    /**
     * 懒加载
     */
    fun layzLoadData(){

        if(userVisibleHint && isViewPrepare && !isLoadedData){
            initData()
            isLoadedData = true
        }
    }

    /**
     * 初始化布局
     */
    abstract fun initLayoutResId():Int
    /**
     * 初始化视图控件
     */
    abstract fun initView()
    /**
     * 初始化数据
     */
    abstract fun initData()
    /**
     * 初始化Presenter
     */
    //abstract fun initPresenter()
}