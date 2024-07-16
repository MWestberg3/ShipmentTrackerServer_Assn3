class ShippedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.expectedDeliveryDate)
        shipment.addUpdate(update)
    }
}