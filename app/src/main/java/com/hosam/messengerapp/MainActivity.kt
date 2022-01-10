package com.hosam.messengerapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hosam.messengerapp.Fragments.SearchFragment
import com.hosam.messengerapp.Fragments.SettingFragment
import com.hosam.messengerapp.Fragments.chatsFragment
import com.hosam.messengerapp.ModelClasses.Users
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  var firebaseUsers:FirebaseUser?=null
  var refUsers:DatabaseReference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        supportActionBar!!.title=""
        val viewpageradapter=viewPagerAdapter(supportFragmentManager)
        viewpageradapter.addfragments(chatsFragment(),"Chats")
        viewpageradapter.addfragments(SearchFragment(),"Search")
        viewpageradapter.addfragments(SettingFragment(),"Setting")
        view_pager.adapter=viewpageradapter
        tab_layout.setupWithViewPager(view_pager)
        firebaseUsers=FirebaseAuth.getInstance().currentUser
        refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUsers!!.uid)
        refUsers!!.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user:Users?=p0.getValue(Users::class.java)
                    user_name.text=user!!.getusername()
                    Picasso.get().load(user.getprofile()).into(profileimage)

                }
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         when (item.itemId) {
            R.id.logout_settings -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
              return true

            }

        }

        return false
    }
    inner class viewPagerAdapter(fragmentmanager:FragmentManager):FragmentPagerAdapter(fragmentmanager){
        private val fragments:ArrayList<Fragment>
        private val titles:ArrayList<String>
        init {
            fragments=ArrayList<Fragment>()
            titles=ArrayList<String>()
        }
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
        fun addfragments(fragment:Fragment,title:String){
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}
