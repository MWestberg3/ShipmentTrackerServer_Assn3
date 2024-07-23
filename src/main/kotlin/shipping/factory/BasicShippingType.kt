package shipping.factory

import shipping.shipment.*

class BasicShippingType: ShipmentFactory() {
    public override fun createShipment(type: String, id: String): Shipment {
        return when (type.lowercase()) {
            "standardshipment", "standard shipment" -> StandardShipment(id)
            "expressshipment", "express shipment" -> ExpressShipment(id)
            "overnightshipment", "overnight shipment" -> OvernightShipment(id)
            "bulkshipment", "bulk shipment" -> BulkShipment(id)
            else -> throw IllegalArgumentException("Invalid shipment type: $type")
        }
    }
}