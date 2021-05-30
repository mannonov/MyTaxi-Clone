package uz.jaxadev.mytaxiclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.jaxadev.mytaxiclone.database.TripDao

class TripViewModelFactory(private val tripDao: TripDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            return TripViewModel(tripDao = tripDao) as T
        }
        throw IllegalArgumentException("infoViewModeldan boshqasini tekshirdi")

    }


}