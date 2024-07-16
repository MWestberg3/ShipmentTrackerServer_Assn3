import ShippingEvents.ShippingEventType

class Shipment(
    var id: String,
): ShipmentSubject {
    var status: ShippingEventType = ShippingEventType.CREATED
    var shippingUpdateHistory: MutableList<ShippingUpdate> = mutableListOf()
        private set
    var notes: MutableList<String> = mutableListOf()
        private set
    var expectedDeliveryDate: Long? = null
    var currentLocation: String? = null
    var observers: MutableList<ShipmentObserver> = mutableListOf()

    // add a timestamp
    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        this.status = update.newStatus
        shippingUpdateHistory.add(update)
        notifyObservers()
    }

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
        observers.forEach { it.notify(this) }
    }
}