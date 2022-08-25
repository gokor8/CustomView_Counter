package com.example.appcustomcounter.core.chain

import com.example.appcustomcounter.core.Handler

abstract class ConditionChain<out HR> : Handler<HR> {

    protected abstract fun condition(): Boolean

    open fun conditionWithExecute(): Boolean = condition()


    abstract class BaseConditionChain<out R> : ConditionChain<R>(), Chain.MutableTriggeredChain<R> {

        override fun conditionWithExecute(): Boolean {
            isNotTriggered()
            return condition()
        }

        override fun handle() = isTriggered()
    }
}
