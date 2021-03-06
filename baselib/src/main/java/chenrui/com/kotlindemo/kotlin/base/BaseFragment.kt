package chenrui.com.kotlindemo.kotlin.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import chenrui.com.baselib.R
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:父级基础视图View Fragment [母亲一样,只管小事情 如:数据加载 视图显示 布局碎片拼接 等家庭琐事]
 */
abstract class BaseFragment : Fragment(),LifecycleProvider<FragmentEvent>,IView{
    /**
     * 视图是否初始化完成
     */
    private var isViewPrepare : Boolean = false
    /**
     * 是否加载过数据
     */
    private var isLoadedData:Boolean = false
    private var mDispatcher : VPDispatcher? = null
    var dataEmptyView : View? = null
    var netErrorView : View? = null
    var loadingView : View? = null
    private var mRootView: View? = null//缓存fragment View
    // 由子类提供当前页面所有需要绑定的Presenter。
    open fun createPresenters():Array<out BasePresenter<*>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayoutResId(),container,false)
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent则从parent删除，防止发生这个rootview已经有parent的错误。
        if(mRootView?.parent !=null){
            val mViewGroup = mRootView?.parent as ViewGroup
            mViewGroup.removeView(mRootView)
        }
        return mRootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewPrepare = true
        initStatusView()
        //创建VP调度分发器 用于对单页面绑定多个presenter
        initPresenter(savedInstanceState)
        initView()
        //加载数据
        layzLoadData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            layzLoadData()
        }
    }
    /**
     * 懒加载
     */
    private fun layzLoadData(){
        if(userVisibleHint && isViewPrepare&&!isLoadedData){
            initData()
            isLoadedData = true
        }
    }

    private fun initPresenter(savedInstanceState: Bundle?){
        mDispatcher = VPDispatcher()
        //V-P生命周期绑定
        mDispatcher?.dispatchOnCreate(savedInstanceState)
        // 创建所有的presenter实例，并通过mDispatcher进行绑定
        createPresenters()?.forEach {
            mDispatcher?.addPresenter(it)
        }
    }
    private fun initStatusView(){
        //初始化状态视图
        loadingView = layoutInflater?.inflate(R.layout.loading_view,null,false)
        dataEmptyView = layoutInflater?.inflate(R.layout.empty_view,null,false)
        netErrorView = layoutInflater?.inflate(R.layout.error_view,null,false)
    }
    override fun getHostActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun getHostFragment(): BaseFragment {
        return this
    }
    override fun showLoading() {
    }

    override fun hideLoading() {
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

    private val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleSubject.hide()
    }

    @CheckResult
    override fun <T> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    @CheckResult
    override fun <T> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }

    @CallSuper
    override fun onAttach(activity: android.app.Activity) {
        super.onAttach(activity)
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
        // 销毁时mDispatcher会自动进行所有的Presenter的解绑。
        // 所以具体的V层并不需要再自己去手动进行解绑操作了
        mDispatcher?.dipatchOnDestroy()
    }

    @CallSuper
    override fun onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
    }
}