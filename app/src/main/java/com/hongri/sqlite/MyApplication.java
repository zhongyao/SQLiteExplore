package com.hongri.sqlite;

import android.app.Application;
import android.content.Context;
import com.hongri.sqlite.base.IServiceFactory;
import com.hongri.sqlite.base.Services;
import com.hongri.sqlite.dao.IDaoService;
import com.hongri.sqlite.dao.DaoServiceImpl;

/**
 * @author zhongyao
 * @date 2019/4/17
 */

public class MyApplication extends Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

        Services.setServiceFactory(new IServiceFactory() {
            @Override
            public <T> T createService(Class<T> clazz) {
                return getService(clazz);
            }
        });
    }

    private static <T> T getService(Class<T> clazz) {
        if (clazz == IDaoService.class) {
            return (T)new DaoServiceImpl(mAppContext);
        }
        return null;
    }

    private Context getAppContext() {
        if (mAppContext != null) {
            return mAppContext;
        }
        return null;
    }
}
