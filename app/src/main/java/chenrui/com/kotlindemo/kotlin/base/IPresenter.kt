package chenrui.com.kotlindemo.kotlin.base

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:父级基础业务逻辑层 P [曾经父母年前的时候也是去做很多事的 如今交给儿女去做了]
 */
interface IPresenter<in V : IView,in M:IModle> {
    /**
     * 绑定view
     */
    fun attachView(view : V,model:M)

    /**
     * 解绑view
     */
    fun detachView()

}