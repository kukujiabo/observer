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
			
			if (item.getHumidity()>=75 && item.getHumidity()<=76) {
				viewhold.humiditys.setTextColor(0xff72C3F2);
			}else if(item.getHumidity()>=77 && item.getHumidity()<=78){
				viewhold.humiditys.setTextColor(0xff83F2EC);
			}else if(item.getHumidity()>=79 && item.getHumidity()<=80){
				viewhold.humiditys.setTextColor(0xff75EA72);
			}else if(item.getHumidity()>=81 && item.getHumidity()<=82){
				viewhold.humiditys.setTextColor(0xffFAE064);
			}else if(item.getHumidity()>=83 && item.getHumidity()<=85){
				viewhold.humiditys.setTextColor(0xffFCC15A);
			}
			
			viewhold.humiditys.setText(item.getHumidity()+"");
			viewhold.times.setText(item.getTime().toString());
		return convertView;
	}

	private class Viewhold{
		private TextView humiditys;
		private TextView times;
	}
}
