package com.peichong.observer.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;


/** 
 * TODO:   	Activity管理类
 * @author:   wy 
 * @version:  V1.0 
 */
public class ActivityUtil {
protected static List<Activity> activityStack = new LinkedList<Activity>();
	
	public static void addActivity(Activity activity){
		activityStack.add(activity);
	}
	
	public static void finishAll() {
		for(int i =activityStack.size()-1; i > -1; i -- ){
			Activity act = activityStack.get(i);
			act.finish();
		}		
		activityStack.clear();
	}
	
	public static void remove(Activity activity){
		if(activityStack.contains(activity))
			activityStack.remove(activity);
	}
}
