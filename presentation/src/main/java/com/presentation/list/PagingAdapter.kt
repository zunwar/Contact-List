package com.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.entities.Contact
import com.presentation.databinding.ListItemViewBinding


class PagingAdapter(
    private val onItemClicked: (Contact) -> Unit,
) : PagingDataAdapter<Contact, RecyclerView.ViewHolder>(DIFF_CALLBACK_PAGER) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val binding =
            ListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item can be null. ViewHolder must support binding a
        // null item as a placeholder.
        (holder as PagingAdapter.ContactViewHolder).bind(item)
    }


    companion object {
        val DIFF_CALLBACK_PAGER: DiffUtil.ItemCallback<Contact> =
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
        fun bind(contact: Contact?) {
            with(binding) {
                name.text = contact?.name ?: ""
                phone.text = contact?.phone ?: ""
                height.text = contact?.height.toString()
                itemView.setOnClickListener {
                    onItemClicked(contact as Contact)
                }
            }
        }
    }

}