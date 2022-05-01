package com.tothestars.a0105

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tothestars.a0105.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val contacts = arrayListOf(
        ContactModel("Firdavs", "+998997546082"),
        ContactModel("Sattorov", "+998907176082"),
        ContactModel("Salimovich", "+998332406082"),
    )

    private val contactAdapter = ContactAdapter(this, contacts)

    private val itemTouchHelper = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            contactAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            contactAdapter.onItemDismiss(viewHolder.adapterPosition)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AllSharedPreferences.init(this)

        val itemTouch = ItemTouchHelper(itemTouchHelper)
        itemTouch.attachToRecyclerView(binding.rvContacts)

        binding.rvContacts.adapter = contactAdapter

        binding.fabAddContact.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_add_contact)

        val yesBtn = dialog.findViewById(R.id.dialogAdd) as Button
        val noBtn = dialog.findViewById(R.id.dialogClose) as Button

        yesBtn.setOnClickListener {
            val name = dialog.findViewById<EditText>(R.id.dialogName)
            val number = dialog.findViewById<EditText>(R.id.dialogNumber)
            when {
                name.text.isBlank() -> name.error = "Fill"
                number.text.isBlank() -> number.error = "Fill"
                else -> {
                    contacts.add(
                        ContactModel(
                            name.text.toString().trim(),
                            number.text.toString().trim()
                        )
                    )
                    contactAdapter.notifyItemInserted(contacts.size - 1)
                    dialog.dismiss()
                }
            }
        }

        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}