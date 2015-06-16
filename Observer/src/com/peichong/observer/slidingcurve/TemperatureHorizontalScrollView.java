/**
 * 
 */
package com.peichong.observer.slidingcurve;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.peichong.observer.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * TODO: 滚动视图
 * 
 * @author: wy
 * @version: V1.0
 */
public class TemperatureHorizontalScrollView extends HorizontalScrollView
		implements OnClickListener {

	private FrameLayout mFrameLayout;
	private BaseAdapter mBaseAdapter;
	private SparseArray<View> mSparseArray;
	private int oldPosition;
	private TextView temperatures;
	private TextView tTimes;
	private TextView humiditys;
	private TextView hTimes;
	private RelativeLayout rFrameLayout;
	// private ImageView preImage, nextImage;
	private OnItemClickListener onItemClickListener;
	private OnItemResetListener onItemResetListener;
	public Handler _scrollHandler;
	
	private EdgeEffect mEdgeGlowTop;
	private EdgeEffect mEdgeGlowBottom;

	public TemperatureHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TemperatureHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TemperatureHorizontalScrollView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		// resetImageView();
	}

	public int getScrollRange() {
		return computeHorizontalScrollRange();
	}

	public int getScrollExtent() {
		return computeHorizontalScrollExtent();
	}

	public int getScrollOffset() {
		return computeHorizontalScrollOffset();
	}

	/*
	 * private void resetImageView() { // 计算水平方向滚动条的滑块的偏移值。 int ScrollOffset =
	 * computeHorizontalScrollOffset(); // 滚动条长度 int ScrollExtent =
	 * computeHorizontalScrollExtent(); // 滚动条当前位置 int curScrollLoc =
	 * ScrollOffset + ScrollExtent; // scrollView 的可滚动水平范围是所有子视图的宽度总合。 int
	 * ScrollRange = computeHorizontalScrollRange(); // 如果当前位置 在ScrollExtent 和
	 * ScrollRange 之间,左右两边的View都显示 if (curScrollLoc > ScrollExtent &&
	 * curScrollLoc < ScrollRange) { if (preImage != null)
	 * preImage.setVisibility(View.VISIBLE); if (nextImage != null)
	 * nextImage.setVisibility(View.VISIBLE); return; } // 如果滚动到最左边 if
	 * (curScrollLoc == ScrollExtent) { if (preImage != null)
	 * preImage.setVisibility(View.INVISIBLE); return; } // 如果滚动到最右边 if
	 * (curScrollLoc >= ScrollRange) { if (nextImage != null)
	 * nextImage.setVisibility(View.INVISIBLE); return; }
	 * 
	 * }
	 */

	private void init() {
		mFrameLayout = new FrameLayout(getContext());
		mFrameLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		addView(mFrameLayout);
		mSparseArray = new SparseArray<View>();
		mFrameLayout.setFadingEdgeLength(0);
	}

	private LinearLayout linearLayout;
	private int count, oldcount, aveWidth;

	@SuppressLint("NewApi")
	private void buildItemView() {
		if (mBaseAdapter == null)
			return;
		if (linearLayout == null)
			linearLayout = new LinearLayout(getContext());
		else
			linearLayout.removeAllViews();
		for (int i = 0; i < (count = mBaseAdapter.getCount()); i++) {
			View view = mBaseAdapter.getView(i, mSparseArray.get(i), this);
			view.setOnClickListener(this);
			mSparseArray.put(i, view);
			linearLayout.addView(mSparseArray.get(i));
		}
		linearLayout.setFadingEdgeLength(0);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		layoutParams.gravity = Gravity.CENTER_VERTICAL;

		// 获取控制台类型默认为温度显示图（1 温度显示图 ---- 2湿度显示图）
		if (ControlActivity.chooseType == 1) {
			rFrameLayout = (RelativeLayout) LayoutInflater.from(getContext())
					.inflate(R.layout.activity_temperature_item, null);
			// temperatures = (TextView) rFrameLayout
			// .findViewById(R.id.temperatures);
			// tTimes = (TextView) rFrameLayout.findViewById(R.id.times);

			// temperatures.setBackgroundResource(R.drawable.ic_launcher);
			// tTimes.setBackgroundResource(R.drawable.ic_launcher);
			// temperatures.setPadding(0, 5, 0, 5);
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(rFrameLayout, layoutParams);
			mFrameLayout.addView(linearLayout);
		} else if (ControlActivity.chooseType == 2) {

			rFrameLayout = (RelativeLayout) LayoutInflater.from(getContext())
					.inflate(R.layout.activity_humidity_item, null);

			// humiditys = (TextView) rFrameLayout.findViewById(R.id.humiditys);
			// hTimes = (TextView) rFrameLayout.findViewById(R.id.times);

			// humiditys.setBackgroundResource(R.drawable.bg_view);
			// hTimes.setBackgroundResource(R.drawable.bg_view);
			// humiditys.setPadding(0, 5, 0, 5);
			mFrameLayout.removeAllViews();
			mFrameLayout.addView(rFrameLayout, layoutParams);
			mFrameLayout.addView(linearLayout);
		} else {
			// LogUtil.showLog("chooseType:"+ControlActivity.chooseType);
		}

	}

	public void setAdapter(BaseAdapter baseAdapter) {
		if (baseAdapter == null)
			return;
		mBaseAdapter = baseAdapter;
		mBaseAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				// Log.e("sofier", "数据改变了，执行重构任务");
				oldPosition = 0;
				buildItemView();
				isRechange = true;
				super.onChanged();
			}
		});
		// Log.e("sofier", "改变adapter后准备重置view");
		mBaseAdapter.notifyDataSetChanged();
	}


	@Override
	public void onClick(View v) {
		/*
		 * if (v.getId() == preImage.getId()) { fling(-800); return; } if
		 * (v.getId() == nextImage.getId()) { fling(800); return; }
		 */
		if (onItemClickListener != null) {
			int position = mSparseArray.indexOfValue(v);
			startAnimation(position);
			oldPosition = position;
			onItemClickListener.click(position);
		}
	}

	private void startAnimation(int position) {
		int scrollExtent = getScrollExtent();
		int scrollOffset = getScrollOffset();
		View v = getItemView(position);
		int width = v.getWidth();
		int l = v.getLeft();
		int r = v.getRight();
		int x = l - scrollOffset;
		int tx = (scrollExtent - width) / 2;
		smoothScrollBy(x - tx, 0);
		notifyReset(position);
		// AnimationSet animationSet = new AnimationSet(true);
		// animationSet.addAnimation(buildScaleAnimation(oldPosition,
		// position));
		// animationSet.addAnimation(buildTranslateAnimation(oldPosition,
		// position));
		// animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
		// /* 移动后不复原,不返回动画前的状态位置 */
		// animationSet.setFillAfter(true);
		// animationSet.setDuration(500);
		// rFrameLayout.startAnimation(animationSet);
		// invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (isRechange) {
			int scrollExtent = getScrollExtent();
			int scrollRange = getScrollRange();
			if (scrollExtent != 0 && scrollRange != 0) {
				if (!isLeft) {
					smoothScrollTo(scrollRange - scrollExtent, 0);
					notifyReset(mBaseAdapter != null ? mBaseAdapter.getCount() - 1
							: -1);
				} else {
					if (oldcount != count) {
						View v = getItemView(count - oldcount);
						int lv = v.getLeft();
						smoothScrollTo(lv, 0);
						notifyReset(count - oldcount);
					}
					isLeft = false;
				}
				isRechange = false;
			}
			oldcount = count;
		}
	}

	private boolean isRechange, isLeft;

	private Animation buildScaleAnimation(int oldPosition, int position) {
		float oldWidth = getItemView(oldPosition).getWidth();
		float newWidth = getItemView(position).getWidth();
		float fromX = oldWidth / rFrameLayout.getWidth();
		float toX = newWidth / rFrameLayout.getWidth();
		ScaleAnimation animation = new ScaleAnimation(fromX, toX, 1f, 1f,
				Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f);
		return animation;
	}

	private Animation buildTranslateAnimation(int oldPosition, int position) {
		TranslateAnimation animation = new TranslateAnimation(getItemView(
				oldPosition).getLeft(), getItemView(position).getLeft(), 0, 0);
		return animation;
	}

	private View getItemView(int position) {
		return mSparseArray.get(position);
	}

	//回调事件  点击的每一个item
	public interface OnItemClickListener {
		void click(int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	/*
	 * public void setImageView(ImageView preImage, ImageView nextImage) {
	 * this.preImage = preImage; this.nextImage = nextImage; if (preImage !=
	 * null) { preImage.setOnClickListener(this); } if (nextImage != null) {
	 * nextImage.setOnClickListener(this); } }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.HorizontalScrollView#onTouchEvent(android.view.MotionEvent
	 * )
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/* 计算水平方向滚动条的滑块的偏移值。 */
		int ScrollOffset = computeHorizontalScrollOffset();
		/* 滚动条长度 */
		int ScrollExtent = computeHorizontalScrollExtent();
		/* 滚动条当前位置 */
		int curScrollLoc = ScrollOffset + ScrollExtent;
		/* scrollView 的可滚动水平范围是所有子视图的宽度总合。 */
		int ScrollRange = computeHorizontalScrollRange();

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			this.scrollType = ScrollType.TOUCH_SCROLL;
			if (scrollViewListener != null)
				scrollViewListener.onScrollChanged(scrollType);
			// 手指在上面移动的时候 取消滚动监听线程
			_scrollHandler.removeCallbacks(scrollRunnable);
			break;
		case MotionEvent.ACTION_UP:
			// 手指移动的时候
			_scrollHandler.post(scrollRunnable);
			/* 如果滚动到最左边 */
			if (curScrollLoc == ScrollExtent) {
				/*
				 * if (preImage != null) {
				 * preImage.setVisibility(View.INVISIBLE);
				 * LogUtil.showLog("left" + String.valueOf(curScrollLoc)); }
				 */
				// 获取旧数据
				Message msg = new Message();
				msg.what = 0;
				_scrollHandler.sendMessage(msg);
				isLeft = true;
				notifyReset(0);
			} else
			/* 如果滚动到最右边 */
			if (curScrollLoc >= ScrollRange) {
				/*
				 * if (nextImage != null) {
				 * nextImage.setVisibility(View.INVISIBLE);
				 * LogUtil.showLog("right" + String.valueOf(curScrollLoc)); }
				 */

				// 获取新数据
				Message msg = new Message();
				msg.what = 1;
				_scrollHandler.sendMessage(msg);
				isLeft = false;
			} else {
				isLeft = false;
			}
			break;

		default:

			break;
		}
		return super.onTouchEvent(ev);
	}

	

	//回调事件  滑动的每一个item
	public interface OnItemResetListener {
		void onReset(int position);
	}

	public void setOnItemResetListener(OnItemResetListener onItemResetListener) {
		this.onItemResetListener = onItemResetListener;
	}

	private void notifyReset(int position) {
		if (onItemResetListener == null)
			return;
		if (position < 0)
			return;
		Log.e("sofier", "回调的item位置为" + position);
		onItemResetListener.onReset(position);
	}

	public interface ScrollViewListener {
		void onScrollChanged(ScrollType scrollType);
	}

	private ScrollViewListener scrollViewListener;

	enum ScrollType {
		IDLE, TOUCH_SCROLL, FLING
	};

	private int currentX = -1;

	private ScrollType scrollType = ScrollType.IDLE;

	private int scrollDealy = 50;

	private Runnable scrollRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (getScrollX() == currentX) {
				// 滚动停止 取消监听线程
				scrollType = ScrollType.IDLE;
				if (aveWidth == 0)
					aveWidth = getItemView(0).getWidth();
				int scrollOffset = getScrollOffset();
				int preCount = scrollOffset / aveWidth;
				int dw = scrollOffset % aveWidth;
				if (dw == 0) {
					notifyReset(preCount);
				} else if (dw > aveWidth / 2) {
					smoothScrollTo(scrollOffset + aveWidth - dw, 0);
					notifyReset(preCount + 1);
				} else {
					smoothScrollTo(scrollOffset - dw, 0);
					notifyReset(preCount);
				}
				if (scrollViewListener != null)
					scrollViewListener.onScrollChanged(scrollType);
				_scrollHandler.removeCallbacks(this);
				return;
			} else {
				scrollType = ScrollType.FLING;
				if (scrollViewListener != null)
					scrollViewListener.onScrollChanged(scrollType);
			}
			currentX = getScrollX();
			_scrollHandler.postDelayed(this, scrollDealy);
		}
	};

	public void setOnScrollStateChangedListener(ScrollViewListener listener) {
		this.scrollViewListener = listener;
	}
	
	
	//去掉边界渐变效果
	@SuppressLint("NewApi")
	@Override
	public void setOverScrollMode(int mode) {

	if (mode != OVER_SCROLL_NEVER) {

	if (mEdgeGlowTop == null) {

	Context context = getContext();

	mEdgeGlowTop = new EdgeEffect(context);

	mEdgeGlowBottom = new EdgeEffect(context);

	}

	} else {

	mEdgeGlowTop = null;

	mEdgeGlowBottom = null;

	}

	super.setOverScrollMode(mode);

	try {

	    Method method = getClass().getMethod("setOverScrollMode", int.class);

	    Field field = getClass().getField("OVER_SCROLL_NEVER");

	    if(method != null && field != null){

	        method.invoke(this, field.getInt(View.class));

	    }

	} catch (Exception e) {

	    e.printStackTrace();

		}
	}
}
