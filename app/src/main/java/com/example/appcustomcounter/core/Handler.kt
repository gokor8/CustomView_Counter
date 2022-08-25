package com.example.appcustomcounter.core

interface Handler<out R> {

    fun handle(): R
}