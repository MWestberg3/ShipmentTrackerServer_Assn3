class Shipment(
    var id: String,
)//: ShipmentSubject {
{
    var status: ShippingEventType = ShippingEventType.CREATED
    var shippingUpdateHistory: MutableList<ShippingUpdate> = mutableListOf()
        private set
    var notes: MutableList<String> = mutableListOf()
        private set
    var expectedDeliveryDate: Long? = null
    var currentLocation: String? = null

    // add a timestamp
    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        this.status = update.newStatus
        shippingUpdateHistory.add(update)
//        notifyObservers()
    }

//    @Override
//    override fun registerObserver(observer: ShipmentObserver) {
//        shippingUpdateHistory.add(observer)
//    }
//
//    @Override
//    override fun removeObserver(observer: ShipmentObserver) {
//        shippingUpdateHistory.remove(observer)
//    }
//
//    @Override
//    override fun notifyObservers() {
//        shippingUpdateHistory.forEach { it.notify(this) }
//    }
}