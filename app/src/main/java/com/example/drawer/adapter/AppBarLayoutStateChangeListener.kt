package com.example.drawer.adapter

import android.util.Log
import android.view.View
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.AppBarLayout
private const val TAG="AppBarLayoutStateChangeListener"
abstract class AppBarLayoutStateChangeListener : OnOffsetChangedListener {
    private var trigger=false
    enum class State {
        EXPANDED,  //展开
        COLLAPSED,  //折叠
        INTERMEDIATE //中间状态
    }

    private var mCurrentState = State.INTERMEDIATE
    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        mCurrentState = if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(
                    appBarLayout,
                    State.EXPANDED
                )
            }
            State.EXPANDED
        } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(
                    appBarLayout,
                    State.COLLAPSED
                )
            }
            State.COLLAPSED
        } else {
            if (mCurrentState != State.INTERMEDIATE) {
                onStateChanged(
                    appBarLayout,
                    State.INTERMEDIATE
                )
            }
            State.INTERMEDIATE
        }
        if (!trigger&&verticalOffset<-500){
            Log.d(TAG, "onOffsetChanged: trigger")
            TriggerAnimation()
            trigger=true
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State?)
    abstract fun TriggerAnimation()
}