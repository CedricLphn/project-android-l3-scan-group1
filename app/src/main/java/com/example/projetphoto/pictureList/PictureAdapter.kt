package com.example.projetphoto.pictureList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projetphoto.databinding.ItemPictureBinding

class PictureAdapter(private var pictures: List<Picture>) :
    RecyclerView.Adapter<PictureAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPictureBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = pictures[position]
        with(holder.binding) {
            titleTextView.text = picture.title
            dateTextView.text = picture.pictureDate
            pictureImageView.setImageResource(picture.pictureImage)
            nbObjectTextView.text = picture.nbObject.toString()
        }
    }

    override fun getItemCount(): Int = pictures.size

    fun updateDataSet(photos: List<Picture>) {
        this.pictures = photos
        notifyDataSetChanged()
    }
}