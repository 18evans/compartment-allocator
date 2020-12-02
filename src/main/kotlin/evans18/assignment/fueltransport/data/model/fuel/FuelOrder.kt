package evans18.assignment.fueltransport.data.model.fuel

/**
 * A wrapper for details of a single fuel order identified by [fuelType] and [depot].
 *
 */
data class FuelOrder(
        /**
         * From which depot is this fuel requested from.
         */
        val depot: String,
        /**
         * What type of fuel is ordered.
         */
        val fuelType: FuelType,
        /**
         * Volume of the ordered fuel.
         */
        private var _volume: Double,
        /**
         * The type of the metric in the [_volume] param.
         */
        private var _volumeType: VolumeType) {

    //note: wanted is public getter for these two
    //workaround Kotlin not supporting property accessor configuration in primary constructor declarations
    val volume
        get() = _volume

    val volumeType
        get() = _volumeType

    /**
     * Setter for changing both variables at once as they are always related.
     */
    fun setVolume(volume: Double, type: VolumeType) {
        this._volume = volume;
        this._volumeType = type
    }

}

fun convertFromCubicMetresToLiters(fuelOrder: FuelOrder) {
    when(fuelOrder.volumeType){
        VolumeType.Liters -> return //already converted
        VolumeType.CubicMetres ->
            fuelOrder.setVolume(fuelOrder.volume * 1000, VolumeType.Liters)
        else -> throw IllegalArgumentException("Given FuelOrder type (${fuelOrder.volumeType.name}) is unsupported by function.")
    }
}



