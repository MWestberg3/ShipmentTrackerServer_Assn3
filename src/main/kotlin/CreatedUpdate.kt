class CreatedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        val update = ShippingUpdate(null, event.type, event.timestamp)
        shipment.addUpdate(update)
    }
}
