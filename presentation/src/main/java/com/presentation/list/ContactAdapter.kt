package com.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.domain.entities.Contact
import com.presentation.databinding.ListItemViewBinding

class ContactAdapter(
    private val onItemClicked: (Contact) -> Unit,
) : ListAdapter<Contact, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = getItem(position)
        (holder as ContactViewHolder).bind(contact)
    }

    // DiffUtil takes care of the check of new list for changes
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Contact> =
            object : DiffUtil.ItemCallback<Contact>() {
                override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Contact, newItem: Contact
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class ContactViewHolder(
        private val binding: ListItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            with(binding) {
                name.text = contact.name
                phone.text = contact.phone
                height.text = contact.height.toString()
                itemView.setOnClickListener {
                    onItemClicked(contact)
                }
            }
        }
    }
}

