package shipping.shipment

import ShippingEvents.ShippingEventType
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class BulkShipment(id: String): Shipment(id) {
    override var expectedDeliveryDate: Long? = null
        set(value) {
            field = if (value != null) {
                val createdTimeStamp =
                    shippingUpdateHistory.find { it.newStatus == ShippingEventType.CREATED }?.timestamp
                if (createdTimeStamp != null) {
                    val createdDate =
                        Instant.ofEpochMilli(createdTimeStamp).atZone(ZoneId.systemDefault()).toLocalDate()
                    val deliveryDate = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
                    if (ChronoUnit.DAYS.between(createdDate, deliveryDate) < 3) {
                        addNote("A bulk shipment was updated to include a delivery date that is less than 3 days from creation date")
                    }
                }
                notifyObservers()
                value
                } else {
                    null
                }
            }

    init {
        println("BulkShipment created with id: $id")
    }
}