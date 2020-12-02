package evans18.assignment.fueltransport.data.manager.allocation

import evans18.assignment.fueltransport.data.manager.TruckManager
import evans18.assignment.fueltransport.data.model.TruckCarrier
import evans18.assignment.fueltransport.data.model.carrier.compartment.TruckCompartment
import evans18.assignment.fueltransport.data.model.fuel.FuelOrder
import evans18.assignment.fueltransport.data.model.fuel.FuelType
import evans18.assignment.fueltransport.data.model.fuel.VolumeType
import evans18.assignment.fueltransport.data.model.fuel.convertFromCubicMetresToLiters
import evans18.assignment.fueltransport.data.model.request.SiteOrder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream

@SpringBootTest
internal class AllocationCalculatorTest {

    //region Fields

    @Autowired
    private lateinit var calculator: SiteOrderAllocator; //SUT

    //endregion

    //region Helpers
    companion object {
        @JvmStatic
        fun siteOrders_allFitTogether_Always_InSingleTruck(): Stream<Arguments> {
            val depoExxon = "Exxon" //names don't really matter as long as unique
            val depoShell = "Shell"

            return Stream.of(
                    //base example
                    Arguments.of(
                            //given allocations (note: this is the base example in the assignment)
                            listOf(
                                    SiteOrder("SiteA",
                                            listOf(
                                                    FuelOrder(depoExxon, FuelType.Diesel, 4000.0, VolumeType.Liters),
                                                    FuelOrder(depoExxon, FuelType.Gasoline, 3000.0, VolumeType.Liters),
                                            )),
                                    SiteOrder("SiteB",
                                            listOf(
                                                    FuelOrder(depoShell, FuelType.Gasoline, 4000.0, VolumeType.Liters),
                                                    FuelOrder(depoExxon, FuelType.Diesel, 2000.0, VolumeType.Liters)
                                            )
                                    ),
                            ),
                            //given truck Compartments -  expected for all site orders to fit in this one together
                            TruckManager.BASE_TEST_CASE_COMPARTMENTS.sortedBy {
                                it.maxCapacity
                            },

                            //expected the order's BEST possible fit for the given compartment allocations (for this specific truck) - sorted by demand
                            listOf(
                                    //when equal demand order doesn't matter
                                    FuelOrder(depoExxon, FuelType.Diesel, 4000.0, VolumeType.Liters),
                                    FuelOrder(depoShell, FuelType.Gasoline, 4000.0, VolumeType.Liters),
                                    FuelOrder(depoExxon, FuelType.Gasoline, 3000.0, VolumeType.Liters),
                                    FuelOrder(depoExxon, FuelType.Diesel, 2000.0, VolumeType.Liters)
                            ).sortedBy { it.volume }
                    )
            )
        }
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("siteOrders_allFitTogether_Always_InSingleTruck")
    fun allocation_DoesAlwaysFit_AllSiteOrdersBy_CarryAllTogether_InSingleTruck(siteOrders: List<SiteOrder>, compartments: List<TruckCompartment>, expectedCompartmentDistributedOrders: List<FuelOrder>) {
        //when
        val truckCarrier = TruckCarrier(compartments)

        //- send allocation request
        val truckAllocation = calculator.calculateAllocation(truckCarrier, siteOrders, AllocationType.CARRY_ALL_TOGETHER)

        fun hasCompartmentEntirelyFitTheGivenOrder(compartment: TruckCompartment, fittedFuelOrder: FuelOrder): Boolean {
            with(compartment) {
                return fuelType?.equals(fittedFuelOrder.fuelType) ?: false &&
                        fuelDepot?.equals(fittedFuelOrder.depot) ?: false &&
                        currentVolume.equals(convertFromCubicMetresToLiters(fittedFuelOrder))
            }
        }

        //then - assert that each of the truck's compartments fit each of the entire orders
        truckAllocation.compartments.mapIndexed { i, compartment ->
            hasCompartmentEntirelyFitTheGivenOrder(compartment, expectedCompartmentDistributedOrders[i])
        }.let { doesEachCompartmentFullyFit ->
            assertTrue {
                doesEachCompartmentFullyFit.all { true }
            }
        }
    }

    //endregion
}