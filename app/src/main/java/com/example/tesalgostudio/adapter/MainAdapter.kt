package com.example.tesalgostudio.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tesalgostudio.MemesItem
import com.example.tesalgostudio.databinding.ItemMemeBinding
import com.example.tesalgostudio.ui.DetailActivity

class MainAdapter() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val listMeme = ArrayList<MemesItem>()

    fun setDataMeme(list: List<MemesItem>){
        listMeme.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemMemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memes: MemesItem) {
            Glide.with(itemView.context)
                .load(memes.url)
                .centerCrop()
                .into(binding.ivMeme)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("EXTRA_URL", memes.url)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemMemeBinding =
            ItemMemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemMemeBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memes = listMeme[position]
        holder.bind(memes)
    }

    override fun getItemCount(): Int = listMeme.size
}