package com.mgkim.tvinputepg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.DiffCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mgkim.tvinputepg.databinding.ItemChannelBinding

class ChannelAdapter : ListAdapter<ChannelData, ChannelViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChannelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChannelBinding.inflate(inflater, parent, false)
        binding.tvChannelInfo.isFocusable = true
        binding.tvChannelInfo.isFocusableInTouchMode = true
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChannelViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChannelData>() {
            override fun areItemsTheSame(
                oldItem: ChannelData,
                newItem: ChannelData
            ): Boolean {
                return oldItem.channelId == newItem.channelId
            }

            override fun areContentsTheSame(
                oldItem: ChannelData,
                newItem: ChannelData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}