/**
 * 
 */
package com.peichong.observer.warning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.peichong.observer.R;
import com.peichong.observer.tools.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/** 
 * TODO:   	警告适配器
 * @author:   wy 
 * @version:  V1.0 
 */
public class WarningAdapter extends BaseAdapter{
	private Context context;
	  /**内容集合  */
    private ArrayList<HashMap<String, Object>> mItemArrayList;
    
	private LayoutInflater inflater;
	
	private String type;
	
	private String up_down;
	
	public WarningAdapter() {
		// TODO Auto-generated constructor stub
	}
	public WarningAdapter(Context cx,ArrayList<HashMap<String, Object>> data){
		
		this.context = cx;
		this.inflater = LayoutInflater.from(context);
        this.mItemArrayList = data; 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItemArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItemArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewhold viewhold=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.activity_warning_item, null);
			viewhold=new Viewhold();
			viewhold.temhum=(TextView)convertView.findViewById(R.id.temhum);
			viewhold.time=(TextView)convertView.findViewById(R.id.time_two);
			viewhold.temhum_set=(TextView)convertView.findViewById(R.id.temhum_set);
			convertView.setTag(viewhold);
		}else{
			viewhold=(Viewhold) convertView.getTag();
		}
		
			type= (String) mItemArrayList.get(position).get("type");
			up_down=(String) mItemArrayList.get(position).get("up_down");
			
			
			
			String s=(String) mItemArrayList.get(position).get("created_at");
			String ss = "";
			
			try {
				ss = Utils.dateTwo(s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (type.equals("0")) {
				if (up_down.equals("0")) {
					viewhold.temhum.setText(R.string.tem_low);
					viewhold.temhum_set.setText((String) mItemArrayList.get(position).get("w_data")+"°C");
					//viewhold.time.setText(ss);
				}else{
					viewhold.temhum.setText(R.string.tem_high);
					viewhold.temhum_set.setText((String) mItemArrayList.get(position).get("w_data")+"°C");
					//viewhold.time.setText(ss);
				}
			}else if(type.equals("1")){
				if (up_down.equals("0")) {
					viewhold.temhum.setText(R.string.hum_low);
					viewhold.temhum_set.setText((String) mItemArrayList.get(position).get("w_data")+"%");
					//viewhold.time.setText(ss);
				}else{
					viewhold.temhum.setText(R.string.hum_high);
					viewhold.temhum_set.setText((String) mItemArrayList.get(position).get("w_data")+"%");
					//viewhold.time.setText(ss);
				}
			}
			
		//viewhold.temhum_set.setText((String) mItemArrayList.get(position).get("w_data"));
		viewhold.time.setText(ss);
		
		return convertView;
	}
	private class Viewhold{
		private TextView  temhum;
		private TextView time;
		private TextView temhum_set;
	}
	
}
