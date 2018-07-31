package chenrui.com.login;

import chenrui.com.componentbase.service.IAccountService;

/**
 * @Author:JIngYuchun
 * @Date:2018/7/31
 * @Description:
 */
public class AccountService implements IAccountService{
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return "login_id_123";
    }

}
