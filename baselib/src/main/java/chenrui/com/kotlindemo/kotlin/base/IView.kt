package chenrui.com.kotlindemo.kotlin.base


/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:父级基础视图层 V [做任何是都是有结果的,它就代表想要的结果 也就是理想的实现 比如学习跳舞 学习就是Presenter,展现舞蹈就是view]
 */
interface IView {
    /**
     * 加载中...
     */
    fun showLoading()

    /**
     * 加载结束
     */
    fun hideLoading()

    fun getHostActivity(): BaseActivity
    fun getHostFragment(): BaseFragment
}