package com.peichong.observer.slidingcurve;

import java.util.ArrayList;

import com.example.basicapp.R;
import com.peichong.observer.tools.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * TODO: 曲线图
 * 
 * @author:  wy
 * @version: V1.0
 */
public class StudyGraphView extends View {

	//public static final float INTERVAL_X_DP=45f; //x坐标的单元间距
	/**y坐标总的高度*/
	public static final float TOTAL_Y_DP = 250f; 

	/**坐标系距离顶部的高度*/
	public static final float COORDINATE_MARGIN_TOP_DP = 30f; 
	// public static final float COORDINATE_MARGIN_LEFT_DP = INTERVAL_X_DP;
	// 坐标系距离左边的间距
	// public static final float COORDINATE_MARGIN_RIGHT_DP = INTERVAL_X_DP;
	/**坐标系距离上边的间距*/
	public static final float COORDINATE_MARGIN_BOTTOM_DP = 30f;

	/**X轴分成几份*/
	public static final int SHOW_NUM = 6;



	/**X轴长度（只是数据的宽度，不包括左边和右边的留空）*/
	private float mTotalWidth; 
	/**Y轴的长度（只是数据的高度，不包括上方和下方的留空）*/
	private float mTotalHeight; 
	/**x坐标的单元间距(px)*/
	private float spacingOfX; 
	/**y坐标的单元间距(px)*/
	private float spacingOfY; 
	/**坐标系距离顶部的距离(px)*/
	private float coordinateMarginTop; 
	/**坐标系距离左边的距离(px)*/
	private float coordinateMarginLeft; 
	/**坐标系距离右边的距离(px)*/
	private float coordinateMarginRight; 
	/**坐标系距离下方的高度(px)*/
	private float coordinateMarginBottom; 

	/**曲线交点的小图标*/
	private Bitmap mPointImage; 
	/**上方文字说明背景*/
	private Bitmap mLevelShowImage; 

	/**各个点*/
	private ArrayList<PointF> points; 
	private ArrayList<StudyGraphItem> studyGraphItems;

	private StudyGraphItem maxEnergy;

	private Context mContext;

	private int currentIndex;

