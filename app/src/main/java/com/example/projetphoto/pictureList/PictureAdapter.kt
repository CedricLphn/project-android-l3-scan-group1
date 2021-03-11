package com.example.projetphoto.pictureList

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.projetphoto.databinding.ItemPictureBinding
import com.example.projetphoto.db.pictures.Pictures
import com.example.projetphoto.itemDetails.ItemDetailsActivity

class PictureAdapter(private var pictures: List<Pictures>) :
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
            itemConstraintLayout.setOnClickListener {
                val context = holder.binding.titleTextView.context
                val intent = Intent(context, ItemDetailsActivity::class.java)
                intent.putExtra("idPhoto", picture.title)
                context.startActivity(intent)
            }
            titleTextView.text = picture.title
            dateTextView.text = "picture.pictureDate"
            pictureImageView.setImageURI(Uri.parse(picture.link))
            nbObjectTextView.text = "picture.nbObject.toString()"
        }
    }

    override fun getItemCount(): Int = pictures.size

    fun updateDataSet(photos: List<Pictures>) {
        this.pictures = photos
        notifyDataSetChanged()
    }
}