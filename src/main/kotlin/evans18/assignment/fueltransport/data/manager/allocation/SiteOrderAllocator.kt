package evans18.assignment.fueltransport.data.manager.allocation

import evans18.assignment.fueltransport.data.model.carrier.Carrier
import evans18.assignment.fueltransport.data.model.carrier.compartment.Compartment
import evans18.assignment.fueltransport.data.model.fuel.FuelOrder
import evans18.assignment.fueltransport.data.model.fuel.FuelType
import evans18.assignment.fueltransport.data.model.fuel.convertFromCubicMetresToLiters
import evans18.assignment.fueltransport.data.model.request.SiteOrder
import org.springframework.stereotype.Service
import java.util.*


/**
 * An implementation of managers that allocate distribution of content within objects that carry it.
 * Note: Specific implementation for Trucks
 * @see [Carrier]
 */
@Service
class SiteOrderAllocator {

    /**
     * Allocates the given orders to the carrier following the given fitting type
     */
    fun <T : Carrier> calculateAllocation(carrier: T, orders: List<SiteOrder>, type: AllocationType): T {
//        if (type == AllocationType.CARRY_ALL_TOGETHER) { //todo: use OOP principles instead of checks
        return carryAllTogether(carrier, orders)
//        }
    }

    //todo: move to separate class
    private fun <T : Carrier> carryAllTogether(truckCarrier: T, orders: List<SiteOrder>): T {
        //flatten only to the FuelOrders
        val flattenedFuelOrders: List<FuelOrder> = orders.flatMap {
            it.fuelOrders
        }.onEach {
            convertFromCubicMetresToLiters(it) //replace format of cubic meters to only litres
        }

        //group by fuel type and depot, summing the total requested fuel for that fuel & depot
        val mapDepotFuelDemand = flattenedFuelOrders.groupingBy {
            it.fuelType to it.depot //concat to use as key
        }.fold(0.0) { accumulator, element ->
            accumulator + element.volume //sum
        }

        //sort by demand and put in Queue (time complexity: N log N    where n is each unique pair of gasoline and depot
        val qFuelDepotDemand = PriorityQueue<Map.Entry<Pair<FuelType, String>, Double>>(
                compareByDescending { //largest first
                    it.value
                }
        ).apply {
            addAll(mapDepotFuelDemand.entries)
        }

        //sort Truck's Compartments by remaining space and place in Queue (C log C   where C == count of compartments)
        val qCompartments = PriorityQueue<Compartment>(
                compareByDescending { //most capacity first
                    it.remainingCapacity
                }
        ).apply {
            addAll(truckCarrier.compartments)
        }

        //time complexity worst case is (N x C) i believe

        while (qFuelDepotDemand.isNotEmpty() && qCompartments.isNotEmpty()) { //still demand AND available compartments

            with(qFuelDepotDemand.poll()) { //get next most in demand fuel/depot entry
                let { (pairFuelDepot, requestedVolume) ->
                    pairFuelDepot.let { (fuelType, depot) ->
                        var compartment: Compartment

                        val notFittingCompartments = mutableListOf<Compartment>()
                        //get next most-empty compartment that can ALSO be allowed to be in the compartment
                        do {
                            compartment = qCompartments.poll()
                            //track as unfit
                            if (compartment.allowsTypeAndDepot(fuelType, depot)) break //found fit
                            else notFittingCompartments.add(compartment) //track as unfit
                        } while (qCompartments.isNotEmpty())

                        if (!compartment.allowsTypeAndDepot(fuelType, depot) && qCompartments.isEmpty()) { // no fits AND no more compartments
                            //no room in this truck for any more of this fuel type/depot entry

                            qCompartments.addAll(notFittingCompartments) //the skipped compartments might fit another fuel type/depot
                            return@with //skip entry
                            //todo future note: use identifier between orders to show which specific orders were denied
                        }
                        qCompartments.addAll(notFittingCompartments) //the skipped compartments might fit another fuel type/depot

                        //insert fuel into compartment then return remaining requested volume
                        compartment.insertFuel(fuelType, depot, requestedVolume).also {
                            if (compartment.isNotFull) //still has capacity
                                qCompartments.add(compartment) //add again
                        }
                    }.also { remainingRequestedVolume -> //new now reduced (post-insertion) demand
                        if (remainingRequestedVolume != 0.0) { //still remains demand for this fuel/depot entry
                            val copyEntry = AbstractMap.SimpleEntry(pairFuelDepot, remainingRequestedVolume)
                            qFuelDepotDemand.add(copyEntry) //add back to q
                        }
                    }
                }
            }

        }

        return truckCarrier
    }

}

/*
  //todo: example class to be used if desired to show how trips were distributed given an allocation attempt

    class AllocationCalculation(
            /**
             * The
             */
            val proposedAllocation: TruckCarrier,
            /**
             * All [FuelOrder] that can fit in this allocation.
             */
            val acceptedFuelOrders: List<FuelOrder>,
            /**
             * All [FuelOrder] that failed to be fitted in this allocation.
             */
            val deniedFuelOrders: List<FuelOrder>,
    //        /**
    //         * All [SiteOrder] that have had all of their [FuelOrder] fitted in this allocation.
    //         * @see [acceptedFuelOrders]
    //         */
    //        val completeSiteOrders: List<SiteOrder>,

    //        /**
    //         * All [SiteOrder] that have a [SiteOrder.fuelOrders] present in [deniedFuelOrders].
    //         */
    //        val uncompleteSiteOrders: List<SiteOrder>
    ) {

        val areOrdersLeft
            get() = deniedFuelOrders.isEmpty()

    }

 */
