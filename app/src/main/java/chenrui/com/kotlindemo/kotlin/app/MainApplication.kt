package chenrui.com.kotlindemo.kotlin.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import chenrui.com.baselib.AppConfig
import chenrui.com.baselib.BaseApp
import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.base.BaseActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import java.util.*
import kotlin.properties.Delegates



class MainApplication: BaseApp() {
    override fun initModuleApp(application: Application?) {
        for (moduleApp in AppConfig.moduleApps) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.initModuleApp(this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }

        }
    }

    override fun initModuleData(application: Application?) {
        for (moduleApp in AppConfig.moduleApps) {
            try {
                val clazz = Class.forName(moduleApp)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.initModuleData(this)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }

        }
    }

    companion object {
        var context : Context by Delegates.notNull()
            private set
    }
    var activitys:LinkedList<Activity>? = null
    override fun onCreate() {
        super.onCreate()
        activitys = LinkedList()
        context = applicationContext
        // 初始化 ARouter
        if (chenrui.com.kotlindemo.BuildConfig.DEBUG) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        }
        // 初始化 ARouter
        ARouter.init(this)
        //logger日志
        initConfig()
        //计算设备大小
        //DisplayManager.init(this)
        //注册Activity生命周期监听
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        // 初始化组件 Application
        initModuleApp(this)
        // 其他操作
        // 所有 Application 初始化后的操作
        initModuleData(this)
    }
    /**
     * 初始化配置
     */
    private fun initConfig() {

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 隐藏线程信息 默认：显示
            .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag("JingYuchun")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        com.orhanobut.logger.Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return com.orhanobut.logger.BuildConfig.DEBUG
            }
        })
    }

    /**
     * 监听每一个activity的生命周期
     */
    private var mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks{
        override fun onActivityPaused(p0: Activity?) {
            //注册所有的activity
            activitys?.add(p0!!)
            //全局配置toolbar
            var toolbar = p0?.findViewById(R.id.toolbar) as Toolbar
            toolbar?.let {
                when(p0){
                    is BaseActivity -> p0?.setSupportActionBar(toolbar)
                }
                (p0 as BaseActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
            }
            var backIv = p0?.findViewById(R.id.toolbar_back_iv) as ImageView
            backIv.setOnClickListener { p0.finish() }
            //设置右侧内容是否显示
            //设置左侧内容是否显示
            //设置是否使用沉浸式状态栏

        }

        override fun onActivityResumed(p0: Activity?) {
        }

        override fun onActivityStarted(p0: Activity?) {
        }

        override fun onActivityDestroyed(p0: Activity?) {
        }

        override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        }

        override fun onActivityStopped(p0: Activity?) {
        }

        override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        }

    }

    /**
     * 全局退出app
     */
    fun exitApp(){
        for (act in activitys!!){
            act.finish()
        }
    }
}