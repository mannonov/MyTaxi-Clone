package uz.jaxadev.mytaxiclone.model

data class TripModel(

    val destination: String,

    val stopAddress: String,

    val date: String,

    val paid: String,

    val carModel: String,

    val carNumber: String,

    val driverPhoneNumber: String,

    val tariff: String,

    val paymentType:String,

    val startTime: String,

    val order:String,

    val endTime: String,

    val tripTime: String,

    val baseFare: String,

    val rideFee: String,

    val waitingFee: String,

    val surge: String,

    val total: String,

    val driverName: String,

    val driverRating: String,

    val driverTrips: String

)