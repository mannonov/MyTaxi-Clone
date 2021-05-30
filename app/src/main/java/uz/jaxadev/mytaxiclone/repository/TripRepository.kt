package uz.jaxadev.mytaxiclone.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.jaxadev.mytaxiclone.database.TripDao
import uz.jaxadev.mytaxiclone.service.MockApi

class TripRepository(val tripDao: TripDao) {

    val allTrips = tripDao.queryAllTrips()


    suspend fun fetchTrips() {


        withContext(Dispatchers.IO) {

            val tripModel = MockApi.mockService.getTrips()
            tripDao.insert(tripModel)


        }

    }

}