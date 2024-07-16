class Shipment(
    var status: String,
    var id: String,
    var timestamp: Long,
    var otherInfo: Any?)//: ShipmentSubject {
{
    private var shippingUpdate: UpdateStrategy = when (status) {
        "created" -> CreatedUpdate()
        "shipped" -> ShippedUpdate()
        else -> throw IllegalArgumentException("Invalid status")
    }
    init {
        shippingUpdate.processUpdate(this)
    }

    lateinit var shippingUpdateHistory: MutableList<ShippingUpdate>
        private set

    lateinit var notes: MutableList<String>
        private set

    var expectedDeliveryDate: Long = 0

    var currentLocation: String = "unknown"

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

    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        shippingUpdateHistory.add(update)
//        notifyObservers()
    }
}