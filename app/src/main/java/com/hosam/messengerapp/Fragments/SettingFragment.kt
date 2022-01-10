package com.hosam.messengerapp.Fragments


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.hosam.messengerapp.ModelClasses.Users
import com.hosam.messengerapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.view.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class SettingFragment : Fragment() {
    var userreference: DatabaseReference? = null
    var firebaseuser: FirebaseUser? = null
    private val RequestCode = 423
    private var imageuri: Uri? = null
    private var storageref: StorageReference? = null
    private var covercheck: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_setting, container, false)
        firebaseuser = FirebaseAuth.getInstance().currentUser
        userreference =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseuser!!.uid)
        storageref = FirebaseStorage.getInstance().reference.child("UserImages")
        userreference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)
                    if (context != null) {
                        view.username_setting.text = user!!.getusername()
                        Picasso.get().load(user.getprofile()).into(view.profile_Image)
                        Picasso.get().load(user.getcover()).into(view.cover_image)

                    }
                }
            }

        })

        view.profile_Image.setOnClickListener {
            pickupImage()
        }
        view.cover_image.setOnClickListener {
            covercheck = "cover"
            pickupImage()
        }
        return view
    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode && requestCode == Activity.RESULT_OK && data != null) {
            imageuri = data.data
            Toast.makeText(context, "uploading.......", Toast.LENGTH_LONG).show()
            uploadImageToDatabase()

        }
    }

    private fun uploadImageToDatabase() {
        val progrssbar = ProgressDialog(context)
        progrssbar.setMessage("image is uploading ,please wait....")
        progrssbar.show()
        if (imageuri != null) {
            val fileref = storageref!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileref.putFile(imageuri!!)
            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }

                }
                return@Continuation fileref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadurl = task.result
                    val url = downloadurl.toString()
                    if (covercheck == "cover") {
                        val mapcoverimage = HashMap<String, Any>()
                        mapcoverimage["Cover"] = url
                        userreference!!.updateChildren(mapcoverimage)
                        covercheck = ""
                    } else {
                        val mapprofileimage = HashMap<String, Any>()
                        mapprofileimage["Profile"] = url
                        userreference!!.updateChildren(mapprofileimage)
                        covercheck = ""

                    }
                    progrssbar.dismiss()

                }
            }
        }

    }
}
