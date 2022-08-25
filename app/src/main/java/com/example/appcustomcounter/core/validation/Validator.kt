package com.example.appcustomcounter.core.validation

interface Validator<I : Any, R : Any> {

    var observe: ((I) -> R)?

}