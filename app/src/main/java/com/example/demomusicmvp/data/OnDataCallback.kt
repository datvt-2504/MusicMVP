package com.example.demomusicmvp.data

interface OnDataCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(throwable: Throwable)
}
