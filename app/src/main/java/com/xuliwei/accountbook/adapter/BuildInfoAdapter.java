package com.xuliwei.accountbook.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xuliwei.accountbook.MainActivity;
import com.xuliwei.accountbook.R;
import com.xuliwei.accountbook.bean.BuildInfo;
import com.xuliwei.accountbook.dao.BuildInfoDao;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class BuildInfoAdapter extends ArrayAdapter<BuildInfo>{
	private Callback callback;
	private int resourceId;
    private Context context;
    private BuildInfoDao buildInfoDao;
	private List<BuildInfo> buildInfoList;
	private Map<Integer,BuildInfo> buildInfoMapCheck;
	private  Map<Integer,Boolean> buildInfoMap;
	private Map<Integer,Boolean> checkBoxMap;

	public interface Callback {
		public void click(View view);
		public void click(Map<Integer,BuildInfo> map);
	}
	public BuildInfoAdapter(Context context,
							int textViewResourceId, List<BuildInfo> objects,Callback callback) {
		super(context,  textViewResourceId, objects);
		this.callback=callback;
		resourceId=textViewResourceId;
        this.context=context;
		buildInfoList=objects;
        buildInfoDao = new BuildInfoDao(context);
		buildInfoDao.openDataBase();
		buildInfoMap =new  HashMap<>();
		buildInfoMapCheck = new  HashMap<>();
		checkBoxMap = new HashMap<>();
		// 初始化数据  
		initDate();
	}

	private void initDate() {
		for (int i = 0; i < buildInfoList.size(); i++) {
			getBuildInfoMap().put(i, false);
			if(buildInfoList.get(i).getPrice()==0||buildInfoList.get(i).getSize()==0) {

				checkBoxMap.put(i,false);
			}else{

				checkBoxMap.put(i,true);
			}
		}

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final BuildInfo buildInfo = buildInfoList.get(position);//获取当前项的Fruit实例
	   final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.account_item,null);
			viewHolder.description = (TextView) convertView.findViewById(R.id.item_description);
			viewHolder.worktime = (TextView) convertView.findViewById(R.id.item_worktime);
			viewHolder.price  = (TextView) convertView.findViewById(R.id.item_price);
			viewHolder.size  = (TextView) convertView.findViewById(R.id.item_size);
			viewHolder.item_deleteItem = (Button) convertView.findViewById(R.id.item_deleteItem);
			viewHolder.item_change = (Button) convertView.findViewById(R.id.item_change);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.item_deleteItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
              callback.click(v);
			}
		});
		viewHolder.item_change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.click(v);
			}
		});
		viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (buildInfoMap.get(position)) {
					buildInfoMap.put(position, false);
					setBuildInfoMap(buildInfoMap);
					buildInfoMapCheck.remove(position);
				} else {
					buildInfoMap.put(position, true);
					setBuildInfoMap(buildInfoMap);
					buildInfoMapCheck.put(position,buildInfo);
				}


				callback.click(buildInfoMapCheck);
			}
		});
		if(buildInfoList.get(position).getPrice()==0||buildInfoList.get(position).getSize()==0) {
			viewHolder.checkBox.setEnabled(false);
			viewHolder.checkBox.setBackgroundResource(R.drawable.fork);
			checkBoxMap.put(position,false);
		}else{
			viewHolder.checkBox.setEnabled(true);
			viewHolder.checkBox.setBackgroundResource(R.drawable.tran);
			checkBoxMap.put(position,true);
		}

		viewHolder.description.setText(buildInfo.getDescription());
		viewHolder.worktime.setText(buildInfo.getWorkTime());
		viewHolder.price.setText(String.valueOf("价格："+buildInfo.getPrice()));
		viewHolder.size.setText(String.valueOf("面积："+buildInfo.getSize()));
		viewHolder.item_deleteItem.setTag(position);
		viewHolder.item_change.setTag(position);
		viewHolder.checkBox.setChecked(getBuildInfoMap().get(position));

		return convertView;
	}

	public  void setBuildInfoMap(Map<Integer, Boolean> buildInfoMap) {
		this.buildInfoMap = buildInfoMap;
	}

	public void setBuildInfoList(List<BuildInfo> buildInfoList) {
		this.buildInfoList = buildInfoList;
	}

	public List<BuildInfo> getBuildInfoList() {
		return buildInfoList;
	}

	public Map<Integer, BuildInfo> getBuildInfoMapCheck() {
		return buildInfoMapCheck;
	}

	public void setBuildInfoMapCheck(Map<Integer, BuildInfo> buildInfoMapCheck) {
		this.buildInfoMapCheck = buildInfoMapCheck;
	}

	public void setCheckBoxMap(Map<Integer, Boolean> checkBoxMap) {
		this.checkBoxMap = checkBoxMap;
	}

	public Map<Integer, Boolean> getCheckBoxMap() {
		return checkBoxMap;
	}

	public  Map<Integer, Boolean> getBuildInfoMap() {
		return buildInfoMap;
	}
	class ViewHolder{
		private Button item_deleteItem ;
		private Button item_change;
		private TextView description;
		private CheckBox checkBox;
		private TextView worktime;
		private TextView price;
		private TextView size;
	}
}
