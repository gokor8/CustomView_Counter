package com.example.appcustomcounter.core.chain

import com.example.appcustomcounter.core.Handler

abstract class Chain<out HR> : Handler<HR> {
    protected abstract fun condition(): Boolean

    interface TriggeredChain<out TR> {
        fun isTriggered(): TR
    }

    interface NotTriggeredChain<out TR> {
        fun isNotTriggered(): TR
    }

    interface MutableTriggeredChain<out TC> : TriggeredChain<TC>, NotTriggeredChain<TC>

    abstract class BaseChain<out R> : Chain<R>(), MutableTriggeredChain<R> {

        override fun handle(): R =
            if (condition()) {
                isTriggered()
            } else {
                isNotTriggered()
            }
    }
}