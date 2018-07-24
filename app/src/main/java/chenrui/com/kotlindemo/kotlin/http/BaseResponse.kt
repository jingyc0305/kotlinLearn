package chenrui.com.kotlindemo.kotlin.http

import java.io.Serializable

/**
 * @Author:JIngYuchun
 * @Date:2018/7/22
 * @Description:封装返回的基础数据
 */
open class BaseResponse<T>(val errorCode: String, val errorMsg: String, val data: T) : Serializable{
}