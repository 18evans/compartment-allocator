package evans18.assignment.fueltransport.data.manager

import evans18.assignment.fueltransport.data.model.TruckCarrier
import evans18.assignment.fueltransport.data.model.carrier.Carrier
import evans18.assignment.fueltransport.data.model.carrier.compartment.TruckCompartment
import org.springframework.stereotype.Service

@Service
class TruckManager : CarrierManager<TruckCarrier> {

    //todo note: hardcoded compartments, however imagine in future these are initiated by the unique to a specific truck's compartment via a DB Query where the request found the best truck for an order
    companion object {
        val BASE_TEST_CASE_COMPARTMENTS = listOf(
                TruckCompartment(4000.0),
                TruckCompartment(4000.0),
                TruckCompartment(3000.0),
                TruckCompartment(3000.0))
    }

    override fun getNextAvailable(): TruckCarrier {
        return TruckCarrier(BASE_TEST_CASE_COMPARTMENTS)
        /*
        todo: implement logic for keeping Truck availability.
           Perhaps some Dispatch event inserts into a local list here whenever a new truck is available?
           Note: in that case can probably remove the TruckManager class and use CarrierManager<T> as a class for all generics
         */
    }

}

interface CarrierManager<T : Carrier> {
    fun getNextAvailable(): T
}
