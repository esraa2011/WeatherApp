package com.example.weatherapp.ui.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.repo.RepositoryOperation
import com.example.weatherapp.ui.favorite.viewModel.ApiState
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)

class HomeViewModelTest {
    @ExperimentalCoroutinesApi

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
        AlarmPojo(12, 13, "1", "2", "wind"),
        AlarmPojo(12, 13, "1", "2", "wind"),
        AlarmPojo(12, 13, "1", "2", "wind"),
        AlarmPojo(12, 13, "1", "2", "wind"),
        AlarmPojo(12, 13, "1", "2", "wind"),
        AlarmPojo(12, 13, "1", "2", "wind")

    )
    private var weatherResponse: Root =
        Root(1, 12.0, 13.0, "asdjadsk", 565, null, emptyList(), emptyList())



    private lateinit var repository: RepositoryOperation
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        homeViewModel = HomeViewModel(repository)
    }
    @Test
    fun getCurrentWeather() = runBlockingTest{
        //Given
        homeViewModel.getWeather(LatLng(33.0, 32.0))
        var data : Root = weatherResponse
        //When
        val result = homeViewModel.root.first()
        when(result){
            is ApiStateRoot.Success -> {
                data = result.data
            }
            is ApiStateRoot.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data , IsNull.notNullValue())
    }
}