package com.kls.fetchtakehometest.repo

import com.kls.fetchtakehometest.data.Data
import com.kls.fetchtakehometest.data.Result
import com.kls.fetchtakehometest.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchRepository @Inject constructor(private val webService: WebService) {

    fun getHiringItems(): Flow<Result<List<Data>>> = flow {
        emit(Result.Loading)
        try {
            val data = webService.getHiringItems()
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}