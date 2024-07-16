package strategies

import Shipment
import shippingEvents.ShippingEvent
import UpdateStrategy

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.addNote(event.otherInfo!!)
    }
}