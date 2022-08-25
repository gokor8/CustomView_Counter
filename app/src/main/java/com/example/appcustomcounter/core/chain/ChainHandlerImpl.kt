package com.example.appcustomcounter.core.chain

import com.example.appcustomcounter.core.Handler

class ChainHandlerImpl<out T>(
    private val start: ConditionChain<T>,
    private val next: Handler<T>
) : Handler<T> {

    override fun handle(): T =
        if (start.conditionWithExecute()) {
            start.handle()
        } else {
            next.handle()
        }
}