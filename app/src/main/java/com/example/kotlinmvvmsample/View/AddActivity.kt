package com.example.kotlinmvvmsample.View

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinmvvmsample.Model.ContactViewModel
import com.example.kotlinmvvmsample.R
import com.example.kotlinmvvmsample.RoomUtils.Contact
import com.example.kotlinmvvmsample.databinding.ActivityAddBinding
import com.example.kotlinmvvmsample.databinding.ActivityMainBinding

class AddActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel
    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add)

        val dataBinding = DataBindingUtil.setContentView<ActivityAddBinding>(this,
            R.layout.activity_add
        )
        dataBinding.lifecycleOwner = this


        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        // intent null check & get extras
        if (intent != null && intent.hasExtra(EXTRA_CONTACT_NAME) && intent.hasExtra(EXTRA_CONTACT_NUMBER)
            && intent.hasExtra(EXTRA_CONTACT_ID)) {
            dataBinding.addEdittextName.setText(intent.getStringExtra(EXTRA_CONTACT_NAME))
            dataBinding.addEdittextNumber.setText(intent.getStringExtra(EXTRA_CONTACT_NUMBER))
            id = intent.getLongExtra(EXTRA_CONTACT_ID, -1)
        }

        dataBinding.addButton.setOnClickListener {
            val name = dataBinding.addEdittextName.text.toString().trim()
            val number = dataBinding.addEdittextNumber.text.toString()

            if (name.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter name and number.", Toast.LENGTH_SHORT).show()
            } else {
                val initial = name[0].toUpperCase()
                val contact = Contact(id, name, number, initial)
                contactViewModel.insert(contact)
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"
        const val EXTRA_CONTACT_NUMBER = "EXTRA_CONTACT_NUMBER"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }
}