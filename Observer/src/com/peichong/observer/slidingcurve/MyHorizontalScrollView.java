package com.peichong.observer.slidingcurve;

import java.util.ArrayList;

import com.peichong.observer.R;



import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * TODO: 滑动
 * 
 * @author:	 wy
 * @version: V1.0
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

	private StudyGraphView studyGraphView;
	private ArrayList<PointF> graphPoints;

	private float showTextX;

	private float marginRight;
	private float marginLeft;
	private float scrollSpacing;
	private float screenWidth;

	public MyHorizontalScrollView(Context context,AttributeSet attrs) {
		super(context,attrs);
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);

		if (hasWindowFocus) {
			studyGraphView = (StudyGraphView) findViewById(R.id.study_graph);
			
			scrollSpacing=studyGraphView.getSpacingOfX();
			marginRight=scrollSpacing;
			marginLeft=scrollSpacing;
			
			graphPoints = studyGraphView.getPoints();
			
			showTextX=screenWidth-marginRight;
			
			this.scrollTo(studyGraphView.getWidth(), 0);
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		if(oldl==0){
			return ;
		}
		
		float scaleplateNow=l + showTextX;
		float scaleplatePre=oldl + showTextX;
		
		if (graphPoints != null) {
			for (int i = 0; i < graphPoints.size(); i++) {
				float pointX=graphPoints.get(i).x;
				if ((pointX>=scaleplateNow && pointX<=scaleplatePre)
						||(pointX>=scaleplatePre && pointX<=scaleplateNow)) {
					
					if(l>oldl){
						if(showTextX<=(screenWidth-marginRight) ){
							showTextX=		
								scrollSpacing*(float)(i+1)*((float)(StudyGraphView.SHOW_NUM)/(float)(graphPoints.size()-1));
						}
					}else{
						
						if(showTextX>=marginLeft ){
							showTextX=screenWidth
								-scrollSpacing*((float)(graphPoints.size()-1-i))*(((float)StudyGraphView.SHOW_NUM)/(float)(graphPoints.size()-1));	
						}
						
					}
					
					studyGraphView.setCurrentIndex(i);
					studyGraphView.invalidate();
					
					break;
				}
			}
		}

	}
}
