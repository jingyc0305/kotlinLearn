package chenrui.com.kotlindemo.kotlin.ui.fragment

import android.os.Bundle
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseFragment

class PersionalFragment:BaseFragment() {
    private var mTitle : String? = null
    companion object {
        fun getInstance(title:String):PersionalFragment{
            val fragment = PersionalFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun initLayoutResId(): Int {
        return R.layout.fragment_persional
    }

    override fun initView() {
    }

    override fun initData() {
    }

}