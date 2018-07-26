package chenrui.com.kotlindemo.kotlin.mpc.contract

import chenrui.com.kotlindemo.kotlin.base.IModle
import chenrui.com.kotlindemo.kotlin.base.IPresenter
import chenrui.com.kotlindemo.kotlin.base.IView

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:登录契约类 [像父亲和母亲以及你之间商量好的计划一样 准备做什么 预期会发生什么]
 */
interface LoginContract {
    interface LoginModel : IModle
    interface LoginView : IView{
        fun onLoginSucess()
        fun onLoginFalied()
        fun onLoginError()
    }
    interface LoginPresenter : IPresenter<LoginView>{
        fun login()
        fun changePassword()
        fun sendVertifyCode()
    }
}