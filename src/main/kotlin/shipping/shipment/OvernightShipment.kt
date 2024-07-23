package shipping.shipment

class OvernightShipment(id: String): Shipment(id) {
    init {
        println("OvernightShipment created with id: $id")
    }
}