package api

import Shipment

interface ShipmentObserver {
    fun notify(shipment: Shipment)
}