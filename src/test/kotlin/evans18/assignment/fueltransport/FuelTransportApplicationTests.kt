package evans18.assignment.fueltransport

import evans18.assignment.fueltransport.controller.FuelAllocationController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FuelTransportApplicationTests {

    @Autowired
    private lateinit var controller : FuelAllocationController

    @Test
    fun contextLoads() {
        assertThat(controller).isNotNull
    }

}
