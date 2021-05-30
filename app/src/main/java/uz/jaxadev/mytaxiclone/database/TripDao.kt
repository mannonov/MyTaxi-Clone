package uz.jaxadev.mytaxiclone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.jaxadev.mytaxiclone.model.TripModel

@Dao
interface TripDao {

    @Query("SELECT * FROM trip_table ORDER BY id DESC")
    fun queryAllTrips(): LiveData<List<TripModel>>

    @Insert
    fun insert(trips: ArrayList<TripModel>)

    @Query("SELECT * FROM trip_table WHERE id = :id")
    fun queryWithID(id: Int): LiveData<TripModel>

}