/*
    Class Drone 

    Construction and Verification of Software, FCT-UNL, © (uso reservado)

    This class emulates a drone. The drone has to obey some restrictions. 
    There are restrictions on its speed and position, and on the conditions 
    its operations can execute.

    The drone supports 4 operations: take off, move, addWaypoint, and land.
    Take note that for every operation, you'll need to implement a method
    for starting it and another for finishing it. For instance, let us
    consider the operation move which moves the drone from point A to
    point B. This operation is divided into <Move> and <CompleteMove>.
    In <Move>, it is ncessary to set the target destination and the drone's
    speed. In the second part of the operations, <CompleteMove>, it is
    necessary to set the drone's position as being the target position
    (it arrived to the target destination) and set the speed of the drone
    to zero. It is hovering at the target position.

    The drone can freely move inside a semi-sphere with
    radius <r> with center <n> meters above its control station and and in a
    cilinder with a height of <n> meters and whose base is centered in the
    drone's control station. Regarding the speed of the drone, the drone's 
    maximum speed is 10 Km/h.

    <r> -> radius
    <n> -> operational height

    OPERATIONS:
    
        Take Off
        
        Makes the drone climb to its operational height at maximum speed and hover
        at that position.
        With the exception for its height, its position must not change.
        This operation can only execute successfully if the drone is on the ground
        (landed).


        Move

        Given a valid position, this operation causes the drone to move to the
        target position. When moving, the drone moves at its maximum speed.
        In a similar fashion to the previous operation, this operation only
        executes if the drone is idle.


        AddWaypoint

        Given a position IN THE SEMI-SPHERE with center at <n> meters above its
        control station, the drone will add that position to a sequence of
        positions it will follow one by one. When following waypoints, the
        drone is said to be patrolling and it moves at half its maximum
        speed. It is only possible to add new waypoints to the drone if
        it is hovering (idle) or it is already on paatrol.


        Land

        Lands the drone directly below its current position. When landing,
        the drone does so at its maximum speed. This operation can only execute
        when the drone is idle.

    When instantiating a new drone, it is necessary to supply its
    operational height (<n>) and its operation range (<r>).

    There is a mapping between the representation state and abstract state
    that guarantees the soundness of the ADT. Also, don't forget the
    state invariant.
	
	The delivery date is the 29th of April.
 */

class Drone {

}