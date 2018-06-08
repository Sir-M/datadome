package de.slg.datadome

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.BottomSheetBehavior
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.support.design.widget.CoordinatorLayout

class AdvancedBottomSheetBehaviour<V : View> constructor(context: Context, set: AttributeSet) : BottomSheetBehavior<V>(context, set) {

    override fun onInterceptTouchEvent(parent: CoordinatorLayout?, child: V, event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN &&
                (state == BottomSheetBehavior.STATE_EXPANDED || state == BottomSheetBehavior.STATE_COLLAPSED)) {
            val outRect = Rect()
            child.getGlobalVisibleRect(outRect)

            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()))
                state = BottomSheetBehavior.STATE_HIDDEN

        }
        return super.onInterceptTouchEvent(parent, child, event)
    }

}