# Fuel Allocator for Trucks
Spring Boot application for allocating objects into carriers.
Currently it's somewhat specific to the case of allocating Fuel orders to Truck but is developed with some intention for extensibility.

## Algorithms
Currently supported:
* Algorithm for best fit of ALL input Site's Fuel Orders into a single truck.
Current used test case is only for the optimistic case where the input will indeed fully fit. 
Designed but not tested to cover following cases:
    * no available compartment space for any of the leftover orders.
    
### Postman:
You can also directly call the endpoint via provided input request available here:
https://documenter.getpostman.com/view/7436485/TVmMfcf2
(Feel free to experiment with input)

#### Requirements
Since spring boot uses port 8080 either ensure that port is free or define a custom port for its use.