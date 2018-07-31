package chenrui.com.login;

import android.app.Application;

import chenrui.com.baselib.BaseApp;
import chenrui.com.componentbase.ServiceFactory;

/**
 * @Author:JIngYuchun
 * @Date:2018/7/31
 * @Description:
 */
public class LoginApp extends BaseApp{
    @Override
    public void onCreate() {
        super.onCreate();
        initModuleApp(this);
        initModuleData(this);
    }

    @Override
    public void initModuleApp(Application application) {
        ServiceFactory.getInstance().setAccountService(new AccountService());
    }

    @Override
    public void initModuleData(Application application) {

    }
}
