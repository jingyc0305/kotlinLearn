package chenrui.com.kotlindemo.kotlin.bean

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:首页广告轮播
 */
data class HomeBannerBean(
    val data: MutableList<Data>,
    val errorCode: Int,
    val errorMsg: String
) {
    
    data class Data(
        val desc: String,
        val id: Int,
        val imagePath: String,
        val isVisible: Int,
        val order: Int,
        val title: String,
        val type: Int,
        val url: String
    )
}