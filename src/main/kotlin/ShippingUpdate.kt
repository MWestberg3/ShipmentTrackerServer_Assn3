class ShippingUpdate(
    var newStatus: String,
    var timestamp: Long
): ShipmentObserver {
    lateinit var previousStatus: String

    @Override
    override fun notify(newStatus: String, timestamp: Long) {
        previousStatus = this.newStatus
        this.newStatus = newStatus
        this.timestamp = timestamp
    }
}
