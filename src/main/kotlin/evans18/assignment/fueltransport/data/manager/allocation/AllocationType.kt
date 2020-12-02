package evans18.assignment.fueltransport.data.manager.allocation
/**
 * Distinct processing types for an allocation calculation.
 *
 * @see [AllocationCalculator]
 */
enum class AllocationType {
    /**
     * Calculator will try to fit as many objects as possible to carry them together into given carrier set.
     */
    //todo future note: as opposed to this examples that you can have separate AllocatingTypes are:     least/many trips as possible,   one site per time (and many others honestly)
    CARRY_ALL_TOGETHER

}