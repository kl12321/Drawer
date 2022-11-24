package com.example.drawer.util

import android.content.Context
import android.media.MediaParser.SeekPoint.START
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class Vibrate {
    companion object{
        fun getVibrate(context:Context): Vibrator {
            val vib  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =  context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator;
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            return vib
        }
        fun occurVibrate(context: Context){
            val vib  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =  context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator;
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val effect= VibrationEffect.createOneShot(5,255)
                vib.vibrate(effect)
            }else{
                vib.vibrate(5)
            }
        }
    }
}