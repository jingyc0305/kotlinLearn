package chenrui.com.kotlindemo.kotlin.mpc.presenter

import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.mpc.contract.LoginContract

class LoginPresenterImpl: BasePresenter<LoginContract.LoginView>(),LoginContract.LoginPresenter{
    //登录
    override fun login() {
        //view?.showLoading()
        //模拟网络请求耗时操作
        Thread(Runnable { Thread.sleep(3000) }).start()
        //与View层交互 通知界面更新
        view?.onLoginSucess()
        if(!isViewAttached())return
        //view?.hideLoading()
    }

    //修改密码
    override fun changePassword() {
    }
    //发送验证码
    override fun sendVertifyCode() {
    }
}