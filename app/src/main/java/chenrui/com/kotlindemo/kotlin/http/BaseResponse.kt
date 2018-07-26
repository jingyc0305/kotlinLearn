package chenrui.com.kotlindemo.kotlin.http

import java.io.Serializable

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:封装返回的基础数据
 */
open class BaseResponse<T>(open var errorCode: String, open var errorMsg: String, open var data: T) : Serializable