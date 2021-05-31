package uz.jaxadev.mytaxiclone.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "trip_table")
data class TripModel(

    @ColumnInfo(name = "tripId")
    val tripId: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "destination")
    val startPoint: String,

    @ColumnInfo(name = "stopAddress")
    val endPoind: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "paid")
    val paid: String,

    @ColumnInfo(name = "carModel")
    val carModel: String,

    @ColumnInfo(name = "carNumber")
    val carNumber: String,

    @ColumnInfo(name = "driverPhoneNumber")
    val driverPhoneNumber: String,

    @ColumnInfo(name = "tariff")
    val tariff: String,

    @ColumnInfo(name = "paymentType")
    val paymentType: String,

    @ColumnInfo(name = "startTime")
    val startTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "tripTime")
    val tripTime: String,

    @ColumnInfo(name = "distance")
    val distance: String,

    @ColumnInfo(name = "baseFare")
    val baseFare: String,

    @ColumnInfo(name = "rideFee")
    val rideFee: String,

    @ColumnInfo(name = "waitingFee")
    val waitingFee: String,

    @ColumnInfo(name = "driverName")
    val driverName: String,

    @ColumnInfo(name = "driverRating")
    val driverRating: String,

    @ColumnInfo(name = "driverTrips")
    val driverTrips: String

)