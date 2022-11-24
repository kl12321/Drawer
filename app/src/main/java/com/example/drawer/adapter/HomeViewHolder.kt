package com.example.drawer.adapter


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.drawer.R
import com.example.drawer.databinding.HomeItemBinding
import com.example.drawer.entity.Pixabay

class HomeViewHolder(private val binding:HomeItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(pixabay: Pixabay){
        binding.author.text=pixabay.author
        binding.root.transitionName="card_${pixabay.id}"
//        binding.downloads.text=pixabay.downloads.toString()
        val sharedOptions=RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
       Glide.with(binding.root).load(pixabay.previewURL)
             .apply(sharedOptions)
            .into(binding.picture)


    }
}