package com.xuliwei.accountbook.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xuliwei.accountbook.BuildConfig;
import com.xuliwei.accountbook.bean.BuildInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xuliwei on 2017/2/13.
 */

public class StringTool {
    /**
     * 根据 - 拼数字
     * @param str
     * @return
     */
    public static int splitByHorizontalLine(String str){
        String[] str_s = str.split("-");
        String strNow="";
        for(String string:str_s){
            strNow+=strBetween1_9(string);
        }
        int strToInt = Integer.parseInt(strNow);
        return  strToInt;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String strBetween1_9(String str){
        int num = Integer.parseInt(str);
        if (num>=1&&num<=9){
            return "0"+str;
        }
        return str;
    }

    public static int saveFile(String file,List<BuildInfo> list){
        ObjectOutputStream oos =  null;
        try {
            System.out.println(file);
            oos = new ObjectOutputStream(new FileOutputStream(file));
            Iterator<BuildInfo> it = list.iterator();
            while(it.hasNext()){
                BuildInfo buildInfo =  it.next();
                oos.writeObject(buildInfo);
     }
            oos.writeObject(null);
            oos.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if(oos != null){
                oos.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static List<BuildInfo> openFile(String file){

        ObjectInputStream ois = null;
        List<BuildInfo> list = new ArrayList<BuildInfo>();
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Object obj = null;
            while((obj= ois.readObject())!=null){
                BuildInfo buildInfo = (BuildInfo)obj;
                list.add(buildInfo);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            ois.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }
    public static List<BuildInfo> combineList(List<BuildInfo> firstList,List<BuildInfo> secondList){
        List<BuildInfo> list = new ArrayList<>();
        if(firstList.isEmpty()){
            return secondList;
        }

            for (BuildInfo buildInfoSecond : secondList) {
                int i = 0;
                for (BuildInfo buildInfoFirst : firstList) {
                    if (buildInfoSecond.getWriteTime().equals(buildInfoFirst.getWriteTime())) {
                        break;
                    }
                    if ((++i) == firstList.size()) {
                        list.add(buildInfoSecond);
                    }
                }
            }

        return list;
    }
   public static String[] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
}
