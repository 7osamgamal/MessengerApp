package com.hosam.messengerapp.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hosam.messengerapp.AdapterClasses.UserAdapter
import com.hosam.messengerapp.ModelClasses.Users

import com.hosam.messengerapp.R
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null
    private var recyclerView:RecyclerView?=null
    var searchEditText:EditText?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView=view!!.findViewById(R.id.searchlist)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager=LinearLayoutManager(context)
        searchEditText=view.findViewById(R.id.usersearch)
        mUsers=ArrayList()
        retriveAllUsers()
        searchEditText!!.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchusers(s.toString().toLowerCase())
            }

        })
        return view
    }

    private fun retriveAllUsers() {
        var  firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid
        var refUsers=FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
               if (usersearch.text.toString()=="")
               {
                   for (snapshot in p0.children)
                   {
                       val user:Users?=snapshot.getValue(Users::class.java)
                       if (!(user!!.getuid()).equals(firebaseUserId))
                       {
                           (mUsers as ArrayList<Users>).add(user)
                       }
                   }
               }
                userAdapter= UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter=userAdapter
            }

        })
    }
    private fun searchusers(str:String)
    {
        var  firebaseUserId=FirebaseAuth.getInstance().currentUser!!.uid
        var QueryUsers=FirebaseDatabase.getInstance().reference.child("Users").orderByChild("Search")
            .startAt(str)
            .endAt(str+"\uf8ff")
        QueryUsers.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()


                    for (snapshot in p0.children)
                    {
                        val user:Users?=snapshot.getValue(Users::class.java)
                        if (!(user!!.getuid()).equals(firebaseUserId))
                        {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }

                userAdapter= UserAdapter(context!!,mUsers!!,false)
                recyclerView!!.adapter=userAdapter
            }

        })


    }

}
