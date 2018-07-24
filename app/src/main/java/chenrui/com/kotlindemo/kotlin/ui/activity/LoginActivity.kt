package chenrui.com.kotlindemo.kotlin.ui.activity

import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseActivity
import chenrui.com.kotlindemo.kotlin.base.BasePresenter
import chenrui.com.kotlindemo.kotlin.mpc.contract.LoginContract
import chenrui.com.kotlindemo.kotlin.mpc.presenter.LoginPresenterImpl
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @Author:JIngYuchun
 * @Date:2018/7/20
 * @Description:登录组件
 */
class LoginActivity : BaseActivity(),LoginContract.LoginView {
    val presenter = LoginPresenterImpl()
    override fun createPresenters(): Array<out BasePresenter<*, *>>? {
        return arrayOf(presenter)
    }

    override fun initLayoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
    }

    override fun initView() {
        email_sign_in_button.setOnClickListener{presenter.login()}
    }

    override fun onLoginSucess() {
    }

    override fun onLoginFalied() {
    }

    override fun onLoginError() {
    }

}
