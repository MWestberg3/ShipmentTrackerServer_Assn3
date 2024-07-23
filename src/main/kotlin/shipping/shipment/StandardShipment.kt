package shipping.shipment

class StandardShipment(id: String): Shipment(id) {
    init {
        println("StandardShipment created with id: $id")
    }
}