enum class ShippingEventType {
    CREATED,
    SHIPPED,
    LOCATION,
    DELIVERED,
    DELAYED,
    LOST,
    NOTE_ADDED,
    CANCELLED;

    companion object {
        fun from(status: String): ShippingEventType = when (status.uppercase()) {
            "CREATED" -> CREATED
            "SHIPPED" -> SHIPPED
            "LOCATION" -> LOCATION
            "DELIVERED" -> DELIVERED
            "DELAYED" -> DELAYED
            "LOST" -> LOST
            "NOTEADDED" -> NOTE_ADDED
            "CANCELLED" -> CANCELLED
            "CANCELED" -> CANCELLED
            else -> throw IllegalArgumentException("Invalid status")
        }
    }

    override fun toString(): String {
        return when (this) {
            CREATED -> "Created"
            SHIPPED -> "Shipped"
            LOCATION -> "Location"
            DELIVERED -> "Delivered"
            DELAYED -> "Delayed"
            LOST -> "Lost"
            NOTE_ADDED -> "Note Added"
            CANCELLED -> "Cancelled"
        }
    }
}