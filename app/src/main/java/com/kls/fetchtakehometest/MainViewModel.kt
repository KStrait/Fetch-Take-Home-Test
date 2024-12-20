package com.kls.fetchtakehometest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kls.fetchtakehometest.data.Data
import com.kls.fetchtakehometest.data.Result
import com.kls.fetchtakehometest.repo.FetchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val fetchRepository: FetchRepository): ViewModel() {

    private val _groupedItems = MutableStateFlow<Result<Map<Int, List<Data>>>>(Result.Loading)
    val groupedItems: StateFlow<Result<Map<Int, List<Data>>>> = _groupedItems

    init {
        viewModelScope.launch {
            fetchRepository.getHiringItems()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _groupedItems.value = Result.Loading
                        }
                        is Result.Success -> {
                            val data = result.data

                            // Filter out items where 'name' is null or blank
                            val filteredData = data.filter { !it.name.isNullOrBlank() }

                            // Sort by 'listId' and then by 'name'
                            val sortedData = filteredData.sortedWith(
                                compareBy<Data> { it.listId }.thenBy {
                                    // Extract the number from 'name' (e.g., "Item 58" -> 58)
                                    it.name?.let { name ->
                                        val number = name.removePrefix("Item ").toIntOrNull() // Extract the integer after "Item "
                                        number ?: Int.MAX_VALUE // If parsing fails, send it to the end
                                    } ?: Int.MAX_VALUE // If 'name' is null, send it to the end
                                }
                            )

                            // Group the data by 'listId'
                            val groupedData = sortedData.groupBy { it.listId }

                            // Emit the grouped and sorted data
                            _groupedItems.value = Result.Success(groupedData)
                        }
                        is Result.Error -> {
                            // Emit error if there was an issue fetching the data
                            _groupedItems.value = Result.Error(result.exception)
                        }
                    }
                }
        }
    }
}