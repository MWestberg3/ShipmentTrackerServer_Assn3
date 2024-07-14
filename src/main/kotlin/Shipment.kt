class Shipment(
    var status: String,
    var id: String,
    notes: MutableList<String>,
    updateHistory: MutableList<ShippingUpdate>,
    var expectedDeliveryDate: Long,
    var currentLocation: String): ShipmentSubject {

    private val observers: MutableList<ShipmentObserver> = mutableListOf()

    var notes: MutableList<String> = notes
        private set

    var updateHistory: MutableList<ShippingUpdate> = updateHistory
        private set

    @Override
    override fun registerObserver(observer: ShipmentObserver) {
        observers.add(observer)
    }

    @Override
    override fun removeObserver(observer: ShipmentObserver) {
        observers.remove(observer)
    }

    @Override
    override fun notifyObservers() {
        observers.forEach { it.notify(1) }
    }

    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
    }
}