package shipping.shipment

import ShippingEvents.ShippingEventType
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class OvernightShipment(id: String) : Shipment(id) {
    override var expectedDeliveryDate: Long? = null
        set(value) {
            field = if (value != null) {
                val createdTimeStamp =
                    shippingUpdateHistory.find { it.newStatus == ShippingEventType.CREATED }?.timestamp
                if (createdTimeStamp != null) {
                    val createdDate =
                        Instant.ofEpochMilli(createdTimeStamp).atZone(ZoneId.systemDefault()).toLocalDate()
                    val deliveryDate = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
                    val nextDay = createdDate.plusDays(1)
                    if (!deliveryDate.equals(nextDay) && !deliveryDate.equals(createdDate)) {
                        addNote("An overnight shipment was updated to include a delivery date later than 24 hours after it was created")
                    }
                }
                notifyObservers()
                value
            } else {
                null
            }
        }

    init {
        println("OvernightShipment created with id: $id")
    }
}