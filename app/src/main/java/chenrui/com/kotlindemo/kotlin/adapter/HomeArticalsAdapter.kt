package chenrui.com.kotlindemo.kotlin.adapter

import chenrui.com.kotlindemo.R
import chenrui.com.kotlindemo.kotlin.bean.HomeArticalBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @Author:JIngYuchun
 * @Date:2018/7/23
 * @Description:首页文章列表适配器
 */
class HomeArticalsAdapter(layoutResId: Int, data: MutableList<HomeArticalBean.Data.DataBean>?) :
    BaseQuickAdapter<HomeArticalBean.Data.DataBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: HomeArticalBean.Data.DataBean) {

        helper.let {
            it.setText(R.id.home_artical_title,item.title)
            it.setText(R.id.home_artical_author,"作者: "+item.author)
            it.setText(R.id.home_artical_category,"分类: "+item.superChapterName)
            it.setText(R.id.home_artical_date,"时间: "+item.niceDate)
        }
    }
}