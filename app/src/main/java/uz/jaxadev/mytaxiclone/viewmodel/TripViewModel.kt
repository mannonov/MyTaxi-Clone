package uz.jaxadev.mytaxiclone.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import uz.jaxadev.mytaxiclone.database.TripDao
import uz.jaxadev.mytaxiclone.repository.TripRepository

class TripViewModel(private val tripDao: TripDao) : ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + job)

    private val repo = TripRepository(tripDao)


    val allTrip = repo.allTrips

    init {

        fetchTrip()

    }

    private fun fetchTrip() {


        viewModelScope.launch {

            repo.fetchTrips()


        }
    }
}