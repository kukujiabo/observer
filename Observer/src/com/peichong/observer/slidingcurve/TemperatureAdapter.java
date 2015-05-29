/**
 * 
 */
package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import com.peichong.observer.R;
import com.peichong.observer.R.drawable;


import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



/** 	
 * TODO:   	 温度滚动栏适配器
 * @author:   wy 
 * @version:  V1.0 
 */
public class TemperatureAdapter extends BaseAdapter{
	private Context ctx;
	private List<TemperatureInfo> list;
	private LayoutInflater inflater;
	
	public TemperatureAdapter() {
		// TODO Auto-generated constructor stub
	}
	public TemperatureAdapter(Context context,List<TemperatureInfo> list){
		this.ctx=context;
		if(list!=null&&list.size()>0){
			this.list=list;
		}else{
			list=new ArrayList<TemperatureInfo>();
		}
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewhold viewhold=null;
		TemperatureInfo item=list.get(position);
		//if(convertView==null){
			convertView=inflater.inflate(R.layout.activity_temperature_item, null);
			viewhold=new Viewhold();
			viewhold.temperatures=(TextView)convertView.findViewById(R.id.temperatures);
			viewhold.times=(TextView)convertView.findViewById(R.id.times);
			convertView.setTag(viewhold);
		//}else{
			viewhold=(Viewhold) convertView.getTag();
		//}
			
			if (item.getTemperature()>=15 && item.getTemperature()<=16) {
				viewhold.temperatures.setTextColor(0xff72C3F2);
			}else if(item.getTemperature()>=17 && item.getTemperature()<=18){
				viewhold.temperatures.setTextColor(0xff83F2EC);
			}else if(item.getTemperature()>=19 && item.getTemperature()<=24){
				viewhold.temperatures.setTextColor(0xff75EA72);
			}else if(item.getTemperature()>=25 && item.getTemperature()<=26){
				viewhold.temperatures.setTextColor(0xffFAE064);
			}else if(item.getTemperature()>=27 && item.getTemperature()<=28){
				viewhold.temperatures.setTextColor(0xffFCC15A);
			}
			
			viewhold.temperatures.setText(item.getTemperature()+"");
			viewhold.times.setText(item.getTime().toString());
		return convertView;
	}

	private class Viewhold{
		private TextView temperatures;
		private TextView times;
	}
}
