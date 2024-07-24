package shipping.shipment

import ShippingEvents.ShippingEventType
import api.ShipmentObserver
import api.ShipmentSubject
import shipping.ShippingUpdate

abstract class Shipment(
    var id: String,
): ShipmentSubject {
    var status: ShippingEventType = ShippingEventType.CREATED
    var shippingUpdateHistory: MutableList<ShippingUpdate> = mutableListOf()
        private set
    var notes: MutableList<String> = mutableListOf()
        private set
    open var expectedDeliveryDate: Long? = null
    var currentLocation: String = "unknown"
    var observers: MutableList<ShipmentObserver> = mutableListOf()

    // add a timestamp
    fun addNote(note: String) {
        notes.add(note)
        notifyObservers()
    }

    fun addUpdate(update: ShippingUpdate) {
        shippingUpdateHistory.add(update)
        notifyObservers()
    }

    @Override
    override fun registerObserver(observer: ShipmentObserver) {
        observers.add(observer)
    }

    @Override
    override fun removeObserver(observer: ShipmentObserver) {
        observers.remove(observer)
    }

    @Override
    override fun notifyObservers() {
        observers.forEach { it.notify(this) }
    }
}