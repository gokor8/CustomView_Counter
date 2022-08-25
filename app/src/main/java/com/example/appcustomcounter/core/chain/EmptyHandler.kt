package com.example.appcustomcounter.core.chain

import com.example.appcustomcounter.core.Handler

class EmptyHandler<R>(private val empty: R) : Handler<R> {
    override fun handle(): R  = empty
}