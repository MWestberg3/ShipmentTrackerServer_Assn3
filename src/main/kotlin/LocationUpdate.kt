class LocationUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        shipment.currentLocation = shipment.otherInfo as String
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}