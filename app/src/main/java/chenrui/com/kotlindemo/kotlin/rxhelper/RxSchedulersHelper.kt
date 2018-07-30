package chenrui.com.kotlindemo.kotlin.rxhelper

object RxSchedulersHelper {
    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}