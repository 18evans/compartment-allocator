package evans18.assignment.fueltransport.data.model.request

import evans18.assignment.fueltransport.data.model.fuel.FuelOrder

/**
 * Cohesive order of potential several types of fuel orders for a target site.
 */
data class SiteOrder(
        /**
         * Name of the site that is ordering.
         */
        //todo future note: can decide if to treat this like user identifier
        val name: String,
        /**
         * The collection of fuel orders that make up this Site's specific cohesive order.
         */
        val fuelOrders: List<FuelOrder>
) {

}
