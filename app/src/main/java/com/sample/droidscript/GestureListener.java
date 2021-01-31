package com.sample.droidscript;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String LOG_TAG2 = "Script";
    static String currentGestureDetected;

    public boolean onSingleTapUp(MotionEvent ev) {
        Log.w(LOG_TAG2, "onSingleTapUp");
        currentGestureDetected = ev.toString();
        return true;
    }

    public void onShowPress(MotionEvent ev) {
        Log.w(LOG_TAG2, "onShowPress");
        currentGestureDetected = ev.toString();
    }

    public void onLongPress(MotionEvent ev) {
        Log.w(LOG_TAG2, "onLongPress");
        currentGestureDetected = ev.toString();
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.w(LOG_TAG2, "onScroll");
        currentGestureDetected = String.valueOf(e1.toString()) + "  " + e2.toString();
        return true;
    }

    public boolean onDown(MotionEvent ev) {
        Log.w(LOG_TAG2, "onDown");
        currentGestureDetected = ev.toString();
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.w(LOG_TAG2, "onFling");
        currentGestureDetected = String.valueOf(e1.toString()) + "  " + e2.toString();
        return true;
    }
}