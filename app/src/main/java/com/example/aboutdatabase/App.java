package com.example.aboutdatabase;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.litepal.LitePal;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.facebook.stetho.Stetho.defaultDumperPluginsProvider;
import static com.facebook.stetho.Stetho.defaultInspectorModulesProvider;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //LitePal初始化
        LitePal.initialize(this);
        LitePal.getDatabase();

        //Realm初始化
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("myRealm.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        //Stetho初始化
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(defaultInspectorModulesProvider(this))
                        .build()
        );
    }
}
