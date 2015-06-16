/**
 * 
 */
package com.peichong.observer.analysislog;

import java.util.List;
import java.util.Map;

import com.peichong.observer.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/** 		分享日志适配器
 * TODO:   
 * @author:   wy 
 * @version:  V1.0 
 */
public class AnalysisAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	
	  /**内容集合  */
    //private ArrayList<HashMap<String, Object>> mItemArrayList;

	public AnalysisAdapter(Context context, List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}
	
	/*public AnalysisAdapter(Context cx,ArrayList<HashMap<String, Object>> data){
		
		this.context = cx;
		this.inflater = LayoutInflater.from(context);
        this.mItemArrayList = data; 
	}*/

	@Override
	public int getCount() {

		return list.size();
		//return mItemArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
		//return mItemArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.activity_analysi_item, null);
			viewHolder = new ViewHolder();

			viewHolder.set_text = (TextView) convertView.findViewById(R.id.set_text);
			viewHolder.set_time = (TextView) convertView.findViewById(R.id.set_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.set_text.setText(list.get(position).get("title").toString());
		viewHolder.set_time.setText(list.get(position).get("time").toString());
		
		/*viewHolder.set_text.setText(mItemArrayList.get(position).get("title").toString());
		viewHolder.set_time.setText(mItemArrayList.get(position).get("time").toString());*/

		return convertView;
	}

	static class ViewHolder {
		public TextView set_text;
		public TextView set_time;
	}
}
