package com.example.drawer

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.drawer.databinding.ActivityMainBinding
import com.example.drawer.databinding.BottomViewBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lottieView.playAnimation()
        binding.lottieView.addAnimatorListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                val animation=ObjectAnimator.ofFloat(binding.lottieView,View.ALPHA,0f)
                animation.duration=1000
                animation.start()
                animation.addListener(object :Animator.AnimatorListener{
                    override fun onAnimationStart(p0: Animator?) {
                        //                   TODO("Not yet implemented")
                    }

                    override fun onAnimationEnd(p0: Animator?) {

                        binding.fragmentContainerView.visibility=View.VISIBLE
                        binding.root.removeView(binding.lottieView)
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                        //  TODO("Not yet implemented")
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                        //  TODO("Not yet implemented")
                    }

                })
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }

        })


    }

}


