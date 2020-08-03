package com.xuliwei.accountbook;

import java.util.Calendar;
import java.util.Date;

import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;
import com.xuliwei.accountbook.dao.MyDatabaseHelper;
import com.xuliwei.accountbook.util.StringTool;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class WriteAccountActivity extends Activity {
	private TextView datePickerBtn;//工作时间
	private BuildInfoDao buildInfoDao;
	private EditText description;//输入户型等详细信息

	private EditText size;//面积
	private EditText price;//单位价格


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setBtnOnClick();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_writeaccount);


		buildInfoDao = new BuildInfoDao(this);
		buildInfoDao.openDataBase();


		datePickerBtn= (TextView) findViewById(R.id.datePickerBtn);
		Button cancelbtn = (Button) findViewById(R.id.cancelbtn);
		Button okBtn = (Button) findViewById(R.id.addbtn);
		description = (EditText) findViewById(R.id.description);
		size = (EditText) findViewById(R.id.houseSize);
		price = (EditText) findViewById(R.id.price);

		Calendar c1 = Calendar.getInstance();
		datePickerBtn.setText(c1.get(Calendar.YEAR)+"-"+ (c1.get(Calendar.MONTH)+1)+"-"+ c1.get(Calendar.DAY_OF_MONTH));
		datePickerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
				// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
				new DatePickerDialog(WriteAccountActivity.this,
						// 绑定监听器
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
												  int monthOfYear, int dayOfMonth) {
								datePickerBtn.setText(year+"-"+ (monthOfYear+1)+"-"+ dayOfMonth);

							}
						}
						// 设置初始日期
						, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				String description, double price, double size,
//				String workTime, String writeTime
				Date date =new Date();
				String startStr = (String) datePickerBtn.getText();
				int startInt = StringTool.splitByHorizontalLine(startStr);
				double priceNow =0;
				double sizeNow =0;
				if(!price.getText().toString().isEmpty()){
					priceNow = Double.parseDouble(price.getText().toString());
				}
				if(!size.getText().toString().isEmpty()){
					sizeNow = Double.parseDouble(size.getText().toString());
				}
				BuildInfo buildInfo = new BuildInfo(description.getText().toString(),priceNow,sizeNow,datePickerBtn.getText().toString(),date.getYear()+""+date.getMonth()+""+date.getDay()+""+date.getHours()+""+date.getMinutes()+""+date.getSeconds(),startInt);
				buildInfoDao.insertBuildInfo(buildInfo);
				Toast.makeText(WriteAccountActivity.this,buildInfo.getWorkTime()+"的一条记录已增加",Toast.LENGTH_LONG).show();
				finish();

			}
		});

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		buildInfoDao.closeDB();
	}

}
