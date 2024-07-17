import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import Strategies.*
import kotlinx.coroutines.delay
import java.io.File

class TrackingSimulator {
    private var shipments: MutableList<Shipment> = mutableListOf()
    private var events: MutableList<ShippingEvent> = mutableListOf()

    fun findShipmentById(id: String): Shipment? {
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }
    private fun setEvents(events: List<ShippingEvent>) {
        this.events = events.toMutableList()
    }
    private fun strategyPicker5000(eventType: ShippingEventType): UpdateStrategy {
        return when (eventType) {
            ShippingEventType.CANCELLED -> CancelledStrategy()
            ShippingEventType.CREATED -> CreatedStrategy()
            ShippingEventType.DELAYED -> DelayedStrategy()
            ShippingEventType.DELIVERED -> DeliveredStrategy()
            ShippingEventType.LOCATION -> LocationStrategy()
            ShippingEventType.LOST -> LostStrategy()
            ShippingEventType.NOTE_ADDED -> NoteAddedStrategy()
            ShippingEventType.SHIPPED -> ShippedStrategy()
        }
    }
    private fun loadShipmentData() {
        val filepath = "/Users/mwestberg/IdeaProjects/test.txt"
        val shippingEvents = mutableListOf<ShippingEvent>()

        File(filepath).forEachLine { line ->
            val parts = line.split(",")
            val statusString = parts[0]
            val status = ShippingEventType.from(statusString)
            val id = parts[1]
            val timestamp = parts[2].toLong()
            val otherInfo = if (parts.size > 3) parts[3] else null

            shippingEvents.add(ShippingEvent(status, id, timestamp, otherInfo))
        }

        setEvents(shippingEvents)
    }

    suspend fun runSimulation() {
        loadShipmentData()
        events.forEach { event ->
            var shipment = findShipmentById(event.shipmentID)
            var isNewShipment = false
            val strategy = strategyPicker5000(event.type)
            if (shipment == null) {
                // error check for if shipment hasn't been created but trying to modify?
                shipment = Shipment(event.shipmentID)
                addShipment(shipment)
                isNewShipment = true
            }
            if (!isNewShipment) {
                delay(1000L)
            }
            strategy.processUpdate(shipment, event)
        }
    }
}