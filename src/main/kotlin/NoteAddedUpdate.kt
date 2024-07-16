class NoteAddedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.addNote(event.otherInfo!!)
    }
}