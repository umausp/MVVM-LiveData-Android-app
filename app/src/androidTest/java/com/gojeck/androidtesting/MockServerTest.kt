package com.gojeck.androidtesting

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gojeck.androidtesting.utils.ReadJsomnFromAsset
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class MockServerTest {

    @Test
    fun testMockedApi() {
        val mockServer = MockWebServer()
        mockServer.start()

        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(200)
        mockedResponse.setBody(ReadJsomnFromAsset.readJSONFromAsset().toString())

        mockedResponse.throttleBody(1024, 1, TimeUnit.SECONDS);

        mockServer.enqueue(mockedResponse)


        val request: RecordedRequest = mockServer.takeRequest()
        assertEquals("{}", request.body.readUtf8());

        mockServer.shutdown();
    }
}
