class NoteAddedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        shipment.addNote(shipment.otherInfo!!)
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}