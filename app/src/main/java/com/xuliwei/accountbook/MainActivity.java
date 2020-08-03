package com.xuliwei.accountbook;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xuliwei.accountbook.adapter.BuildInfoAdapter;
import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;
import com.xuliwei.accountbook.util.StringTool;
import com.xuliwei.accountbook.util.SysApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends BaseActivity implements BuildInfoAdapter.Callback,RadioGroup.OnCheckedChangeListener{
	 private BuildInfoDao buildInfoDao;
	 private List<BuildInfo> buildInfoList;
     private RadioGroup radioGroup;
	private ListView listView;
	private BuildInfoAdapter adapter;
	private Button accountBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
      // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		buildInfoDao = new BuildInfoDao(this);
		buildInfoDao.openDataBase();
		radioGroup = (RadioGroup) findViewById(R.id.main_button_tabs);
		radioGroup.setOnCheckedChangeListener(this);
		accountBtn = (Button) findViewById(R.id.accountBtn);
	    Calendar c = Calendar.getInstance();
	    TextView textView1 = (TextView) findViewById(R.id.textView1);
		boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
//获取周几
		int weekDay = c.get(Calendar.DAY_OF_WEEK);
//若一周第一天为星期天，则-1
		if(isFirstSunday){
			weekDay = weekDay - 1;
			if(weekDay == 0){
				weekDay = 7;
			}
		}
	    textView1.setText("今天的时间是"+c.get(Calendar.YEAR)+"年"+ (c.get(Calendar.MONTH)+1)+"月"+ c.get(Calendar.DAY_OF_MONTH)+"日,"+ StringTool.week[weekDay-1]);
	    accountBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,WriteAccountActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Date date = new Date();
		buildInfoList = buildInfoDao.queryItemByWriteTime(date.getYear()+""+date.getMonth()+""+date.getDay());
		if(!buildInfoList.isEmpty())
		{
			View layout =  findViewById(R.id.lineback);
		    layout.setBackgroundResource(R.drawable.bg_2);
	}
	Intent intent = getIntent();
	int checkedId =intent.getIntExtra("checkedId",0);
	radioGroup.check(checkedId);

	adapter = new BuildInfoAdapter(this,R.layout.account_item,buildInfoList,this);
	listView = (ListView) findViewById(R.id.list_view);
	listView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		buildInfoDao.closeDB();
	}



	@Override
	public void click(final  View view) {
		switch (view.getId()){
			case R.id.item_deleteItem:

				new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")
						.setMessage("确定要删除这条数据吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								int i =(Integer)view.getTag();
								buildInfoDao.removeItem(buildInfoList.get(i).getWriteTime());
								Toast.makeText(MainActivity.this,buildInfoList.get(i).getWorkTime()+"的一条记录被删除",Toast.LENGTH_LONG).show();
								buildInfoList.remove(i);
								adapter.notifyDataSetChanged();
							}
						})
						.setNeutralButton("取消",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			}).show();

				break;
			case R.id.item_change:
				Intent intent =new Intent(MainActivity.this,ChangeAccountActivity.class);
				intent.putExtra("writetime",buildInfoList.get((Integer) view.getTag()).getWriteTime());
				startActivity(intent);
				break;
		}


	}


	@Override
	public void click(Map<Integer, BuildInfo> map) {

	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId){
			case R.id.main_home:
				Intent intent = new  Intent(this,MainActivity.class);
				intent.putExtra("checkedId",checkedId);
				startActivity(intent);
				break;
			case R.id.main_all:
				Intent intent1 = new  Intent(this,AllActivity.class);
				intent1.putExtra("checkedId",checkedId);
				startActivity(intent1);

				break;
			case R.id.main_more:
				Intent intent2 = new  Intent(this,PersonalActivity.class);
				intent2.putExtra("checkedId",checkedId);
				startActivity(intent2);

				break;
		}
	}
}
