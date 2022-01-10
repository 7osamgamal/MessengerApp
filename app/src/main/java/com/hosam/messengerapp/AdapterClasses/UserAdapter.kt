package com.hosam.messengerapp.AdapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hosam.messengerapp.ModelClasses.Users
import com.hosam.messengerapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(mcontext: Context, muser: List<Users>, isChatCheck: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {
    private var mcontext: Context
    private var musers: List<Users>
    private var isChatCheck: Boolean

    init {
        this.mcontext = mcontext
        this.musers = muser
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(mcontext).inflate(R.layout.user_search_item, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user: Users = musers[position]
        holder.username.text = user.getusername()
        Picasso.get().load(user.getprofile())
            .into(holder.profileImage)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var profileImage: CircleImageView
        var username: TextView
        var onlineprofile: CircleImageView
        var offlineprofile: CircleImageView
        var lastmessagetxt: TextView

        init {
            profileImage = view.findViewById(R.id.profile_Image)
            username = view.findViewById(R.id.user_name)
            onlineprofile = view.findViewById(R.id.image_online)
            offlineprofile = view.findViewById(R.id.image_offline)
            lastmessagetxt = view.findViewById(R.id.message_last)
        }
    }
}