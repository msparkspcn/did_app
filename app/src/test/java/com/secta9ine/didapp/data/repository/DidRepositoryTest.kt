package com.secta9ine.didapp.data.repository

import com.secta9ine.didapp.data.local.DidDao
import com.secta9ine.didapp.data.remote.DidApi
import com.secta9ine.didapp.data.remote.DidItemDto
import com.secta9ine.didapp.data.remote.DidResponse
import com.secta9ine.didapp.util.AssetDownloader
import android.util.Log
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.After
import org.junit.Before
import org.junit.Test

class DidRepositoryTest {

    private lateinit var repository: DidRepository
    private val api: DidApi = mockk()
    private val dao: DidDao = mockk(relaxed = true)
    private val downloader: AssetDownloader = mockk()

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.d(any(), any<String>()) } returns 0
        
        repository = DidRepository(api, dao, downloader)
    }

    @After
    fun teardown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `syncWithRemote should fetch from API, save to DAO, and download assets`() = runTest {
        // Given: Mocking dependencies. Actual network or file system is not used.
        val items = listOf(
            DidItemDto("1", "IMAGE", "https://cdn.secta9ine.com/assets/banner_1920x1080.jpg?token=abc123"),
            DidItemDto("2", "TEXT", "Promotion: Buy 1 Get 1 Free!")
        )
        val response = DidResponse(items)
        val fileNameSlot = slot<String>()
        coEvery { api.getDidContent() } returns response
        coEvery { downloader.downloadFile(any(), capture(fileNameSlot)) } returns "/data/user/0/com.secta9ine.didapp/files/media/banner_1920x1080.jpg"

        // When
        repository.syncWithRemote()

        // Then
        coVerify { api.getDidContent() }
        coVerify { dao.deleteItemsNotIn(listOf("1", "2")) }
        coVerify { dao.insertItems(any()) }
        coVerify { downloader.downloadFile("https://cdn.secta9ine.com/assets/banner_1920x1080.jpg?token=abc123", any()) }
        coVerify { dao.updateLocalPath("1", "/data/user/0/com.secta9ine.didapp/files/media/banner_1920x1080.jpg") }
        assertFalse(fileNameSlot.captured.contains("?"))

        // Verify TEXT content doesn't trigger download
        coVerify(exactly = 0) { downloader.downloadFile(any(), match { it.contains("Promotion") }) }
    }

    @Test
    fun `syncWithRemote should clear all when API returns empty list`() = runTest {
        // Given
        coEvery { api.getDidContent() } returns DidResponse(emptyList())

        // When
        repository.syncWithRemote()

        // Then
        coVerify { dao.clearAll() }
        coVerify(exactly = 0) { dao.insertItems(any()) }
        coVerify(exactly = 0) { downloader.downloadFile(any(), any()) }
    }

    @Test
    fun `syncWithRemote should inject samples when API fails and DB is empty`() = runTest {
        // Given
        coEvery { api.getDidContent() } throws Exception("Network Error")
        coEvery { dao.getAllDidItemsSync() } returns emptyList()
        coEvery { downloader.downloadFile(any(), any()) } returns "/local/sample/path"

        // When
        repository.syncWithRemote()

        // Then
        coVerify { dao.insertItems(any()) } // Sample items injected
        coVerify { downloader.downloadFile(any(), any()) } // Sample assets downloaded
    }
}
