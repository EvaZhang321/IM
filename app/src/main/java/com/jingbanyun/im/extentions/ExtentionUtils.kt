package com.jingbanyun.im.extentions

fun String.isValidUserName():Boolean = this.matches(kotlin.text.Regex("^[a-zA-Z]\\w{2,19}$"))
fun String.isValidPassword():Boolean = this.matches(kotlin.text.Regex("^[0-9]{3,20}$"))

fun<K,V>MutableMap<K,V>.toVarargArray():Array<Pair<K,V>>{
    //将MutableMap转化为Pair类型的数组
    return map{
        Pair(it.key,it.value)
    }.toTypedArray()
}