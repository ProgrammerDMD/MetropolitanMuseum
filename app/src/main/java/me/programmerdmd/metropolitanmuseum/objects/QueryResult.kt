package me.programmerdmd.metropolitanmuseum.objects

interface QueryResult<T> {

    fun onSuccess(result: T)
    fun onFailure(error: Exception)

}