class LocationUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        shipment.currentLocation = shipment.otherInfo!!
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}