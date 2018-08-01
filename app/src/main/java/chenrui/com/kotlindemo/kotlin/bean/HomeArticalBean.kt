package chenrui.com.kotlindemo.kotlin.bean

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:首页文章
 */
data class HomeArticalBean(
    val data: Data,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val curPage: Int,
        val datas: MutableList<DataBean>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
    ) {

        data class DataBean(
            val apkLink: String,
            val author: String,
            val chapterId: Int,
            val chapterName: String,
            val collect: Boolean,
            val courseId: Int,
            val desc: String,
            val envelopePic: String,
            val fresh: Boolean,
            val id: Int,
            val link: String,
            val niceDate: String,
            val origin: String,
            val projectLink: String,
            val publishTime: Long,
            val superChapterId: Int,
            val superChapterName: String,
            val tags: List<Tag>,
            val title: String,
            val type: Int,
            val userId: Int,
            val visible: Int,
            val zan: Int
        ) {

            data class Tag(
                val name: String,
                val url: String
            )
        }
    }
}