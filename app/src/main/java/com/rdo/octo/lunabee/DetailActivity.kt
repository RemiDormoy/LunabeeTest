package com.rdo.octo.lunabee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.cell_user.view.*

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val first_nameExtra = "first_nameExtra"
        private const val last_nameExtra = "last_nameExtra"
        private const val emailExtra = "emailExtra"
        private const val genderExtra = "genderExtra"
        private const val avatarExtra = "avatarExtra"

        fun newIntent(user: User, context: Context) = Intent(context, DetailActivity::class.java)
            .putExtra(first_nameExtra, user.first_name)
            .putExtra(last_nameExtra, user.last_name)
            .putExtra(emailExtra, user.email)
            .putExtra(genderExtra, user.gender)
            .putExtra(avatarExtra, user.avatar)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        intent.getStringExtra(first_nameExtra)?.let {
            firstNameTextView.text = it
        }
        intent.getStringExtra(last_nameExtra)?.let {
            nameTextView.text = it
        }
        intent.getStringExtra(emailExtra)?.let {
            emailTextView.text = it
        }
        intent.getStringExtra(genderExtra)?.let {
            genderTextView.text = it
        }
        intent.getStringExtra(avatarExtra)?.let {
            Picasso.get()
                .load(it)
                .into(imageView)
        }
    }
}
