package chenrui.com.kotlindemo.kotlin.http.exception

/**
 * @Author:JIngYuchun
 * @Date:2018/7/30
 * @Description:异常状态码
 */
object ErrorCode {
    /**
     * 响应成功
     */
    @JvmField
    val SUCCESS = 0
    /**
     * 无网络
     */
    @JvmField
    val NO_NETWORK = 1001

    /**
     * 未知错误
     */
    @JvmField
    val UNKNOWN_ERROR = 1002

    /**
     * 服务器内部错误
     */
    @JvmField
    val SERVER_ERROR = 1003

    /**
     * 网络连接超时
     */
    @JvmField
    val NETWORK_ERROR = 1004

    /**
     * API解析异常（或者第三方数据结构更改）等其他异常
     */
    @JvmField
    val API_ERROR = 1005
}