package chenrui.com.componentbase.empty_service;

import chenrui.com.componentbase.service.IAccountService;

/**
 * @Author:JIngYuchun
 * @Date:2018/7/31
 * @Description:
 */
public class EmptyAccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }
}
