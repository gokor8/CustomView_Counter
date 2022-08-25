package com.example.appcustomcounter.core.mapper

interface Mapper<I : Any, R : Any> {
    fun map(model: I) : R
}