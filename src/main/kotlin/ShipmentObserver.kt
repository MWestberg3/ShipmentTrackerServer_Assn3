interface ShipmentObserver {
    fun notify(newStatus: String, timestamp: Long)
}