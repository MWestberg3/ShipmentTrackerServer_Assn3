package shipping.shipment

class BulkShipment(id: String): Shipment(id) {
    init {
        println("BulkShipment created with id: $id")
    }
}