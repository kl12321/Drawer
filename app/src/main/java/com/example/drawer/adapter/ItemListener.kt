package com.example.drawer.adapter

import android.content.ClipData.Item
import android.view.View
import com.example.drawer.entity.Pixabay

interface ItemListener {
    fun onCardClicked(item: View, pixabay: Pixabay)
}