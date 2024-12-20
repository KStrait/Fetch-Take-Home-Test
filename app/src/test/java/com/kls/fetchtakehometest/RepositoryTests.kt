package com.kls.fetchtakehometest

import com.kls.fetchtakehometest.data.Data
import com.kls.fetchtakehometest.data.Result
import com.kls.fetchtakehometest.network.WebService
import com.kls.fetchtakehometest.repo.FetchRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RepositoryTests {

    private lateinit var repository: FetchRepository
    private lateinit var api: WebService

    @Before
    fun setup() {
        // Mock WebService
        api = mockk()
        // Initialize Repository with mocked API
        repository = FetchRepository(api)
    }

    @Test
    fun `getItems should fetch data correctly`() = runTest {
        // Given
        val apiResponse = listOf(
            Data(id = 755, listId = 2, name = ""),
            Data(id = 203, listId = 2, name = "Item B")
        )

        // Mock the repository's API response
        coEvery { api.getHiringItems() } returns apiResponse

        // When
        val resultList = repository.getHiringItems().toList()

        // Then
        when (val finalResult = resultList.last()) {
            is Result.Success -> {
                // Assert that the data inside Success is what you expect
                assertEquals(apiResponse, finalResult.data)
            }
            else -> fail("Expected Success but got $finalResult")
        }
    }
}