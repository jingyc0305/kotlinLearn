package chenrui.com.kotlindemo.kotlin.base

import android.os.Bundle
/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description: VP调度分发器 用于对单页面绑定多个presenter 及V-P生命周期绑定
 * 在需要共用一些基础业务逻辑的时候。此一对多的绑定可以做到Presenter的复用
 * [做任何事情都会有结果,它就像一个老师指引你的人生方向,该做什么,不该做什么,什么时候做,
 * 这里想象是父母交给老师去支配人生的学习方面,父母引导的是成长生活方面,其实这些从老师和父母那里都能学到]
 */
class VPDispatcher {
    private val presenters:MutableList<BasePresenter<*,*>> = mutableListOf()

    // ==== 添加与移除Presenter ========
    fun <V:IView,M:IModle> addPresenter(presenter:BasePresenter<V,M>) {
        presenters.add(presenter)
    }
    internal fun <V:IView,M:IModle> removePresenter(presenter:BasePresenter<V,M>) {
        presenters.remove(presenter)
    }

    // ==== 绑定生命周期 ==========
    fun dispatchOnCreate(bundle: Bundle?) {

    }
    fun dipatchOnDestroy(){
        presenters.forEach {
            if (it.isViewAttached()) { it.onDestroy() }
            // 生命方法派发完毕后。自动解绑
            removePresenter(it)
        }
    }
    fun dispatchOnRestoreInstanceState(savedInstanceState: Bundle?) {

    }

}