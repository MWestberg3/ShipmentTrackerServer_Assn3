data class ShippingUpdate(
    var previousStatus: ShippingEventType?,
    var newStatus: ShippingEventType,
    var timestamp: Long
)
