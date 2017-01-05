package com.meiku.dev.views.gesture;

import android.view.MotionEvent;

public interface GestureDetector {

	public boolean onTouchEvent(MotionEvent ev);

	public boolean isScaling();

	public void setOnGestureListener(OnGestureListener listener);

}
