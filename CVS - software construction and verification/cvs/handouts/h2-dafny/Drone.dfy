/*
    Class Drone 

    Construction and Verification of Software, FCT-UNL, © (uso reservado)

    This class emulates a drone. The drone has to obey some restrictions. 
    There are restrictions on its speed and position, and on the conditions 
    its operations can execute.

    The drone supports 4 operations: take off, move, addWaypoint, and land.
    Take note that for every operation, you'll need to implement a method
    for starting it and another for finishing it.
    
    For instance, let us
    consider the operation move which moves the drone from point A to
    point B. This operation is divided into <Move> and <CompleteMove>.
    In <Move>, it is ncessary to set the target destination and the drone's
    speed.
    
    In the second part of the operations, <CompleteMove>, it is
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

   

    When instantiating a new drone, it is necessary to supply its
    operational height (<n>) and its operation range (<r>).

    There is a mapping between the representation state and abstract state
    that guarantees the soundness of the ADT. Also, don't forget the
    state invariant.
	
	The delivery date is the 29th of April.
 */

class Drone {
    var speed: int;
    var maxSpeed: int;
    // current xyz - where we are
    var x: int;
    var y: int;
    var z: int;
    // target xyz - where we go nexst
    var target_x: int;
    var target_y: int;
    var target_z: int;
    // waypoints lists
    var wp_x: array<int>;
    var wp_y: array<int>;
    var wp_z: array<int>;
    // number of elements in wp
    var n_wp: int;
    var operationalHeight: int;
    var operationalRadius: int;
    // controlls
    var landed: bool;
    var hovering: bool;
    var patrolling: bool;
    
    var maxWp: int;

    constructor(n: int, r:int)
    requires n > 0 && r > 0
    ensures fresh(wp_x) && fresh(wp_z) && fresh(wp_y) && wp_z.Length == maxWp && wp_y.Length == maxWp && wp_x.Length == maxWp
    ensures landed == true && hovering == false && patrolling == false
    ensures operationalRadius > 0 && operationalHeight > 0
    {
        operationalHeight:= n;
        operationalRadius:= r;

        speed := 0;
        maxSpeed:= 10;

        x := 0;
        y := 0;
        z := 0;

        landed:= true;
        hovering:= false;
        patrolling:= false;
       
        maxWp := 100;
        n_wp:= 0;
        wp_x := new int[100];
        wp_y := new int[100];
        wp_z := new int[100];
    }

    function RepInv(): bool
    reads this
    {
       (landed ==> !hovering && !patrolling)
            && ((hovering ==> !landed && !patrolling) || ((patrolling && hovering) ==> !landed))
            && ((patrolling ==> !landed && !hovering) || ((patrolling && hovering) ==> !landed))
    }

    
    //   Take Off  
    
    //     Makes the drone climb to its operational height at maximum speed and hover
    //     at that position.
    //     With the exception for its height, its position must not change.
    //     This operation can only execute successfully if the drone is on the ground
    //     (landed).

    function isInMaxRadious(x:int, x0:int, y:int, y0:int, z:int, z0:int) : bool
    reads this
    requires operationalRadius > 0
    {
        var r := (x-x0)*(x-x0) + (y-y0)*(y-y0) + (z-z0)*(z-z0);
        operationalRadius * operationalRadius > r 
    }

    method isInMaxRadiousMethod(x:int, x0:int, y:int, y0:int, z:int, z0:int) returns (b: bool)
    requires operationalRadius > 0
    ensures isInMaxRadious(x,x0,y,y0,z,z0) == b
    {
        var r := (x-x0)*(x-x0) + (y-y0)*(y-y0) + (z-z0)*(z-z0);
        if (operationalRadius * operationalRadius > r ) {
            return true;
        }else {
            return false;
        }
    }

    method TakeOff()
    modifies this`speed, this`z, this`target_z, this`landed, this`hovering
    requires landed == true && hovering == false && patrolling == false
    ensures  hovering == true && landed == false
    ensures  z == operationalHeight &&  speed == 0  
    ensures RepInv()
    {
        StartTakeOff();
        EndTakeOff();
    }

    method StartTakeOff()
    modifies this`speed, this`target_z, this`landed
    requires landed == true && hovering == false
    ensures landed == false 
    ensures speed == maxSpeed && target_z == operationalHeight
    {
        landed:= false;
        speed := maxSpeed;
        target_z := operationalHeight;
    }

    method EndTakeOff()
    modifies this`speed, this`z, this`hovering
    requires landed == false && hovering == false
    ensures landed == false  && hovering == true
    ensures speed == 0 && z == target_z
    {
        speed := 0;
        z:= target_z;
        hovering:= true;
    }

    //    Move

    //     Given a valid position, this operation causes the drone to move to the
    //     target position. When moving, the drone moves at its maximum speed.
    //     In a similar fashion to the previous operation, this operation only
    //     executes if the drone is idle.

   

    method Move(tx: int, ty: int, tz: int)
    modifies this`speed, this`target_x, this`target_y, this`target_z, this`landed, this`hovering, this`x, this`y, this`z
    requires hovering == true && patrolling == false && landed == false
    requires z <= operationalHeight && operationalRadius > 0 && isInMaxRadious(tx, 0, ty, 0, tz, 0)
    ensures hovering == true
    ensures x == tx && y == ty && z == tz
    ensures RepInv()
    {
        StartMove(tx,ty,tz);
        CompleteMove();
    }

