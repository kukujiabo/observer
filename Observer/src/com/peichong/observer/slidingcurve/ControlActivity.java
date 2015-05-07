package com.peichong.observer.slidingcurve;

import java.util.ArrayList;

import com.example.basicapp.R;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;


/** 
 * TODO:   滑动曲线图
 * @author:   wy 
 * @version:  V1.0 
 */
public class ControlActivity extends Activity{
	private MyHorizontalScrollView studyGraphLayout;
	private StudyGraphView studyGraph;
	private ArrayList<StudyGraphItem> studyGraphItems;
	private ArrayList<PointF> pointList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		initUi();
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initUi() {

		studyGraphLayout = (MyHorizontalScrollView) findViewById(R.id.study_graph_layout);
		studyGraph = (StudyGraphView) findViewById(R.id.study_graph);

		studyGraphItems = new ArrayList<StudyGraphItem>();
		studyGraphItems.add(new StudyGraphItem("00:00", 5));
		studyGraphItems.add(new StudyGraphItem("01:00", (float) 7.5));
		studyGraphItems.add(new StudyGraphItem("02:00", 10));
		studyGraphItems.add(new StudyGraphItem("03:00", (float) 15.5));
		studyGraphItems.add(new StudyGraphItem("04:00", 20));
		studyGraphItems.add(new StudyGraphItem("05:00", 17));
		studyGraphItems.add(new StudyGraphItem("06:00", 15));
		studyGraphItems.add(new StudyGraphItem("07:00", 13));
		studyGraphItems.add(new StudyGraphItem("08:00", 18));
		studyGraphItems.add(new StudyGraphItem("09:00", 11));
		studyGraphItems.add(new StudyGraphItem("10:00", 8));
		studyGraphItems.add(new StudyGraphItem("11:00", 5));
		studyGraphItems.add(new StudyGraphItem("12:00", 10));
		studyGraphItems.add(new StudyGraphItem("13:00", (float) 15.5));
		studyGraphItems.add(new StudyGraphItem("14:00", 8));
		studyGraphItems.add(new StudyGraphItem("15:00", 11));
		studyGraphItems.add(new StudyGraphItem("16:00", 10));
		studyGraphItems.add(new StudyGraphItem("17:00", 5));
		studyGraphItems.add(new StudyGraphItem("18:00", 8));
		studyGraphItems.add(new StudyGraphItem("19:00", 7));
		studyGraphItems.add(new StudyGraphItem("20:00", (float) 10.5));
		studyGraphItems.add(new StudyGraphItem("21:00", 5));
		studyGraphItems.add(new StudyGraphItem("22:00", 4));
		studyGraphItems.add(new StudyGraphItem("23:00", 17));
		studyGraphItems.add(new StudyGraphItem("24:00", 2));

		studyGraph.setData(studyGraphItems);

		pointList = studyGraph.getPoints();

	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	public interface OnScrollListerner {
		void optionScrollEvent(int currentIndex);
	}
}
