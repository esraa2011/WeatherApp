package com.example.weatherapp.ui.favorite.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.data.models.Root
import com.example.weatherapp.data.repo.FakeRepository
import com.example.weatherapp.data.repo.RepositoryOperation
import com.example.weatherapp.ui.home.viewModel.ApiStateRoot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
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
class FavoriteViewModelTest {
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
    private lateinit var favouriteViewModel: FavoriteViewModel

    @Before
    fun initFavouriteViewModelTest(){
        repository = FakeRepository(favoriteList,alertList,weatherResponse)
        favouriteViewModel = FavoriteViewModel(repository)
    }
    @Test
    fun insertFavoriteWeather_insertItem_increaseSizeOfList() = runBlockingTest{
        //Given
        favouriteViewModel.insertFavoriteWeather(favoriteWeatherPlacesModel = favoriteList[0])
        //When
        favouriteViewModel.getAllFavoritePlaces()

        favouriteViewModel.favList.first()
        //Then
        MatcherAssert.assertThat(favoriteList.size , `is` (5))
    }

    @Test
    fun deleteFavoriteWeather_deleteItem_decreaseSizeOfList()  = runBlockingTest{
        //Given
        favouriteViewModel.deleteFavoriteWeather(favoriteWeatherPlacesModel = favoriteList[0])
        //When
        favouriteViewModel.getAllFavoritePlaces()

        favouriteViewModel.favList.first()
        //Then
        MatcherAssert.assertThat(favoriteList.size , `is` (3))
    }
    @Test
    fun getAllFavoritePlacesDetails_checkIfNotNull()= runBlockingTest{
        //Given
        favouriteViewModel.getAllFavoritePlacesDetails(favoriteWeatherPlacesModel = favoriteList[0])
        var data : Root = weatherResponse
        //When
        val result = favouriteViewModel.root.first()
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
    @Test
    fun getAllFavouritePlaces_checkEqualityOfSize() = runBlockingTest{
        //Given
        favouriteViewModel.getAllFavoritePlaces()
        var data : List<FavoriteWeatherPlacesModel> = emptyList()
        //When
        val result = favouriteViewModel.favList.first()
        when(result){
            is ApiState.Success -> {
                data = result.data
            }
            is ApiState.Failure -> {
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(data.size , `is` (4))
    }


}