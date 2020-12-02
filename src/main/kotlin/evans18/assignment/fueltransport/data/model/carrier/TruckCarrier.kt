package evans18.assignment.fueltransport.data.model

import evans18.assignment.fueltransport.data.model.carrier.Carrier
import evans18.assignment.fueltransport.data.model.carrier.compartment.TruckCompartment
import evans18.assignment.fueltransport.data.model.fuel.FuelType

data class TruckCarrier(
        /**
         * The compartments in this truck.
         *
         * Note: 1) Each truck might have different set of compartments.
         *         2) Immutable because a truck should not have its compartments changed in runtime (... i think)
         */
        override val compartments: List<TruckCompartment>
) : Carrier {

    /**
     * Gets the next compartment that is note empty.
     * @see [TruckCompartment.isFull]
     * @return the direct object reference to the truck compartment or null if all compartments are full
     */
    //todo ideally logic should be in a separate loader manager or sth like that
    override fun nextNonFullCompartmentFor(fuelType: FuelType, fromDepot: String): TruckCompartment? {
        return compartments.firstOrNull {
            it.allowsTypeAndDepot(fuelType, fromDepot) && !it.isFull//it.remainingCapacity >= requestedVolume
        }
    }
}