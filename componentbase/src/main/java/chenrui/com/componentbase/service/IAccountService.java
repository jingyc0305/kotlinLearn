package chenrui.com.componentbase.service;

/**
 * @Author:JIngYuchun
 * @Date:2018/7/31
 * @Description:
 */
public interface IAccountService {
    /**
     * 是否已经登录
     * @return
     */
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     * @return
     */
    String getAccountId();
}
