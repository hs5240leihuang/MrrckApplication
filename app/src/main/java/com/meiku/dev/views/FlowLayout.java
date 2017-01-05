package com.meiku.dev.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 个人标签group，自动换行
 */
public class FlowLayout extends ViewGroup {
	
	/**
	 * childView的总个数
	 */
	private int childCount;

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FlowLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p){
		
	    return new MarginLayoutParams(p);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs){
		
	    return new MarginLayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams(){
		
	    return new MarginLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	}
	

	/**
	 * onMeasure():测量方法,测量自己的大小,为正式布局提供建议(只是建议,具体是否采用,看onLayout而定)
	 * 
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//获得ViewGroup的建议宽高和测量模式
		int measureSpecWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureSpecHeight = MeasureSpec.getSize(heightMeasureSpec);
		int measureSpecWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int measureSpecheightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		childCount = getChildCount();
		
		int width = 0 ;//记录ViewGroup的宽度
		int height = 0 ;//记录ViewGroup的高度
		int lineWinth = 0 ;//记录每一行的宽度
		int lingHeight = 0 ;//记录每一行的高度
		
		
		/**
		 * 依次遍历所有子节点(一定不能写成i<=childCount)
		 * 否侧抛出:java.lang.NullPointerException: Attempt to invoke virtual method '
		 * android.view.ViewGroup$LayoutParams android.view.View.getLayoutParams()' on a null object reference
		 */
		for(int i=0; i <childCount ; i++){
			
			//得到childView
			View childView = getChildAt(i);
			
			//测量childView的宽高
			measureChild(childView, widthMeasureSpec, heightMeasureSpec);
			
			//如果忘记重写generateLayoutParams，则hild.getLayoutParams()将不是MarginLayoutParams的实例
            //在强制转换时就会出错，此时我们把左右间距设置为0，但由于在计算布局宽高时没有加上间距值，就是计算出的宽高要比实际小，所以是onLayout时就会出错
            MarginLayoutParams mp = null;
            if (childView.getLayoutParams() instanceof MarginLayoutParams) {
            	
            	//获取MarginLayoutParams对象
   			 	mp = (MarginLayoutParams) childView.getLayoutParams();
            }else{
            	mp = new MarginLayoutParams(0,0);
            }
			
			//获取childView的宽高
			int measuredChildWidth = childView.getMeasuredWidth() + mp.leftMargin + mp.rightMargin;
			int measuredChildHeight = childView.getMeasuredHeight() + mp.topMargin + mp.bottomMargin;
			
			/**
			 * 分析可能出现情况:
			 * 1.当前行宽度大于Viewgroup宽度:
			 * 	-换行:
			 * 		height:高度需要每次换行后记录并叠加
			 * 		width:记录下当前最大宽度
			 * 		换行后lineWidth需要重新初始化为measuredChildWidth(这里也可以置零 + 当前measuredChildWidth);
			 * 	-否则不换行
			 * 		lineWidth:宽度累加
			 * 2.最后一行肯定不会超过屏幕宽度:需要单独处理
			 * 
			 */
			
			if(lineWinth + measuredChildWidth > measureSpecWidth){
				
				//需要换行
				height += measuredChildHeight ;
				width = Math.max(lineWinth, width);
				
				lineWinth = 0 + measuredChildWidth;
				lingHeight = 0 + measuredChildHeight ;
			}else{
				
				//不用换行
				lineWinth += measuredChildWidth ;
			}
			
			if(i == childCount -1){
				
				//特殊处理
				height += measuredChildHeight ;
				width = Math.max(lineWinth, width);
			}
		}
		
		//依据测量模式设置Layout最终宽高
		setMeasuredDimension(measureSpecWidthMode==MeasureSpec.EXACTLY?measureSpecWidth:width, measureSpecheightMode==MeasureSpec.EXACTLY?measureSpecHeight:height);
	}
	
	/**
	 * onLayout():使用layout()函数对所有子控件布局； 
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = 0 ;//记录ViewGroup的宽度
		int height = 0 ;//记录ViewGroup的高度
		int lineWinth = 0 ;//记录每一行的宽度
		int lingHeight = 0 ;//记录每一行的高度
		int left = 0 ;//记录left左边点
		int top = 0 ;//记录top坐标点
		
		for (int i = 0; i < childCount; i++) {
			
			//得到childView
			View childView = getChildAt(i);
			
			//如果忘记重写generateLayoutParams，则hild.getLayoutParams()将不是MarginLayoutParams的实例
            //在强制转换时就会出错，此时我们把左右间距设置为0，但由于在计算布局宽高时没有加上间距值，就是计算出的宽高要比实际小，所以是onLayout时就会出错
            MarginLayoutParams mp = null;
            if (childView.getLayoutParams() instanceof MarginLayoutParams) {
            	//获取MarginLayoutParams对象
   			 	mp = (MarginLayoutParams) childView.getLayoutParams();
            }else{
            	mp = new MarginLayoutParams(0,0);
            }
			
			//获取childView的宽高
			int measuredChildWidth = childView.getMeasuredWidth() + mp.leftMargin + mp.rightMargin;
			int measuredChildHeight = childView.getMeasuredHeight() + mp.topMargin + mp.bottomMargin;
			
			if(lineWinth + measuredChildWidth > getMeasuredWidth()){
				
				top += measuredChildHeight ;
				left = 0 ;

				lineWinth = 0 + measuredChildWidth;
				lingHeight = 0 + measuredChildHeight ;
				
			}else{
				lingHeight = measuredChildHeight ;
				//不用换行
				lineWinth += measuredChildWidth ;
			}
			
			/**
			 * 	Parameters:
			 * 	l Left position, relative to parent
			 * 	t Top position, relative to parent
			 * 	r Right position, relative to parent
			 * 	b Bottom position, relative to parent
			 *  layout()的位置会直接影响到view的宽高,如果尺寸错误,会导致view显示畸形
			 */
			childView.layout(left + mp.leftMargin , top + mp.topMargin , left +  childView.getMeasuredWidth() + mp.leftMargin , top +  childView.getMeasuredHeight() + mp.topMargin);
			left += measuredChildWidth ;
		}
	}
}
