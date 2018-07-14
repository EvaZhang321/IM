package com.jingbanyun.im.data.db

data class Contact(val map:MutableMap<String,Any?>) {
    //_id和name都是从map集合中获取
    val _id by map
    val name by map
}