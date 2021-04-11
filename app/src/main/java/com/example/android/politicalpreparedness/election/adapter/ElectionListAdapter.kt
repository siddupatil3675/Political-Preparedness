package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.model.Election

class ElectionListAdapter(private val clickListener: ElectionListener) : ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    class ElectionViewHolder(var binding: ItemElectionBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: ElectionListener, election: Election) {
            binding.election = election
            binding.clickListener = listener
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder(ItemElectionBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Election, newItem: Election) = oldItem == newItem

    }

    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }
}






