package com.example.weatherapp.ui.alert.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.data.repo.RepositoryOperation
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class AlertsViewModelTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    // Fake Data
    private var favoriteList: MutableList<FavoriteWeatherPlacesModel> =
        mutableListOf<FavoriteWeatherPlacesModel>(
            FavoriteWeatherPlacesModel("dfsfsd", 45.5, 65.05),

            FavoriteWeatherPlacesModel("dfsfsd", 45.5, 65.05),

            FavoriteWeatherPlacesModel("dfsfsd", 45.5, 65.05),

            FavoriteWeatherPlacesModel("dfsfsd", 45.5, 65.05)

        )
    private var alertList: MutableList<AlarmPojo> = mutableListOf<AlarmPojo>(
        AlarmPojo(12, 13, 12, 12,"egypt"),
        AlarmPojo(12, 13, 12, 12,"egypt"),
        AlarmPojo(12, 13, 12, 12,"egypt"),
        AlarmPojo(12, 13, 12, 12,"egypt"),
        AlarmPojo(12, 13, 12, 12,"egypt"),
        AlarmPojo(12, 13, 12, 12,"egypt"),

        )
    private var weatherResponse: Root =
        Root(1, 12.0, 13.0, "asdjadsk", 565, null, emptyList(), emptyList(), emptyList())
    private lateinit var repository: RepositoryOperation
    private lateinit var favouriteViewModel: FavoriteViewModel
    private lateinit var alertsViewModel:AlertsViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        alertsViewModel = AlertsViewModel(repository)
    }

    @Test
    fun getAllAlerts_checkEqualityOfSize() = runBlockingTest{
        //Given
        alertsViewModel.getAllAlerts()
        var data : List<AlarmPojo> = emptyList()
        //When
        val result = alertsViewModel.alertList.first()
        when(result){
            is AlertState.Success -> {
                data = result.data
            }
            is AlertState.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data.size , Is.`is`(6))
    }

    @Test
    fun insertAlert_insertItem_increaseSizeOfList() = runBlockingTest{
        //Given
        alertsViewModel.insertAlert(alertList[0])
        //When
        alertsViewModel.getAllAlerts()
        alertsViewModel.alertList.first()
        //Then
        MatcherAssert.assertThat(alertList.size , Is.`is`(7))
    }

    @Test
    fun deleteAlert_deleteItem_decreaseSizeOfList()= runBlockingTest{
        //Given
        alertsViewModel.deleteAlert(alertList[0])
        //When
        alertsViewModel.getAllAlerts()
        alertsViewModel.alertList.first()
        //Then
        MatcherAssert.assertThat(alertList.size , Is.`is`(5))
    }
}