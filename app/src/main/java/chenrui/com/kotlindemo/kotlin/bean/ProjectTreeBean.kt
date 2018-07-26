package chenrui.com.kotlindemo.kotlin.bean

/**
 * @Author:JIngYuchun
 * @Date:2018/7/26
 * @Description:
 */

data class ProjectTreeBean(
    val data: MutableList<Data>,
    val errorCode: Int,
    val errorMsg: String
) {

    data class Data(
        val children: List<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val visible: Int
    )
}