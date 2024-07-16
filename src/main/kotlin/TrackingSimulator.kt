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
            ShippingEventType.CANCELLED -> CancelledUpdate()
            ShippingEventType.CREATED -> CreatedUpdate()
            ShippingEventType.DELAYED -> DelayedUpdate()
            ShippingEventType.DELIVERED -> DeliveredUpdate()
            ShippingEventType.LOCATION -> LocationUpdate()
            ShippingEventType.LOST -> LostUpdate()
            ShippingEventType.NOTE_ADDED -> NoteAddedUpdate()
            ShippingEventType.SHIPPED -> ShippedUpdate()
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