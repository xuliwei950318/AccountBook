package com.xuliwei.accountbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;
import com.xuliwei.accountbook.util.StringTool;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuliwei on 2017/2/12.
 */

public class ChangeAccountActivity extends Activity {
    private TextView datePickerBtn;//工作时间
    private BuildInfoDao buildInfoDao;
    private EditText description;//输入户型等详细信息

    private EditText size;//面积
    private EditText price;//单位价格
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildInfoDao = new BuildInfoDao(this);
        buildInfoDao.openDataBase();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_changeaccount);
        datePickerBtn= (TextView) findViewById(R.id.datePickerBtn);
        Button cancelbtn = (Button) findViewById(R.id.cancelbtn);
        Button okBtn = (Button) findViewById(R.id.addbtn);

        description = (EditText) findViewById(R.id.description);
        size = (EditText) findViewById(R.id.houseSize);
        price = (EditText) findViewById(R.id.price);

        Calendar c1 = Calendar.getInstance();
        datePickerBtn.setText(c1.get(Calendar.YEAR)+"-"+ (c1.get(Calendar.MONTH)+1)+"-"+ c1.get(Calendar.DAY_OF_MONTH));
        Intent intent = getIntent();
       final String writetime = intent.getStringExtra("writetime");
        final  BuildInfo buildInfo =buildInfoDao.queryBuildInfoByWritetime(writetime);
        description.setText(buildInfo.getDescription());
        size.setText(buildInfo.getSize()+"");
        price.setText(buildInfo.getPrice()+"");
        datePickerBtn.setText(buildInfo.getWorkTime());
        final String[] str = buildInfo.getWorkTime().split("-");

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(ChangeAccountActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datePickerBtn.setText(year+"-"+ (monthOfYear+1)+"-"+ dayOfMonth);

                            }
                        }
                        // 设置初始日期
                        , Integer.parseInt(str[0]),Integer.parseInt(str[1])-1, Integer.parseInt(str[2])).show();

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {

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
                BuildInfo buildInfo = new BuildInfo(description.getText().toString(),priceNow,sizeNow,datePickerBtn.getText().toString(),writetime,startInt);
                buildInfoDao.updateBuildInfo(buildInfo);
                Toast.makeText(ChangeAccountActivity.this,buildInfo.getWorkTime()+"的一条记录被修改",Toast.LENGTH_LONG).show();
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
