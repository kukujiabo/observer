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


/** 		湿度适配器
 * TODO:   
 * @author:   wy 
 * @version:  V1.0 
 */
public class HumidityAdapter extends BaseAdapter{
	private Context ctx;
	private List<HumidityInfo> list;
	private LayoutInflater inflater;
	
	public HumidityAdapter() {
		// TODO Auto-generated constructor stub
	}
	public HumidityAdapter(Context context,List<HumidityInfo> list){
		this.ctx=context;
		if(list!=null&&list.size()>0){
			this.list=list;
		}else{
			list=new ArrayList<HumidityInfo>();
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
		HumidityInfo item=list.get(position);
		//if(convertView==null){
			convertView=inflater.inflate(R.layout.activity_humidity_item, null);
			viewhold=new Viewhold();
			viewhold.humiditys=(TextView)convertView.findViewById(R.id.humiditys);
			viewhold.times=(TextView)convertView.findViewById(R.id.times);
			convertView.setTag(viewhold);
		//}else{
			viewhold=(Viewhold) convertView.getTag();
		//}
			viewhold.humiditys.setText(item.getHumidity().toString());
			viewhold.times.setText(item.getTime().toString());
		return convertView;
	}

	private class Viewhold{
		private TextView humiditys;
		private TextView times;
	}
}
