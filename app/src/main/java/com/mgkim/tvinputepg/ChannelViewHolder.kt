package com.mgkim.tvinputepg

import androidx.recyclerview.widget.RecyclerView
import com.mgkim.tvinputepg.databinding.ItemChannelBinding

class ChannelViewHolder(
    private val binding: ItemChannelBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChannelData) {
        binding.apply {
            tvChannelInfo.text = item.toString()
        }
    }
}