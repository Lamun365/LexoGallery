package com.lexo.lexogallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexo.lexogallery.databinding.ImageRecycleBinding

class ImageAdapter(private val context: Context, private val imgList: ArrayList<ImageData>, private val listener: ItemClickListener): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    class ImageViewHolder(val binding: ImageRecycleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context).load(imgList[position].uri).centerCrop().into(holder.binding.imgRec)
        holder.binding.imgRec.setOnClickListener {
            listener.onClick(position)
        }
    }

    override fun getItemCount() = imgList.size

    interface ItemClickListener {
        fun onClick(item: Int)
    }
}