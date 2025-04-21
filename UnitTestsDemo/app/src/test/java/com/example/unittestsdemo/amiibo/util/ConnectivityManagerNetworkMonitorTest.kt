package com.example.unittestsdemo.amiibo.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.unittestsdemo.util.ConnectivityManagerNetworkMonitor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectivityManagerNetworkMonitorTest {

    private val connectivityManager: ConnectivityManager = mockk()
    private val ioDispatcher = StandardTestDispatcher()

    private lateinit var networkMonitor: ConnectivityManagerNetworkMonitor

    @Before
    fun setUp() {
        Dispatchers.setMain(ioDispatcher)

        // Mock the constructor of NetworkRequest.Builder
        mockkConstructor(NetworkRequest.Builder::class)

        // Create a mock NetworkRequest object
        val mockNetworkRequest: NetworkRequest = mockk(relaxed = true)

        // Mock the behavior of the Builder methods
        every { anyConstructed<NetworkRequest.Builder>().addCapability(any()) } returns mockk(
            relaxed = true
        )
        every { anyConstructed<NetworkRequest.Builder>().build() } returns mockNetworkRequest

        // Mock the context and connectivity manager
        val mockContext = mockk<Context>(relaxed = true) {
            every { getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        }

        // Mock registerNetworkCallback
        every {
            connectivityManager.registerNetworkCallback(
                any<NetworkRequest>(), any<ConnectivityManager.NetworkCallback>()
            )
        } answers {
            val callback = secondArg<ConnectivityManager.NetworkCallback>()
            // Simulate network callback behavior if needed
        }

        // Mock unregisterNetworkCallback
        every { connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>()) } just Runs

        // Mock getActiveNetworkInfo
        every {
            @Suppress("DEPRECATION")
            connectivityManager.getActiveNetworkInfo()
        } returns mockk(relaxed = true)

        // Initialize the network monitor
        networkMonitor = ConnectivityManagerNetworkMonitor(
            context = mockContext, ioDispatcher = ioDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isOnline emits true when network is available`() = runTest {
        // Mock a valid network
        val network: Network = mockk()

        // Mock network capabilities with internet capability
        val networkCapabilities: NetworkCapabilities = mockk {
            every { hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        }

        // Mock connectivity manager behavior
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities

        // Simulate the onAvailable callback
        every {
            connectivityManager.registerNetworkCallback(
                any<NetworkRequest>(), any<ConnectivityManager.NetworkCallback>()
            )
        } answers {
            val callback = secondArg<ConnectivityManager.NetworkCallback>()
            callback.onAvailable(network) // Simulate network becoming available
        }

        // Trigger the flow and collect the first emitted value
        val isOnline = networkMonitor.isOnline.first()

        // Assert that the emitted value is true
        assertEquals(true, isOnline)
    }

    @Test
    fun `isOnline emits false when no network is available`() = runTest {
        // Mock no active network
        every { connectivityManager.activeNetwork } returns null
        every { connectivityManager.getNetworkCapabilities(any()) } returns null

        // Simulate the onLost callback
        every {
            connectivityManager.registerNetworkCallback(
                any<NetworkRequest>(), any<ConnectivityManager.NetworkCallback>()
            )
        } answers {
            val callback = secondArg<ConnectivityManager.NetworkCallback>()
            callback.onLost(mockk()) // Simulate network being lost
        }

        // Trigger the flow and collect the first emitted value
        val isOnline = networkMonitor.isOnline.first()

        // Assert that the emitted value is false
        assertEquals(false, isOnline)
    }
}
