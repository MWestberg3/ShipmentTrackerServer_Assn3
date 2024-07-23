package shipping.shipment

class ExpressShipment(id: String): Shipment(id) {
    init {
        println("ExpressShipment created with id: $id")
    }
}