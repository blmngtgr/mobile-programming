package com.example.mobile_programming_teamproject.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile_programming_teamproject.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.Date

class ItemAdapter(val onItemClicked: (ItemModel) -> Unit) : ListAdapter<ItemModel, ItemAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(itemModel.createdAt)

            binding.titleTextView.text = itemModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = itemModel.price

            if(itemModel.imageURL.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(itemModel.imageURL)
                    .into(binding.thumbnailImageView)
            }
            binding.root.setOnClickListener {
                onItemClicked(itemModel)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemModel>() {
            //기존의 아이템과 같은지 확인
            override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}