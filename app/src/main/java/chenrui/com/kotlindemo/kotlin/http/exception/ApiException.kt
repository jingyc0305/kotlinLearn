package chenrui.com.kotlindemo.kotlin.http.exception

/**
 * @Author:JIngYuchun
 * @Date:2018/7/30
 * @Description:自定义异常 根据服务器返回的状态码
 */
class ApiException:RuntimeException {

    private var code : Int?= null
    constructor(throwable: Throwable ,code:Int):super(throwable){
        this.code = code
    }
    constructor(message:String):super(Throwable(message))
}