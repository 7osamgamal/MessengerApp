package com.hosam.messengerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refusers: DatabaseReference
    var FirebaseUserId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar_register)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_register.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        register_btn.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val username = user_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()
        if (username.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(this@RegisterActivity, "please fill all the field", Toast.LENGTH_LONG)
                .show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirebaseUserId = mAuth.currentUser!!.uid
                        refusers = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(FirebaseUserId)
                        val UserHashMap = HashMap<String, Any>()
                        UserHashMap["UId"] = FirebaseUserId
                        UserHashMap["UserName"] = username
                        UserHashMap["Profile"] =
                            "https://firebasestorage.googleapis.com/v0/b/messengerapp-47f0d.appspot.com/o/profile.png?alt=media&token=0da0288a-7f69-47e8-beae-21a713e2e2f0"
                        UserHashMap["Status"] = "Offline"
                        UserHashMap["Facebook"] = "https://www.facebook.com"
                        UserHashMap["Cover"] =
                            "https://firebasestorage.googleapis.com/v0/b/messengerapp-47f0d.appspot.com/o/cover.jpg?alt=media&token=fdd92af5-ad31-4523-ba4a-24cd21a173df"
                        UserHashMap["Search"] = username
                        UserHashMap["password"] = password
                        UserHashMap["email"]=email
                        refusers.updateChildren(UserHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent =
                                        Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                } else
                                    Toast.makeText(
                                        this,
                                        "error in signup" + task.exception!!.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                            }

                    } else
                        Toast.makeText(
                            this,
                            "error in signup" + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                }
        }
    }
}
