package com.hosam.messengerapp.ModelClasses

class Users {

    private var UId: String = ""
    private var Cover: String = ""
    private var Facebook: String = ""
    private var Profile: String = ""
    private var Status: String = ""
    private var UserName: String = ""
    private var Search:String=""

    constructor()
    constructor(
        UId: String = "",
        Cover: String = "",
        Facebook: String = "",
        Profile: String = "",
        Status: String = "",
        UserName: String = "",
        Search:String=""
    )
    {
        this.UId=UId
        this.Cover=Cover
        this.Facebook=Facebook
        this.Profile=Profile
        this.Status=Status
        this.UserName=UserName
        this.Search=Search

    }
    fun getsearch():String
    {
        return Search
    }
    fun setsearch(search:String){
        this.Search=search
    }
    fun getuid():String
    {
        return UId
    }
    fun setuid(uid:String){
        this.UId=uid
    }
    fun getcover():String
    {
        return Cover
    }
    fun setcover(cover:String){
        this.Cover=cover
    }
    fun getfacebook():String
    {
        return Facebook
    }
    fun setfacebook(facebook:String){
        this.Facebook=facebook
    }
    fun getprofile():String
    {
        return Profile
    }
    fun setprofile(profile:String){
        this.Profile=profile
    }
    fun getstatus():String
    {
        return Status
    }
    fun setstatus(status:String){
        this.Status=status
    }
    fun getusername():String
    {
        return UserName
    }
    fun setusername(username:String){
        this.UserName=username
    }

}