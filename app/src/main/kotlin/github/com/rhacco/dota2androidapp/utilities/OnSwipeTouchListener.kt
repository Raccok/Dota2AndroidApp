package github.com.rhacco.dota2androidapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeTouchListener(private val mContext: Context) : View.OnTouchListener {
    private val mGestureDetector: GestureDetector by lazy {
        GestureDetector(mContext, GestureListener(this))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }

    open protected fun onSwipeLeft() {}

    open protected fun onSwipeRight() {}

    open protected fun onSwipeTop() {}

    open protected fun onSwipeBottom() {}

    private class GestureListener(private val mCaller: OnSwipeTouchListener) :
            GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velX: Float, velY: Float): Boolean {
            if (e1.x - e2.x > 90 && Math.abs(velX) > 120) {
                mCaller.onSwipeLeft()
                return true
            }
            if (e2.x - e1.x > 90 && Math.abs(velX) > 120) {
                mCaller.onSwipeRight()
                return true
            }
            if (e1.y - e2.y > 90 && Math.abs(velY) > 120) {
                mCaller.onSwipeTop()
                return true
            }
            if (e2.y - e1.y > 90 && Math.abs(velY) > 120) {
                mCaller.onSwipeBottom()
                return true
            }
            return false
        }
    }
}
