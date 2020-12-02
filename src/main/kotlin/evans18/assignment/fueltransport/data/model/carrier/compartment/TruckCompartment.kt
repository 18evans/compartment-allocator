package evans18.assignment.fueltransport.data.model.carrier.compartment

import evans18.assignment.fueltransport.data.model.fuel.FuelType
import kotlin.math.min

/**
 * @property maxCapacity - max capacity in litres
 */
data class TruckCompartment(val maxCapacity: Double) : FuelCompartment {

    /**
     * Current taken capacity.
     */
    var currentVolume = 0.0
        private set

    var fuelType: FuelType? = null
        private set

    var fuelDepot: String? = null
        private set

    override val isFull
        get() = currentVolume == maxCapacity

    override val isNotFull
        get() = !isFull

    override val remainingCapacity
        get() = maxCapacity - currentVolume

    /**
     * @param fuelType - type of fuel to insert.
     * @param FuelDepot - depot from which to insert.
     * @param requestedVolume - volume of fuel requested to insert.
     *
     * @return the remainder of the fuel that was not inserted. Value 0 signifies fuel was fully inserted.
     */ //todo future note: logic is hardcoded to fill as much as possible, however, this might not necessary be the requirement -> ideally logic is elsewhere
    override fun insertFuel(fuelType: FuelType, fromDepot: String, requestedVolume: Double): Double {
        if (!allowsTypeAndDepot(fuelType, fromDepot))
            throw IllegalArgumentException("Doesn't match fuel type or same depot")

        if (isFull)
            return requestedVolume //already at max capacity

        //calculate insertion
        val insertionBatch = min(requestedVolume, remainingCapacity)

        //insert
        currentVolume += insertionBatch
        this.fuelType = fuelType
        this.fuelDepot = fromDepot

        return requestedVolume - insertionBatch //remaining
    }

    override fun allowsTypeAndDepot(type: FuelType, fromDepot: String): Boolean {
        return (this.fuelType == null || this.fuelDepot == null) || matchesFuelTypeAndDepot(type, fromDepot)
    }

    private fun matchesFuelTypeAndDepot(type: FuelType, fromDepot: String): Boolean {
        return containsSameFuel(type) && containsFromSameDepot(fromDepot)
    }

    private fun containsSameFuel(fuelType: FuelType) =
            this.fuelType == fuelType

    private fun containsFromSameDepot(fuelDepot: String) =
            this.fuelDepot == fuelDepot

}

interface FuelCompartment {
    val remainingCapacity: Double

    val isFull: Boolean

    val isNotFull: Boolean

    //todo future note: ideally I believe this logic should be in a separate loader manager or sth like that
    fun insertFuel(fuelType: FuelType, fromDepot: String, requestedVolume: Double): Double

    fun allowsTypeAndDepot(type: FuelType, fromDepot: String): Boolean
}
