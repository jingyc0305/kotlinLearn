package chenrui.com.kotlindemo.kotlin.base

import android.content.Intent
import android.os.Bundle

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:基础业务逻辑层Presenter P-V的绑定与解绑 [像儿女一样,在父母的管理下,去做很多事,当然只是举例,现实可能是不听话的 =.=!~]
 */
open class BasePresenter<V : IView> : IPresenter<V> {
    var view : V? = null
    override fun attachView(view: V) {
        this.view = view
    }

    override fun getActivity(): BaseActivity {
        return view?.getHostActivity()?:throw RuntimeException("Could not call getActivity if the View is not attached")
    }

    override fun getFragment(): BaseFragment {
        return view?.getHostFragment()?:throw RuntimeException("Could not call getFragment if the View is not attached")
    }
    override fun detachView() {
        this.view = null
    }

    /**
     * 如果是使用异步回调的方式去从model层获取的数据。
     * 那么很可能，接收到回调消息的之前，这个时候view已被解绑置空。导致通知失败
     * 对于任务中有异步回调操作的，应该在回调处。先行判断是否已解绑。若已解绑则跳过当前操作：
     * 示例: if (!isViewAttached()) return view?.hideLoadingDialog()
     */
    fun isViewAttached():Boolean{
        return view != null
    }
    /**
     * 检测view是否已经被绑定
     */
    fun checkViewIsAttached() {
        if (view == null) throw RuntimeException("view is not attached,please let presenter to attach the view")
    }
    var isAttached  = isViewAttached()
    /**
     * 生命周期方法
     */
    open fun onCreate(bundle: Bundle?) {}
    open fun onStart(){}
    open fun onRestart(){}
    open fun onResume(){}
    open fun onPause(){}
    open fun onStop(){}
    open fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?){}
    open fun onDestroy(){}
    open fun onSaveInstanceState(outState: Bundle?){}
    open fun onRestoreInstanceState(savedInstanceState: Bundle?){}
}