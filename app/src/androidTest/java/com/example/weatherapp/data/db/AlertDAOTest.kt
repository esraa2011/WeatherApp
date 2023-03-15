package com.example.weatherapp.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.models.AlarmPojo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class AlertDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var db : AppDataBase
    lateinit var dao : AlertDAO

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.AlertDAO()
    }

    @After
    fun tearDown() {
        // Close the DataBase
        db.close()
    }

    @Test
    fun getAllAlerts_insertItem_countOfItemSame() = runBlockingTest {
        //Given
        val data1 =AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)
        val data2 = AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)
        val data3 =AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)

        dao.insertAlert(data1)
        dao.insertAlert(data2)
        dao.insertAlert(data3)

        //when
        val result = dao.getAllAlerts().first()

        //Then
        MatcherAssert.assertThat(result.size , Is.`is`(3))
    }

    @Test
    fun insertAlert_insertSingleItem_returnItem()= runBlockingTest {
        //Given
        val data1 =AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)

        //when
        dao.insertAlert(data1)

        //Then
        val result = dao.getAllAlerts().first()
        MatcherAssert.assertThat(result[0], IsNull.notNullValue())
    }

    @Test
    fun deleteAlert_deleteItem_checkIsNull()  = runBlockingTest{
        // Given
        val data1 =AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)
        dao.insertAlert(data1)
        val outComData = dao.getAllAlerts().first()

        //when
        outComData[0].id?.let { dao.deleteAlert(it) }

        //Then
        val result = dao.getAllAlerts().first()
        assertThat(result, IsEmptyCollection.empty())
        assertThat(result.size , Is.`is`(0))
    }

    @Test
    fun getAlert_insertItem_getItem() = runBlockingTest{
        // Given
        val data1 =AlarmPojo(1,10000,1000000,1000000,"fsdfd",32.0,33.0)
        dao.insertAlert(data1)
        //When
        val outComData = data1.id?.let { dao.getAlert(it) }
        //Then
        if (outComData != null) {
            assertThat(data1.id, Is.`is`(outComData.id))
        }

    }
}