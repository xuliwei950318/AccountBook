package com.xuliwei.accountbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xuliwei.accountbook.adapter.BuildInfoAdapter;
import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;
import com.xuliwei.accountbook.util.StringTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by xuliwei on 2017/2/14.
 */

public class PersonalActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {

    private TextView clearData;
    private TextView share;
    private TextView setting;
    private BuildInfoDao buildInfoDao;
    private View clearDataLayout;
    private View shareLayout;
    private View settingLayout;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        buildInfoDao = new BuildInfoDao(this);
        buildInfoDao.openDataBase();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal);
        radioGroup = (RadioGroup) findViewById(R.id.main_button_tabs);
        clearData = (TextView) findViewById(R.id.clearData);
        share = (TextView) findViewById(R.id.share);
        setting = (TextView) findViewById(R.id.setting);


        clearDataLayout = findViewById(R.id.clearDataLayout);
        shareLayout = findViewById(R.id.shareLayout);
        settingLayout=findViewById(R.id.settingLayout);
        clearData.setOnClickListener(this);
        share.setOnClickListener(this);
        setting.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int checkedId =intent.getIntExtra("checkedId",0);
        radioGroup.check(checkedId);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = StringTool.getPath(this, uri);
                    if(path.endsWith("xuliwei.txt")) {
                        List<BuildInfo> list = StringTool.openFile(path);
                        List<BuildInfo> buildInfoList = buildInfoDao.queryBuildInfoList();
                        List<BuildInfo> listEnd = StringTool.combineList(buildInfoList, list);
                        for (BuildInfo buildInfo : listEnd) {
                            buildInfoDao.insertBuildInfo(buildInfo);
                        }
                        Toast.makeText(this, "新插入" + listEnd.size() + "条数据", Toast.LENGTH_SHORT).show();
                    }else {
                        new AlertDialog.Builder(PersonalActivity.this).setTitle("系统提示")
                                .setMessage("不是数据文件，请重选！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clearData:
               // clearDataLayout.setBackgroundResource(R.color.colorSkyBlue);
                break;
            case R.id.share:
               // shareLayout.setBackgroundResource(R.color.colorSkyBlue);
                List<BuildInfo> list =buildInfoDao.queryBuildInfoList();
                Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
                intent.setType("*/*"); // 分享发送的数据类型
                Calendar calendar = Calendar.getInstance();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),calendar.get(Calendar.YEAR)+"_"+ (calendar.get(Calendar.MONTH)+1)+"_"+ calendar.get(Calendar.DAY_OF_MONTH)+"xuliwei.txt");
                try {
                    if(!file.exists()){
                        file.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringTool.saveFile(file.getAbsolutePath(),list);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); // 分享的内容
                startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
                break;
            case R.id.setting:
               // settingLayout.setBackgroundResource(R.color.colorSkyBlue);
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("*/*");
                intent1.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult( Intent.createChooser(intent1, "选择数据文件（如2007_08_01xuliwei.txt）"), 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
