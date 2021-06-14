package com.example.kotlinmvvmsample.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmvvmsample.ContactAdapter
import com.example.kotlinmvvmsample.Model.ContactViewModel
import com.example.kotlinmvvmsample.R
import com.example.kotlinmvvmsample.RoomUtils.Contact
import com.example.kotlinmvvmsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val dataBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        dataBinding.lifecycleOwner = this

        // Set contactItemClick & contactItemLongClick lambda
        val adapter = ContactAdapter({ contact ->
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NAME, contact.name)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NUMBER, contact.number)
            intent.putExtra(AddActivity.EXTRA_CONTACT_ID, contact.id)
            startActivity(intent)
        }, { contact ->
            deleteDialog(contact)
        })

        val lm = LinearLayoutManager(this)
        dataBinding.mainRecycleview.adapter = adapter
        dataBinding.mainRecycleview.layoutManager = lm
        dataBinding.mainRecycleview.setHasFixedSize(true)

        dataBinding.mainButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }


        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(this, Observer<List<Contact>> { contacts ->
            adapter.setContacts(contacts!!)
        })

    }

    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactViewModel.delete(contact)
            }

        builder.show()
    }


}