package com.xuliwei.accountbook.util;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuliwei on 2017/2/15.
 */

public class SysApplication extends Application{
    private List<Activity> mList = new LinkedList<>();
    private static  SysApplication instance;
    private SysApplication(){}
    public synchronized static  SysApplication getInstance(){
        if(instance==null){
            instance=new SysApplication();
        }
        return instance;
    }
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
