package com.example.appcustomcounter.core.mapper

interface FunMapper<I : Any> {

    fun <R : Any> map(model: Mapper<I, R>) : R
}