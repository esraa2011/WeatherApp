package com.example.weatherapp.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.models.Root
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

@ExperimentalCoroutinesApi
@SmallTest
class WeatherDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var db: AppDataBase
    lateinit var dao: WeatherDAO

    @Before
    fun initDB() {
        //init db
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.weatherDAO()
    }

    @After
    fun tearDown() {
        //close db
        db.close()
    }

    @Test
    fun insertLastWeather_insertItem_returnItem() = runTest {
        //Given
        val data = Root(1, 20.0, 32.0, "egypt", 55, null, emptyList(), emptyList())


        dao.insertLastWeather(data)

        //when
        val result = dao.getLastWeather().first()

        //Then

        MatcherAssert.assertThat(result.timezone, Is.`is`("egypt"))
    }

    @Test
    fun getLastWeather_insertWeather_CountOfItemsSame() = runBlockingTest {
        //Given
        val data = Root(1, 20.0, 32.0, "egypt", 55, null, emptyList(), emptyList())


        dao.insertLastWeather(data)

        //when
        val result = dao.getLastWeather().first()

        //Then

        MatcherAssert.assertThat(result, Is.`is`(data))
    }


    @Test
    fun deleteCurrentWeather_deleteItem_checkIsNull() = runBlockingTest() {

        //Given
        val data = Root(1, 20.0, 32.0, "egypt", 55, null, emptyList(), emptyList())
        dao.insertLastWeather(data)

        //When

        val result = dao.deleteCurrentWeather()
        //Then

        MatcherAssert.assertThat(result.toString(), true)
    }
}