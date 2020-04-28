package com.husseinelfeky.githubpaging.utils

import java.util.LinkedHashSet

//fun <T>MutableList<T>.removeDuplicates(): MutableList<T> {
//    val itemsSet: MutableSet<T> = LinkedHashSet()
//    for (item in this) {
//        itemsSet.add(item)
//    }
//    return itemsSet.toMutableList()
//}

fun <T>MutableList<T>.removeDuplicates() {
    val itemsSet: MutableSet<T> = LinkedHashSet()
    for (item in this) {
        itemsSet.add(item)
    }
    this.clear()
    this.addAll(itemsSet.toMutableList())
}