	public StudyGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		initParam();
		
	}

	/**
	 * TODO :初始化
	 * 
	 * @throw
	 * @return :void
	 */
	private void initParam() {
		// x坐标间隔为定值，屏幕不够就左右滑动
		// spacingOfX = Utils.dip2px(mContext, INTERVAL_X_DP);

		//Y坐标总高度设为定值，单位间距动态计算
		mTotalHeight = Utils.dip2px(mContext, TOTAL_Y_DP);

		//X坐标的间距
		spacingOfX = getResources().getDisplayMetrics().widthPixels / SHOW_NUM;

		coordinateMarginTop = Utils.dip2px(mContext, COORDINATE_MARGIN_TOP_DP);
		coordinateMarginLeft = spacingOfX;
		coordinateMarginRight = spacingOfX;
		coordinateMarginBottom = Utils.dip2px(mContext,
				COORDINATE_MARGIN_BOTTOM_DP);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.graph_separate));
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setTextSize(Utils.sp2px(mContext, 13));

		//绘制底部的横线、文字、以及向上的线条 
		canvas.drawLine(coordinateMarginLeft, mTotalHeight
				+ coordinateMarginTop, mTotalWidth + coordinateMarginLeft
				+ coordinateMarginRight, mTotalHeight + coordinateMarginTop,
				paint);
		for (int i = 0; i < studyGraphItems.size(); i++) {
			StudyGraphItem energy = studyGraphItems.get(i);
			PointF textPoint = points.get(i);

			// 绘制底部 到上面的线
			if (i == currentIndex) {
				paint.setStrokeWidth(15);
			} else {
				paint.setStrokeWidth(1);
			}
			paint.setColor(getResources().getColor(R.color.graph_separate));
			canvas.drawLine(textPoint.x, mTotalHeight + coordinateMarginTop,
					textPoint.x, coordinateMarginTop+3, paint);

			if (i == currentIndex) {
				// 画上方的文字说明
				canvas.drawBitmap(mLevelShowImage, textPoint.x
						- mLevelShowImage.getWidth() / 2, coordinateMarginTop
						- mLevelShowImage.getHeight() / 2, paint);

				paint.setStrokeWidth(1);
				paint.setTextAlign(Align.CENTER);
				canvas.drawText(
						studyGraphItems.get(i).temperature
								+ getResources().getString(R.string.temperature_unit),
						textPoint.x,
						coordinateMarginTop - mLevelShowImage.getHeight() / 2
								+ Utils.dip2px(mContext, 3)
								+ Utils.sp2px(mContext, 13), paint);
			}
			paint.setStrokeWidth(1);
			paint.setColor(getResources().getColor(R.color.graph_text));
			// 绘制底部的 文字
			canvas.drawText(
					energy.date,
					textPoint.x - Utils.sp2px(mContext, 10),
					mTotalHeight+ coordinateMarginTop
							+ Utils.sp2px(mContext, 13 + 5), paint);

		}
		Paint pathPaint = new Paint();
		pathPaint.setColor(getResources().getColor(R.color.graph_fill));
		pathPaint.setAlpha(51);
		//裁剪出一个需要的矩阵图 
		Path path = new Path();
		PointF point = null;
		// 原点
		path.moveTo(coordinateMarginLeft, mTotalHeight + coordinateMarginTop); 
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			path.lineTo(point.x, point.y);
		}
		path.lineTo(point.x, mTotalHeight + coordinateMarginTop);
		path.close();

		canvas.drawPath(path, pathPaint);

		int halfPointImageWidth = mPointImage.getWidth() / 2;
		int halfPointImageHeight = mPointImage.getHeight() / 2;
		
		//绘制曲线 覆盖 剪切后的锯齿
		for (int i = 0; i < points.size(); i++) {
			paint.setStrokeWidth(6);
			paint.setColor(getResources().getColor(R.color.graph_fill));
			PointF startPoint = points.get(i);

			if (i + 1 == points.size()) {
				// 绘制最后一个圆点到底部的 竖线
				// paint.setStrokeWidth(1);
				// canvas.drawLine(startPoint.x, startPoint.y, startPoint.x,
				// mTotalHeight + coordinateMarginTop, paint);
			} else {
				PointF endPoint = points.get(i + 1);
				// 绘制曲线，并且覆盖剪切后的锯齿
				canvas.drawLine(startPoint.x, startPoint.y, endPoint.x,
						endPoint.y, paint);
				
			}

			// 绘制节点小icon
			canvas.drawBitmap(mPointImage, startPoint.x - halfPointImageWidth,
					startPoint.y - halfPointImageHeight, paint);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				(int) (mTotalWidth + coordinateMarginLeft + coordinateMarginRight),
				(int) (mTotalHeight + coordinateMarginTop + coordinateMarginBottom));
	}

	/**
	 * 设置数据(初始化进行)
	 */
	public void setData(ArrayList<StudyGraphItem> studyGraphItems) {

		this.studyGraphItems = studyGraphItems;
		maxEnergy = findMaxPowers(studyGraphItems);

		mTotalWidth = (studyGraphItems.size() - 1) * spacingOfX;

		// y坐标间隔是根据用户的最大值动态计算的
		spacingOfY = (mTotalHeight) / maxEnergy.temperature;

		points = new ArrayList<PointF>();
		for (int i = 0; i < studyGraphItems.size(); i++) {
			studyGraphItems.get(i);
			float f = studyGraphItems.get(i).temperature;
			float y = mTotalHeight + coordinateMarginTop - f * spacingOfY;
			float x = (i * spacingOfX + coordinateMarginLeft);
			PointF point = new PointF(x, y);
			points.add(point);
		}

		mPointImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_circle);
		mLevelShowImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.study_graph_level_show);

		currentIndex = studyGraphItems.size() - 1;
	}

	/**
	 * 找到 数据集合中 最高能量 对应的脚标
	 * 
	 * @param powers
	 * @return
	 */
	private static StudyGraphItem findMaxPowers(
			ArrayList<StudyGraphItem> energys) {
		StudyGraphItem energy = new StudyGraphItem();
		energy.temperature = 0;
		for (int i = 0; i < energys.size(); i++) {
			if (energys.get(i).temperature > energy.temperature) {
				energy = energys.get(i);
			}
		}
		return energy;
	}

	public float getSpacingOfX() {
		return spacingOfX;
	}

	public ArrayList<PointF> getPoints() {
		return points;
	}

	public ArrayList<StudyGraphItem> getStudyGraphItems() {
		return studyGraphItems;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}
}
