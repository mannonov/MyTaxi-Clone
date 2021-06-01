package uz.jaxadev.mytaxiclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.jaxadev.mytaxiclone.database.TripDao
import uz.jaxadev.mytaxiclone.repository.TripRepository
import uz.jaxadev.util.GoogleApi
import uz.jaxadev.util.Result

class TripViewModel(private val tripDao: TripDao) : ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private val repo = TripRepository(tripDao)


    val allTrip = repo.allTrips

    init {

        fetchTrip()

    }

    suspend fun getDirection(origin: String, destination: String): LiveData<Result> {

        val direction = GoogleApi.googleService.getDirectionsAsync(
            "driving",
            "less_driving",
            origin,
            destination,
            "AIzaSyC2SEE5SOLqvgUUiUwIio6H_rDnD7mVMl0"
        ).await()

        Timber.d("$direction")

        return liveData {
            emit(direction)
        }


    }

    private fun fetchTrip() {


        viewModelScope.launch {

            repo.fetchTrips()


        }
    }
}