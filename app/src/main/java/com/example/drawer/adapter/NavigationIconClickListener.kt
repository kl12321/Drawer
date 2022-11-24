package com.example.drawer.adapter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.drawer.R
import com.example.drawer.util.Vibrate
import kotlin.math.log

/**
 * [android.view.View.OnClickListener] used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
private const val TAG="NavigationIconClickListener"
class NavigationIconClickListener @JvmOverloads internal constructor(
    private val context: Context, private val sheet: View, private val interpolator: Interpolator? = null,
    private val openIcon: Drawable? = null, private val closeIcon: Drawable? = null) : View.OnClickListener {

    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false
    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(view: View) {
        backdropShown = !backdropShown
        Vibrate.occurVibrate(view.context)
        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()
        updateIcon(view)

        val translateY = height - 1800

        val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())
        animator.duration = 500
        if (interpolator != null) {
            animator.interpolator = interpolator
        }
        animatorSet.play(animator)
        animator.start()
    }


    private fun updateIcon(view:View) {
//        if (openIcon != null && closeIcon != null) {
//            if (view !is ImageView) {
//                throw IllegalArgumentException("updateIcon() must be called on an ImageView")
//            }
//            if (backdropShown) {
//                view.setImageDrawable(closeIcon)
//            } else {
//                view.setImageDrawable(openIcon)
//            }
//        }
       val lottie= view as LottieAnimationView
            lottie.speed=1.5f
//        lottie.repeatMode=LottieDrawable.RESTART
//        lottie.repeatCount=1
        lottie.addAnimatorListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
//                TODO("Not yet implemented")
                Log.d(TAG, "onAnimationEnd: 结束")
            }

            override fun onAnimationEnd(p0: Animator?) {
//                TODO("Not yet implemented")
//                lottie.cancelAnimation()
                Log.d(TAG, "onAnimationEnd: 结束")
            }

            override fun onAnimationCancel(p0: Animator?) {
//                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator?) {
//                TODO("Not yet implemented")
            }

        })
        if(backdropShown){
            lottie.setMinAndMaxFrame(0,45)
            lottie.playAnimation()
        }else{

            lottie.setMinAndMaxFrame(45,75)
            lottie.playAnimation()
        }





    }
}
