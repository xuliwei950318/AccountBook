package com.xuliwei.accountbook.dao;

import java.util.ArrayList;
import java.util.List;

import com.xuliwei.accountbook.bean.BuildInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BuildInfoDao {
	private Context context;
	//private SQLiteDatabase sqLiteDatabase;
	private SQLiteDatabase sqLiteDatabase;
	private MyDatabaseHelper  dataBaseHelper;
	public BuildInfoDao(Context context){
		this.context=context;
	}
	//打开数据库连接，并且操作
	public void openDataBase(){
		dataBaseHelper = new MyDatabaseHelper(context, "AccountBook", null, 1);
		this.sqLiteDatabase = dataBaseHelper.getWritableDatabase();//获得当前可写的数据库对象


	}

	//关闭数据库
	public  void closeDB(){
		if(sqLiteDatabase!=null){
			sqLiteDatabase.close();
		}
	}

	public void insertBuildInfo(BuildInfo buildInfo){
		String sql = "insert into buildinfo(description,price,size,worktime,writetime,worktimeint) values(?,?,?,?,?,?)";
		sqLiteDatabase.execSQL(sql,new Object[]{buildInfo.getDescription(),buildInfo.getPrice(),buildInfo.getSize(),buildInfo.getWorkTime(),buildInfo.getWriteTime(),buildInfo.getWorkTimeInt()});

	}
	public void removeItem(String writetime){
		String sql = "delete from buildinfo where writetime ='"+writetime+"'";
		sqLiteDatabase.execSQL(sql);
	}
	public void removeAll(){
		String sql = "delete from buildinfo";
		sqLiteDatabase.execSQL(sql);
	}
	public List<BuildInfo> queryItemByWriteTime(String writetime){
		String sql = "select * from buildinfo where writetime like '"+writetime+"%' order by worktimeint";
		Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
		List<BuildInfo> list = new ArrayList<>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String description= cursor.getString(cursor.getColumnIndex("description"));
				double price = cursor.getDouble(cursor.getColumnIndex("price"));
				double size = cursor.getDouble(cursor.getColumnIndex("size"));
				String workTime= cursor.getString(cursor.getColumnIndex("worktime"));
				String writeTime= cursor.getString(cursor.getColumnIndex("writetime"));
				int workTimeInt = cursor.getInt(cursor.getColumnIndex("worktimeint"));
				BuildInfo buildInfo = new BuildInfo(description,price,size,workTime,writeTime,workTimeInt);
				list.add(buildInfo);
			} while (cursor.moveToNext());
		}
		return list;
	}
	public List<BuildInfo> queryBuildInfoByTwoTime(int startTime,int endTime){
		String sql = "select * from buildinfo where worktimeint between ? and ? order by worktimeint";
		Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{startTime+"",endTime+""});
		List<BuildInfo> list = new ArrayList<>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String description= cursor.getString(cursor.getColumnIndex("description"));
				double price = cursor.getDouble(cursor.getColumnIndex("price"));
				double size = cursor.getDouble(cursor.getColumnIndex("size"));
				String workTime= cursor.getString(cursor.getColumnIndex("worktime"));
				String writeTime= cursor.getString(cursor.getColumnIndex("writetime"));
				int workTimeInt = cursor.getInt(cursor.getColumnIndex("worktimeint"));
				BuildInfo buildInfo = new BuildInfo(description,price,size,workTime,writeTime,workTimeInt);
				list.add(buildInfo);
			} while (cursor.moveToNext());
		}
		return list;
	}


	public List<BuildInfo> queryBuildInfoList(){
		String sql = "select * from buildinfo order by worktimeint";
		Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
		List<BuildInfo> list = new ArrayList<>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String description= cursor.getString(cursor.getColumnIndex("description"));
				double price = cursor.getDouble(cursor.getColumnIndex("price"));
				double size = cursor.getDouble(cursor.getColumnIndex("size"));
				String workTime= cursor.getString(cursor.getColumnIndex("worktime"));
				String writeTime= cursor.getString(cursor.getColumnIndex("writetime"));
				int workTimeInt = cursor.getInt(cursor.getColumnIndex("worktimeint"));
				BuildInfo buildInfo = new BuildInfo(description,price,size,workTime,writeTime,workTimeInt);
				list.add(buildInfo);
			} while (cursor.moveToNext());
		}
		return list;
	}

	public BuildInfo queryBuildInfoByWritetime(String writetime) {
		String sql = "select * from buildinfo where writetime = '"+writetime+"' order by worktimeint";
		Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
		BuildInfo buildInfo = null;
		if (cursor.moveToFirst()) {

				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String description= cursor.getString(cursor.getColumnIndex("description"));
				double price = cursor.getDouble(cursor.getColumnIndex("price"));
				double size = cursor.getDouble(cursor.getColumnIndex("size"));
				String workTime= cursor.getString(cursor.getColumnIndex("worktime"));
				String writeTime= cursor.getString(cursor.getColumnIndex("writetime"));
			int workTimeInt = cursor.getInt(cursor.getColumnIndex("worktimeint"));
			buildInfo = new BuildInfo(description,price,size,workTime,writeTime,workTimeInt);
		}
		return buildInfo;
	}

	public void updateBuildInfo(BuildInfo buildInfo) {
		String sql = "update buildinfo set description= ?,price= ?,size = ?,worktime=?,worktimeint=? where writetime=?";
		sqLiteDatabase.execSQL(sql,new Object[]{buildInfo.getDescription(),buildInfo.getPrice(),buildInfo.getSize(),buildInfo.getWorkTime(),buildInfo.getWorkTimeInt(),buildInfo.getWriteTime()});

	}

}
