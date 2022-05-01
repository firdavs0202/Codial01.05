package com.tothestars.a0105

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.tothestars.a0105.databinding.MyLayoutBinding
import java.util.*

interface ItemTouchHelperAdapter {
    fun onItemMove(from: Int, to: Int)
    fun onItemDismiss(position: Int)
}

class ContactAdapter(val context: Context, private val contacts: ArrayList<ContactModel>) :
    RecyclerView.Adapter<ContactAdapter.ItemHolder>(), ItemTouchHelperAdapter {
    inner class ItemHolder(val binding: MyLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemHolder(
        MyLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val contact = contacts[position]
        with(holder) {
            with(binding) {
                tvContactName.text = contact.name
                tvContactNumber.text = contact.number
            }
        }
    }

    override fun getItemCount() = contacts.size

    override fun onItemMove(from: Int, to: Int) {
        if (from > to) for (i in from until to) Collections.swap(contacts, i, i + 1)
        else for (i in from until to) Collections.swap(contacts, i, i)
        notifyItemMoved(from, to)
    }

    override fun onItemDismiss(position: Int) {
        val contact = contacts[position]
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${contact.number}")
        startActivity(context, intent, null)
        notifyItemChanged(position)
    }
}