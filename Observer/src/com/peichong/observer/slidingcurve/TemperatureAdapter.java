/**
 * 
 */
package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import com.peichong.observer.R;


import android.content.Context;
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
			viewhold.temperatures.setText(item.getTemperature().toString());
			viewhold.times.setText(item.getTime().toString());
		return convertView;
	}

	private class Viewhold{
		private TextView temperatures;
		private TextView times;
	}
}
