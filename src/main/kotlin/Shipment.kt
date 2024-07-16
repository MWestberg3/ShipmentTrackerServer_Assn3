class Shipment(
    var status: String,
    var id: String,
    var timestamp: Long,
    var otherInfo: String?)//: ShipmentSubject {
{
    private var shippingUpdate: UpdateStrategy = when (status) {
        "created" -> CreatedUpdate()
        "shipped" -> ShippedUpdate()
        "location" -> LocationUpdate()
        "delivered" -> DeliveredUpdate()
        "delayed" -> DelayedUpdate()
        "lost" -> LostUpdate()
        "cancelled" -> CancelledUpdate()
        "canceled" -> CancelledUpdate()
        "noteadded" -> NoteAddedUpdate()
        else -> throw IllegalArgumentException("Invalid status")
    }
    var shippingUpdateHistory: MutableList<ShippingUpdate> = mutableListOf()
        private set

    var notes: MutableList<String> = mutableListOf()
        private set

    var expectedDeliveryDate: Long? = null

    var currentLocation: String? = null

    init {
        shippingUpdate.processUpdate(this)
    }

    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        
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