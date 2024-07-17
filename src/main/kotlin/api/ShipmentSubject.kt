package api

interface ShipmentSubject {
    fun registerObserver(observer: ShipmentObserver)
    fun removeObserver(observer: ShipmentObserver)
    fun notifyObservers()
}