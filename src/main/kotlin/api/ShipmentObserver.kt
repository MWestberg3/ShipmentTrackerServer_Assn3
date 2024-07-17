package api

import shipping.Shipment

interface ShipmentObserver {
    fun notify(shipment: Shipment)
}