package chenrui.com.kotlindemo.kotlin.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:父级基础视图View Activity [父亲一样,只管大事情,多事情,掌控全局 如:Fragment 等琐事]
 */
abstract class BaseActivity:AppCompatActivity(),IView{
    var mDispatcher : VPDispatcher? = null
    // 由子类提供当前页面所有需要绑定的Presenter。
    open fun createPresenters():Array<out BasePresenter<*,*>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //创建VP调度分发器 用于对单页面绑定多个presenter
        mDispatcher = VPDispatcher()
        //V-P生命周期绑定
        mDispatcher?.dispatchOnCreate(savedInstanceState)
        //初始化布局
        setContentView(initLayoutResId())
        //初始化控件
        initView()
        // 创建所有的presenter实例，并通过mDispatcher进行绑定
        createPresenters()?.forEach { mDispatcher?.addPresenter(it) }
        //初始化一些数据
        initData()
    }
    override fun showLoading() {
        Toast.makeText(this,"正在加载...",Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Toast.makeText(this,"加载成功",Toast.LENGTH_SHORT).show()
    }
    /**
     * 初始化布局资源
     */
    abstract fun initLayoutResId():Int
    /**
     * 初始化数剧
     */
    abstract fun initData()
    /**
     * 初始化视图
     */
    abstract fun initView()

    /**
     * 初始化presenter
     */
    //abstract fun initPresenter()

    override fun onDestroy() {
        super.onDestroy()
        // 销毁时mDispatcher会自动进行所有的Presenter的解绑。
        // 所以具体的V层并不需要再自己去手动进行解绑操作了
        mDispatcher?.dipatchOnDestroy()
    }

}