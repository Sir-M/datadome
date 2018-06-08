package de.slg.datadome

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.BottomSheetBehavior
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.support.design.widget.CoordinatorLayout

class AdvancedBottomSheetBehaviour<V : View> constructor(context: Context, set: AttributeSet) : BottomSheetBehavior<V>(context, set)