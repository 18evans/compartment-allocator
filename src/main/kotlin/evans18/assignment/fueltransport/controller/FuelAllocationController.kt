package evans18.assignment.fueltransport.controller

import evans18.assignment.fueltransport.data.manager.CarrierManager
import evans18.assignment.fueltransport.data.manager.allocation.AllocationType
import evans18.assignment.fueltransport.data.manager.allocation.SiteOrderAllocator
import evans18.assignment.fueltransport.data.model.TruckCarrier
import evans18.assignment.fueltransport.data.model.request.SiteOrder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/allocator")
class FuelAllocationController(
        private val truckCarriersManager: CarrierManager<TruckCarrier>,
        private var calculator: SiteOrderAllocator
) {

    /**
     * Distributes into a single truck the collection of orders from different clients, identified by each object's [SiteOrder.name],
     *
     * Note: truck doesn't know anything about its orders.
     *
     * @return the best allocation possible for a single Truck
     */
    @PostMapping("/truck")
    fun allocateSiteFuelOrdersToSingleTruck(@RequestBody siteOrders: List<SiteOrder>): TruckCarrier {
        val truckCarrier = truckCarriersManager.getNextAvailable()

        //calculate if a single truck can fit all of the sites' fuel orders at once
        val calculateAllocation = calculator.calculateAllocation(truckCarrier, siteOrders, AllocationType.CARRY_ALL_TOGETHER)
        return calculateAllocation
    }

}