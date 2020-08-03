package com.xuliwei.accountbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import com.xuliwei.accountbook.adapter.BuildInfoAdapter;
import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;
import com.xuliwei.accountbook.util.StringTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuliwei on 2017/2/13.
 */

public class AllActivity extends BaseActivity implements BuildInfoAdapter.Callback,RadioGroup.OnCheckedChangeListener,View.OnClickListener{
    private RadioGroup radioGroup;
    private BuildInfoDao buildInfoDao;
    private List<BuildInfo> buildInfoList;
    private ListView listView;
    private BuildInfoAdapter adapter;
    private TextView datePickerStart;//开始时间
    private TextView datePickerEnd;//结束时间
    private Button calculateBtn;
    private TextView accountMoney;
    private CheckBox checkBoxAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_allaccount);
        radioGroup = (RadioGroup) findViewById(R.id.main_button_tabs);
        datePickerStart= (TextView) findViewById(R.id.startTime);
        datePickerEnd= (TextView) findViewById(R.id.endTime);
        calculateBtn = (Button) findViewById(R.id.calculateBtn);
        accountMoney = (TextView)findViewById(R.id.accountMoney);
        checkBoxAll = (CheckBox) findViewById(R.id.checkBoxAll);
        radioGroup.setOnCheckedChangeListener(this);
        buildInfoDao = new BuildInfoDao(this);
        buildInfoDao.openDataBase();
        Calendar c1 = Calendar.getInstance();
        datePickerStart.setText(c1.get(Calendar.YEAR)+"-"+ (c1.get(Calendar.MONTH)+1)+"-"+ c1.get(Calendar.DAY_OF_MONTH));
        datePickerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AllActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datePickerStart.setText(year+"-"+ (monthOfYear+1)+"-"+ dayOfMonth);

                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        datePickerEnd.setText(c1.get(Calendar.YEAR)+"-"+ (c1.get(Calendar.MONTH)+1)+"-"+ c1.get(Calendar.DAY_OF_MONTH));
        datePickerEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(AllActivity.this,
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datePickerEnd.setText(year+"-"+ (monthOfYear+1)+"-"+ dayOfMonth);

                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        calculateBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//accountMoney datePickerStart datePickerEnd
                        String startStr = (String) datePickerStart.getText();
                        String endStr = (String) datePickerEnd.getText();
                        int startInt = StringTool.splitByHorizontalLine(startStr);
                        int endInt = StringTool.splitByHorizontalLine(endStr);
                        if(startInt<=endInt){
                            List<BuildInfo> list = buildInfoDao.queryBuildInfoByTwoTime(startInt,endInt);
                            double count =0;
                            for(BuildInfo buildInfo:list){
                                double price = buildInfo.getPrice();
                                double size = buildInfo.getSize();
                                if(price==0||price==0){
                                    new AlertDialog.Builder(AllActivity.this).setTitle("系统提示")
                                            .setMessage("所选时间中含有未填写价格或时间的记录！")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                    count=0;
                                    break;
                                }
                                count +=price*size;
                            }
                            accountMoney.setText(count+"元");

                        }
                        else {
                            new AlertDialog.Builder(AllActivity.this).setTitle("系统提示")
                                    .setMessage("所选的日期不符合要求(比如：日期是汉字，起始日期大于结束日期)！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }

                    }
                }
        );
        checkBoxAll.setOnClickListener(this);
        accountMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(6);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildInfoList = buildInfoDao.queryBuildInfoList();
        Intent intent = getIntent();
        int checkedId =intent.getIntExtra("checkedId",0);
        radioGroup.check(checkedId);

        adapter = new BuildInfoAdapter(this,R.layout.account_item,buildInfoList,this);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

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

    @Override
    public void click(final View view) {
        switch (view.getId()){

            case R.id.item_deleteItem:

                new AlertDialog.Builder(AllActivity.this).setTitle("系统提示")
                        .setMessage("确定要删除这条数据吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i =(Integer)view.getTag();
                                buildInfoDao.removeItem(buildInfoList.get(i).getWriteTime());
                                Toast.makeText(AllActivity.this,buildInfoList.get(i).getWorkTime()+"的一条记录被删除",Toast.LENGTH_LONG).show();
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
                Intent intent =new Intent(AllActivity.this,ChangeAccountActivity.class);
                intent.putExtra("writetime",buildInfoList.get((Integer) view.getTag()).getWriteTime());
                startActivity(intent);
                break;

        }
    }

    @Override
    public void click(Map<Integer, BuildInfo> map) {
        double count =0;
        for (int key:map.keySet()){
            count+=map.get(key).getPrice()*map.get(key).getSize();
        }
        accountMoney.setText(count+"元");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        buildInfoDao.closeDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBoxAll:
                if (!checkBoxAll.isChecked()) {
                     adapter = new BuildInfoAdapter(this, R.layout.account_item, buildInfoList, this);
                     listView.setAdapter(adapter);
                    accountMoney.setText(0+"元");
                }else {
                   Map<Integer,Boolean> checkBoxMap = adapter.getCheckBoxMap();
                    Map<Integer,BuildInfo> buildInfoMapCheck = new HashMap<>();
                   Map<Integer,Boolean> buildInfoMap = new HashMap<>();
                    double count =0;
                    for(Integer key:checkBoxMap.keySet()){

                        if(checkBoxMap.get(key)){
                            buildInfoMap.put(key,true);
                            buildInfoMapCheck.put(key,buildInfoList.get(key));

                            count+=buildInfoMapCheck.get(key).getPrice()*buildInfoMapCheck.get(key).getSize();
                        }else if (!checkBoxMap.get(key)){
                            buildInfoMap.put(key,false);

                            buildInfoMapCheck.put(key,buildInfoList.get(key));
                        }
                    }
                    adapter.setBuildInfoMapCheck(buildInfoMapCheck);
                    adapter.setCheckBoxMap(checkBoxMap);
                    adapter.setBuildInfoMap(buildInfoMap);
                    adapter.notifyDataSetChanged();
                    accountMoney.setText(count+"元");
                }
                break;
        }

    }
}
