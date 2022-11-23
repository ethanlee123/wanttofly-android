@file:JvmName("Extensions")
package com.example.wanttofly.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
    val oldValue = this.value ?: mutableListOf()
    oldValue.add(item)
    this.value = oldValue
}
