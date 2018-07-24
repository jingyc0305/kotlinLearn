package chenrui.com.kotlindemo.kotlin.bean

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntites(var title:String,var selectIcon:Int,private var unSelectIcon:Int) : CustomTabEntity {
    override fun getTabUnselectedIcon(): Int {
        return unSelectIcon
    }

    override fun getTabSelectedIcon(): Int {
        return selectIcon
    }

    override fun getTabTitle(): String {
        return title
    }
}