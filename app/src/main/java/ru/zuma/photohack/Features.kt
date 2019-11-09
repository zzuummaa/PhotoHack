package ru.zuma.photohack

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun launch(runnable: ()->Unit) = GlobalScope.launch {
    try {
        runnable.invoke()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}