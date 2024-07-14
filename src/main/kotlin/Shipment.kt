class Shipment(
    var status: String,
    var id: String,
    notes: MutableList<String>,
    updateHistory: MutableList<ShippingUpdate>,
    var expectedDeliveryDate: Long,
    var currentLocation: String) {

    var notes: MutableList<String> = notes
        private set

    var updateHistory: MutableList<ShippingUpdate> = updateHistory
        private set

    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
    }
}