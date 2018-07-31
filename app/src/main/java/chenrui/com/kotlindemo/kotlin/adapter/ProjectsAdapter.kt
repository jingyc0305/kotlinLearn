package chenrui.com.kotlindemo.kotlin.adapter

import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.app.MainApplication
import chenrui.com.kotlindemo.kotlin.bean.ProjectsBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author:JIngYuchun
 * @Date:2018/7/23
 * @Description:项目列表适配器
 */
class ProjectsAdapter(layoutResId: Int, data: MutableList<ProjectsBean.Data.Data>?) :
    BaseQuickAdapter<ProjectsBean.Data.Data, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: ProjectsBean.Data.Data) {

        helper.let {
            it.setText(R.id.home_project_title,item.title)
            it.setText(R.id.home_project_author,"作者: "+item?.author)
            it.setText(R.id.home_project_descripe,item.desc)
            it.setText(R.id.home_project_date,"时间: "+item?.niceDate)
            Glide.with(MainApplication.context)
                .load(item.envelopePic)
                .into(it.getView(R.id.home_project_img))
        }
    }
}