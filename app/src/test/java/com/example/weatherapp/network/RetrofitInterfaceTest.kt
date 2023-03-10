package com.example.weatherapp.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.network.RetrofitHelper
import com.example.weatherapp.data.network.RetrofitInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RetrofitInterfaceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var retrofit: Retrofit

    lateinit var apiCalls: RetrofitInterface

    @Before
    fun setUp() {
        retrofit = RetrofitHelper.getInstance()
        apiCalls = retrofit.create(RetrofitInterface::class.java)

    }


    @Test
    fun getCurrentTempData() = runBlocking {
        //Given

        val latitude = 30.0
        val longitude = 32.0
        val appid = "bec88e8dd2446515300a492c3862a10e"
        val units = "metric"
        val lang = "en"

        //When
        val response = apiCalls.getCurrentTempData(
            latitude = latitude,
            longitude = longitude,
            appid = appid,
            units = units,
            lang = lang

        )
        //then
        assertThat(response.code() as Int, Is.`is`(200))
        assertThat(response.body() as Root, IsNull.notNullValue())
        assertThat(response.errorBody() ,IsNull.nullValue())
    }
}