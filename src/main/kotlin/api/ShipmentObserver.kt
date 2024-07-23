package api

import shipping.shipment.Shipment

interface ShipmentObserver {
    fun notify(shipment: Shipment)
}