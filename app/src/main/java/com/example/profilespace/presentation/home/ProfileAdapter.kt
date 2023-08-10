package com.example.profilespace.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.profilespace.R
import com.example.profilespace.databinding.RvCardProfileBinding
import com.example.profilespace.domain.model.Profile

/**
 * Created by Omar Elashry.
 */
class ProfileAdapter(private val itemListener: ItemListener) :
    ListAdapter<Profile, ProfileAdapter.MyViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            RvCardProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(itemView: View, binding: RvCardProfileBinding) :
        RecyclerView.ViewHolder(itemView) {
        private var binding: RvCardProfileBinding

        init {
            this.binding = binding
            itemView.setOnClickListener {
                itemListener.onClick(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
            this.binding.deleteBtn.setOnClickListener {
                itemListener.onDelete(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }

        fun bind(item: Profile) {
            binding.nameTV.text = item.name
            binding.jobTitleTV.text = item.jobTitle
            binding.ageTV.text =
                binding.root.context.getString(R.string.age_label).plus(item.age.toString())
            binding.locationTV.text = item.country
            binding.levelTV.text = binding.root.context.getString(R.string.level_label).plus(
                when (item.level) {
                    1 -> "Junior"
                    2 -> "Mid-Level"
                    3 -> "Senior"
                    else -> "Manager"
                }
            )
            if (item.gender == "Male")
                binding.genderIV.setImageResource(R.drawable.male_avatar)
            else binding.genderIV.setImageResource(R.drawable.female_avatar)
        }
    }
}


interface ItemListener {
    fun onClick(item: Profile, position: Int)
    fun onDelete(item: Profile, position: Int)
}

private class ItemDiffCallback : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean =
        oldItem == newItem

}