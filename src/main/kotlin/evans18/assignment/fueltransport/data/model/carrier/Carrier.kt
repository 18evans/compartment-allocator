package evans18.assignment.fueltransport.data.model.carrier

import evans18.assignment.fueltransport.data.model.carrier.compartment.Compartment
import evans18.assignment.fueltransport.data.model.carrier.compartment.TruckCompartment
import evans18.assignment.fueltransport.data.model.fuel.FuelType

interface Carrier {

    val compartments: Collection<Compartment>

    fun nextNonFullCompartmentFor(fuelType: FuelType, fromDepot: String): TruckCompartment?

}

