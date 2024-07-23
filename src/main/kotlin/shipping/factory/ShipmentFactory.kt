package shipping.factory

import shipping.shipment.Shipment

abstract class ShipmentFactory {
    protected abstract fun createShipment(type: String, id: String): Shipment
}