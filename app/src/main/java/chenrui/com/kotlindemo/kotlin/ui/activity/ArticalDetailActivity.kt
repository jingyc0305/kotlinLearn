package chenrui.com.kotlindemo.kotlin.ui.activity

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.app.IntentKeyConstant
import chenrui.com.kotlindemo.kotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_artical_detail.*
import kotlinx.android.synthetic.main.toolbar_layout.*

/**
 * @Author:JIngYuchun
 * @Date:2018/7/25
 * @Description:
 */
class ArticalDetailActivity: BaseActivity() {
    private var mArticalUrl : String? = null
    private var mArticalTitle : String? = null
    override fun initLayoutResId(): Int {
        return R.layout.activity_artical_detail
    }

    override fun initData() {
        artical_webview.loadUrl(mArticalUrl)
    }
    override fun initView() {
        mArticalUrl = intent.getStringExtra(IntentKeyConstant.artical_url_key)
        mArticalTitle = intent.getStringExtra(IntentKeyConstant.artical_title_key)
        //设置标题栏
        toolbar_title_tv.text = mArticalTitle
        //不显示右侧
        toolbar_right_tv. text = "分享"
        artical_webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }
        }
    }
}