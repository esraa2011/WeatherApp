package com.example.weatherapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.Is
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

@ExperimentalCoroutinesApi
@SmallTest
class FavoriteWeatherPlacesDAOTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var db: AppDataBase
    lateinit var dao: FavoriteWeatherPlacesDAO

    @Before
    fun initDB() {
        //init db
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.favoriteWeatherPlacesDAO()
    }


    @After
    fun tearDown() {
        //close db
        db.close()
    }


    @Test
    fun getAllFavoriteWeatherPlaces_insertFav_countofitemsSame() = runBlockingTest {
        //Given
        val data = FavoriteWeatherPlacesModel("mansoura", 30.0, 30.0)
        val data2 = FavoriteWeatherPlacesModel("Ismailia", 30.0, 32.0)


        dao.addPlaceToFavorite(data)
        dao.addPlaceToFavorite(data2)

        //when
        val result = dao.getAllFavoriteWeatherPlaces().first()

        //Then

        MatcherAssert.assertThat(result.size, Is.`is`(2))
    }

    @Test
    fun addPlaceToFavorite_insertItem_returnItem() = runBlockingTest {
        //Given
        val data = FavoriteWeatherPlacesModel("mansoura", 30.0, 30.0)

        //When
        dao.addPlaceToFavorite(data)

        //Then

        val result = dao.getAllFavoriteWeatherPlaces().first()
        MatcherAssert.assertThat(result.get(0), IsNull.notNullValue())

    }

    @Test
    fun deletePlaceFromFavorite_deleteItem_checkIsNull() = runBlockingTest() {

        //Given
        val data = FavoriteWeatherPlacesModel("mansoura", 30.0, 30.0)
        dao.addPlaceToFavorite(data)
        val outcomeData = dao.getAllFavoriteWeatherPlaces().first()
        //When

        dao.deletePlaceFromFavorite(outcomeData[0])
        //Then

        val result = dao.getAllFavoriteWeatherPlaces().first()
        MatcherAssert.assertThat(result, IsEmptyCollection.empty())
    }
}
