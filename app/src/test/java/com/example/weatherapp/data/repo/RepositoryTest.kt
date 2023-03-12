package com.example.weatherapp.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var favoriteList: MutableList<FavoriteWeatherPlacesModel> =
        mutableListOf<FavoriteWeatherPlacesModel>(

            FavoriteWeatherPlacesModel("mansoura", 30.0, 32.0),
            FavoriteWeatherPlacesModel("oman", 50.0, 82.0)

        )
    private var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(
        AlarmPojo(12, 13, "1", "2", "wind")
    )
    private var rootList: MutableList<Root> = mutableListOf<Root>(
        Root(1, 20.0, 32.0, "mansoura", 55, null, emptyList(), emptyList())

    )
    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource
    private lateinit var repository: Repository

    @Before
    fun initialzeRepository() {
        remoteDataSource = FakeDataSource(favoriteList, alertList, rootList)
        localDataSource = FakeDataSource(favoriteList, alertList, rootList)
        repository = Repository(
            remoteDataSource, localDataSource,
            ApplicationProvider.getApplicationContext()
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertFavoritePlaces_insertItem_increaseSizeOfList() = runBlockingTest {
        //Given single item of favorite
        val item = FavoriteWeatherPlacesModel("egypt", 32.0, 43.0)
        //when insert favorite in room in repository
        repository.insertFavoritePlaces(item)
        //Then size of favorite list will be 3
        MatcherAssert.assertThat(favoriteList.size, Is.`is`(3))


    }

    @Test
    fun deleteFromFav_deleteItem_decreaseSizeOfTest() = runBlockingTest {
        //Given delete single  item of favorite
        val item = favoriteList[0]
        //when delete favorite in room in repository
        repository.deleteFromFav(item)
        //Then size of favorite list will be 1
        MatcherAssert.assertThat(favoriteList.size, Is.`is`(1))

    }

    @Test
    fun getFavoritePlaces_nothing_resultOfFavoriteListIsSameSize() = runBlockingTest {
        //Given
        //when request all  favorite in room in repository

        val result = repository.getFavoritePlaces().first()

        //Then size of favorite list will be same size  3
        MatcherAssert.assertThat(result.size, Is.`is`(favoriteList.size))


    }

    @Test
    fun getFavoriteWeather_Nothing_WeatherDetails() = runBlockingTest() {
        //Given

        //When request weather details from retrofit in repository
        val result = repository.getFavoriteWeather(
            favoriteWeatherPlacesModel = FavoriteWeatherPlacesModel(
                "mansoura",
                30.0,
                32.0
            )
        ).first()

        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(rootList[0]))
    }

    @Test
    fun getCurrentWeather_Nothing_CurrentWeather() = runBlockingTest {
        //Given

        //When request current weather
        val result = repository.getCurrentWeather().first()

        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(rootList[0]))

    }


    @Test
    fun insertCurrentWeather_insertItem_increaseSizeOfList() = runBlockingTest {
        //Given single item of current weather
        val item = Root(2, 20.0, 32.0, "suze", 55, null, emptyList(), emptyList())

        //when insert current weather in room in repository
        repository.insertCurrentWeather(item)

        //Then size of root  list will be 2
        MatcherAssert.assertThat(rootList.size, Is.`is`(2))


    }

    @Test
    fun deleteCurrentWeather_Nothing_DecreaseSizeOfList() = runBlockingTest {
        //Given

        //when delete current weather in room in repository
        val result = repository.deleteCurrentWeather()
        //Then size of root list will be 0
        MatcherAssert.assertThat(rootList.size, Is.`is`(0))

    }

    @Test
    fun getAlert_Nothing_ReturnAlert() = runBlockingTest {
        //Given

        //When request Alert
        val result = repository.getAlert().first()

        //Then response is same of fake data
        MatcherAssert.assertThat(result, Is.`is`(alertList))

    }

    @Test
    fun insertAlert_InsertItem_IncreaseSize() = runBlockingTest {
        //Given single item of alert
        val item = AlarmPojo(12, 13, "1", "2", "wind")

        //when insert alert in room in repository
        repository.insertAlert(item)

        //Then size of root  list will be 2
        MatcherAssert.assertThat(alertList.size, Is.`is`(2))

    }

    @Test
    fun deleteAlert() = runBlockingTest {
        //Given
        var item = alertList[0]
        //when delete  alert in room in repository
        val result = repository.deleteAlert(item)
        //Then size of alert list will be 0
        MatcherAssert.assertThat(alertList.size, Is.`is`(0))

    }


}