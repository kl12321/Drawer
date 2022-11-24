package com.example.drawer.adapter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.AnimationUtils.loadAnimation

import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.ListAdapter
import com.example.drawer.R
import com.example.drawer.databinding.HomeItemBinding
import com.example.drawer.entity.Pixabay



class HomeAdapter(private val listener:ItemListener): ListAdapter<Pixabay, HomeViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val holder=HomeViewHolder(HomeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        holder.itemView.setOnClickListener {
//            Bundle().apply{
//                putParcelable("pixabay",getItem(holder.adapterPosition))
//                val action=OverViewFragmentDirections.actionOverViewFragmentToLargeFragment(this)
//                val extras = FragmentNavigatorExtras(holder.itemView to "card_${getItem(holder.adapterPosition).id}")
//                holder.itemView.findNavController().navigate(action,extras)
//            }
            listener.onCardClicked(holder.itemView,getItem(holder.adapterPosition))


        }
        return holder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.animator_scale)

        holder.bind(getItem(position))
    }

    companion object {
        val DiffUtil=object: DiffUtil.ItemCallback<Pixabay>(){
            override fun areItemsTheSame(oldItem: Pixabay, newItem: Pixabay): Boolean {
                return oldItem.id==newItem.id

            }

            override fun areContentsTheSame(oldItem: Pixabay, newItem: Pixabay): Boolean {
                return oldItem.previewURL==newItem.previewURL
            }
        }
    }
}