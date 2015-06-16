package com.peichong.observer.slidingcurve;

import java.util.ArrayList;
import java.util.List;

import com.peichong.observer.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/** 
 * TODO:     抽屉菜单适配器
 * @author:   wy 
 * @version:  V1.0 
 */
public class MenuAdapter extends BaseAdapter{
	private Context ctx;
	private List<MenuInfo> list;
	private LayoutInflater inflater;
	public int selectIndex;
	
	public MenuAdapter() {
		// TODO Auto-generated constructor stub
	}
	public MenuAdapter(Context context,List<MenuInfo> list){
		this.ctx=context;
		if(list!=null&&list.size()>0){
			this.list=list;
		}else{
			list=new ArrayList<MenuInfo>();
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
		MenuInfo item=list.get(position);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.activity_menu_item, null);
			viewhold=new Viewhold();
			viewhold.img=(ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewhold);
		}else{
			viewhold=(Viewhold) convertView.getTag();
		}
			viewhold.img.setImageDrawable(item.getUrl());
			
		return convertView;
	}
	private class Viewhold{
		private ImageView img;
	}
}
