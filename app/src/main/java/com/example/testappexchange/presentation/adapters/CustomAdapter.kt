package com.example.testappexchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testappexchange.core.util.toTwo
import com.example.testappexchange.databinding.UserBalanceItemBinding
import com.example.testappexchange.domain.models.BalanceCurrency

class CustomAdapter: ListAdapter<BalanceCurrency, CustomAdapter.CustomAdapterViewHolder>(diffUtil) {
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<BalanceCurrency>() {
            override fun areItemsTheSame(
                oldItem: BalanceCurrency,
                newItem: BalanceCurrency
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: BalanceCurrency,
                newItem: BalanceCurrency
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class CustomAdapterViewHolder(val binding: UserBalanceItemBinding): RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserBalanceItemBinding.inflate(inflater,parent,false)
        return CustomAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomAdapterViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            tvName.text = data.name
            tvBalance.text = data.balance.toTwo().toString()
        }

    }

}