    method StartMove(tx: int, ty: int, tz: int)
    modifies this`speed, this`target_x, this`target_y, this`target_z, this`hovering
    ensures hovering == false
    ensures speed == maxSpeed && target_x == tx && target_y == ty && target_z == tz
    {
        hovering:= false;
        target_x:=tx;
        target_y:=ty;
        target_z:=tz;
        speed:= maxSpeed;
    }

    method CompleteMove()
    modifies this`speed, this`x, this`y, this`z, this`hovering
    ensures hovering == true
    ensures speed == 0 && x == target_x && y == target_y && z == target_z
    {
        speed:= 0;
        x:=target_x;
        y:=target_y;
        z:=target_z;
        hovering:= true;
    }

    //  AddWaypoint

    //      Given a position IN THE SEMI-SPHERE with center at <n> meters above its
    //      control station, the drone will add that position to a sequence of
    //      positions it will follow one by one. When following waypoints, the
    //      drone is said to be patrolling and it moves at half its maximum
    //      speed. It is only possible to add new waypoints to the drone if
    //      it is hovering (idle) or it is already on paatrol.

   

    method AddWaypoint(x_: int, y_: int, z_: int, n: int) returns (b:bool)
    modifies this, wp_x, wp_y, wp_z
    requires z <= operationalHeight && operationalRadius > 0
    requires isInMaxRadious(x_, 0, y_, 0, z_, n)
    requires n_wp >= 0 && n_wp < maxWp - 1
    requires n_wp < wp_x.Length && n_wp < wp_y.Length && n_wp < wp_z.Length
    requires (hovering == true || patrolling == true) && landed == false
    ensures RepInv()
    {
        // isInMaxRadiousMethod()
        // adding new wp
        hovering := false;
        patrolling := true;
        wp_x[n_wp] := x_;
        wp_y[n_wp] := y_;
        wp_z[n_wp] := z_;
        n_wp:= n_wp + 1;
        speed := maxSpeed/2;
        // triggering patrolling area. After going through all points back to hovering
        PatrolArea();
    }

    method PatrolArea()
    modifies this`target_x, this`target_y, this`target_z, this`hovering, this`patrolling, this`speed, this`x, this`y, this`z
    requires patrolling == true && hovering == false
    requires n_wp > 0 && n_wp < maxWp
    requires n_wp <= wp_x.Length && n_wp <= wp_y.Length && n_wp <= wp_z.Length
    ensures wp_x == old(wp_x) && wp_y == old(wp_y) && wp_z == old(wp_z)
    ensures patrolling== false && hovering == true
    {
        
        var i:= 0;
        while (i < n_wp - 1)
        decreases n_wp - i
        invariant patrolling == true
        {
            i:= i + 1;
            StartMoveWp(wp_x[i],wp_y[i],wp_z[i]);
            CompleteMoveWp();
        }
        // after pattrolling whole area, go to hovering mode
        patrolling:= false;
        hovering:= true;
    }

    method StartMoveWp(tx: int, ty: int, tz: int)
    modifies this`target_x, this`target_y, this`target_z
    requires patrolling == true
    ensures target_x == tx && target_y == ty && target_z == tz
    {
        target_x:=tx;
        target_y:=ty;
        target_z:=tz;
    }

    method CompleteMoveWp()
    modifies this`speed, this`x, this`y, this`z
    requires patrolling == true
    ensures speed == 0 && x == target_x && y == target_y && z == target_z
    {
        speed:= 0;
        x:=target_x;
        y:=target_y;
        z:=target_z;
    }
    
    //   Land

    //     Lands the drone directly below its current position. When landing,
    //     the drone does so at its maximum speed. This operation can only execute
    //     when the drone is idle.
    method Land()
    modifies this`speed, this`z, this`target_z, this`landed, this`hovering
    requires hovering == true && patrolling == false
    ensures  hovering == false && landed == true
    ensures  z == 0 && speed == 0
    ensures RepInv()
    {
        StartLanding();
        CompleteLading();
    }

    method StartLanding()
    modifies this`speed, this`target_z, this`hovering
    requires hovering == true
    ensures speed == maxSpeed && target_z == 0
    ensures hovering == false
    {
        hovering := false;
        speed := maxSpeed;
        target_z := 0;
    }

    method CompleteLading()
    modifies this`speed, this`z, this`landed
    requires hovering == false
    ensures landed == true
    ensures speed == 0 && z == target_z
    {
        speed := 0;
        z:= target_z;
        landed:= true;
    }
}


    method Main(){
        var n := 10;
        var r := 10;
        var dron := new Drone(n, r);
        
        // dron.TakeOff();
        // dron.Move(1,1,1);
        // dron.AddWaypoint(1,1,1,1);
        // dron.StartLanding();
    }
