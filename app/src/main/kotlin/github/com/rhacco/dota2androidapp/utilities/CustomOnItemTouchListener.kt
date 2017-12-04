package github.com.rhacco.dota2androidapp.utilities

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

open class CustomOnItemTouchListener(private val mContext: Context, private val mRecyclerView: RecyclerView) :
        RecyclerView.OnItemTouchListener {
    private val mGestureDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(mContext, GestureListener(this, mRecyclerView))
    }

    open protected fun onSingleTap(itemPosition: Int) {}

    open protected fun onDoubleTap(itemPosition: Int) {}

    open protected fun onSwipeLeft(itemPosition: Int) {}

    open protected fun onSwipeRight(itemPosition: Int) {}

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(e)
        return false
    }

    private class GestureListener(
            private val mParent: CustomOnItemTouchListener, private val mRecyclerView: RecyclerView) :
            GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val clickedView = mRecyclerView.findChildViewUnder(e.x, e.y)
            if (clickedView != null) {
                mParent.onSingleTap(mRecyclerView.getChildAdapterPosition(clickedView))
                return true
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            val clickedView = mRecyclerView.findChildViewUnder(e.x, e.y)
            if (clickedView != null) {
                mParent.onDoubleTap(mRecyclerView.getChildAdapterPosition(clickedView))
                return true
            }
            return false
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velX: Float, velY: Float): Boolean {
            val touchedView = mRecyclerView.findChildViewUnder(e1.x, e1.y)
            if (touchedView != null) {
                val itemPosition = mRecyclerView.getChildAdapterPosition(touchedView)
                if (e1.x - e2.x > 1 && Math.abs(velX) > 1) {
                    mParent.onSwipeLeft(itemPosition)
                    return true
                }
                if (e2.x - e1.x > 1 && Math.abs(velX) > 1) {
                    mParent.onSwipeRight(itemPosition)
                    return true
                }
            }
            return false
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}
}