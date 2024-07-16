import java.io.File

class TrackingSimulator {
    private var shipments: MutableList<Shipment> = mutableListOf()

    fun findShipmentById(id: String): Shipment? {
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }
    fun runSimulation() {

    }
}