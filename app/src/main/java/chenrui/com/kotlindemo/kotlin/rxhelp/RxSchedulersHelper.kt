package chenrui.com.kotlindemo.kotlin.rxhelp

object RxSchedulersHelper {
    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}