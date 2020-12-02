package evans18.assignment.fueltransport.model

import evans18.assignment.fueltransport.data.model.fuel.FuelOrder
import evans18.assignment.fueltransport.data.model.fuel.FuelType
import evans18.assignment.fueltransport.data.model.fuel.VolumeType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FuelOrderTest {

    @Test
    fun setterVolumeChangesVolumeValueAndType() {
        //given
        val originalVolume = 3.0
        val originalType = VolumeType.CubicMetres
        val dummy = FuelOrder("someDepot", FuelType.Diesel, originalVolume, originalType)

        //when
        val newVolume = 500.0
        val newType = VolumeType.Liters
        dummy.setVolume(newVolume, newType)

        //then
        assertEquals(newVolume, dummy.volume)
        assertEquals(newType, dummy.volumeType)
    }

    @Test
    fun convertFromCubicMetresToLiters() {
        //given
        val originalVolume = 3.0
        val originalType = VolumeType.CubicMetres
        val order = FuelOrder("someDepot", FuelType.Diesel, originalVolume, originalType)

        //when
        evans18.assignment.fueltransport.data.model.fuel.convertFromCubicMetresToLiters(order)

        //then
        assertNotEquals(originalType, order.volumeType)
        assertEquals(VolumeType.Liters, order.volumeType)
    }

}