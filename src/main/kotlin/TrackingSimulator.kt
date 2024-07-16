import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import Strategies.*

class TrackingSimulator {
    private var shipments: MutableList<Shipment> = mutableListOf()
    private var events: MutableList<ShippingEvent> = mutableListOf()

    fun findShipmentById(id: String): Shipment? {
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }
    fun setEvents(events: List<ShippingEvent>) {
        this.events = events.toMutableList()
    }
    fun strategyPicker5000(eventType: ShippingEventType): UpdateStrategy {
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
    fun runSimulation() {
        events.forEach { event ->
            var shipment = findShipmentById(event.shipmentID)
            if (shipment == null) {
                // error check for if shipment hasn't been created but trying to modify?
                shipment = Shipment(event.shipmentID)
                addShipment(shipment)
            }
            val strategy = strategyPicker5000(event.type)
            strategy.processUpdate(shipment, event)
            // wait for one second
        }
    }
}