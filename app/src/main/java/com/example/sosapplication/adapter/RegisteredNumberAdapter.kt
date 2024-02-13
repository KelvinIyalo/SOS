package com.example.sosapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapplication.databinding.RegisteredItemsBinding
import com.example.sosapplication.model.EmergencyNumber


class RegisteredNumberAdapter(
    private val onItemClicked: (Int, EmergencyNumber) -> Unit
) : ListAdapter<EmergencyNumber, RegisteredNumberAdapter.EmergencyHistoryVH>(object :
    DiffUtil.ItemCallback<EmergencyNumber>() {

    override fun areItemsTheSame(oldItem: EmergencyNumber, newItem: EmergencyNumber): Boolean {
        return oldItem.name== newItem.name
    }

    override fun areContentsTheSame(oldItem: EmergencyNumber, newItem: EmergencyNumber): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyHistoryVH {
        //inflate the view to be used by the payment option view holder
        val binding =
            RegisteredItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmergencyHistoryVH(binding, onItemClick = { position ->
            val itemAtPosition = currentList[position]
            this.onItemClicked(position, itemAtPosition)
        })

    }

    override fun submitList(list: List<EmergencyNumber>?) {
        super.submitList(list)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: EmergencyHistoryVH, position: Int) {
        val itemAtPosition = currentList[position]
        holder.bind(itemAtPosition)
    }


    inner class EmergencyHistoryVH(
        private val binding: RegisteredItemsBinding,
        onItemClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }


        fun bind(emergency: EmergencyNumber) {

            with(binding) {
                this.registeredNumber.text = emergency.phoneNumber
                this.registeredName.text = emergency.name
            }
        }

    }

}