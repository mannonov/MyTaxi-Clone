package uz.jaxadev.mytaxiclone.service

import uz.jaxadev.mytaxiclone.model.TripModel

class MockService {

    private lateinit var trips: ArrayList<TripModel>

    fun getTrips(): ArrayList<TripModel> {

        trips = ArrayList()

//        trips.add(
//            TripModel(
//                tripId = 1,
//                startPoint = "Olmozor Nurafshon aylanma ko'chasi 48",
//                endPoind = "Shayxontohur, Xadra",
//                date = "2021-02-24 15:04",
//                paid = "7,900 som",
//                carModel = "White Lacetti",
//                carNumber = "01 A 939 NA",
//                driverPhoneNumber = "+998938290012",
//                tariff = "Economy",
//                paymentType = "Payme",
//                distance = "18.3 km",
//                startTime = "15:14",
//                endTime = "15:19",
//                tripTime = "00:04",
//                baseFare = "5,000 som",
//                rideFee = "2,008 som",
//                waitingFee = "873 som",
//                driverName = "Maxamadjon",
//                driverRating = "4.8",
//                driverTrips = "858"
//            )
//        )

//        trips.add(
//            TripModel(
//                tripId = 1,
//                startPoint = "Olmozor tumani Qorasaroy ko'cha",
//                endPoind = "Shayxontohur tumani Sebzor ko'chasi",
//                date = "2021-05-28 08:28",
//                paid = "10,800 som",
//                carModel = "White Nexia",
//                carNumber = "01 Z 884 YA",
//                distance = "18.3 km",
//                driverPhoneNumber = "+998909666335",
//                tariff = "Economy",
//                paymentType = "Cash",
//                startTime = "08:32",
//                endTime = "08:38",
//                tripTime = "00:05",
//                baseFare = "5,000 som",
//                rideFee = "1,752 som",
//                waitingFee = "0 som",
//                driverName = "Timur",
//                driverRating = "4.9",
//                driverTrips = "1133"
//            )
//        )
        return trips
    }

}


object MockApi {
    val mockService = MockService()
}