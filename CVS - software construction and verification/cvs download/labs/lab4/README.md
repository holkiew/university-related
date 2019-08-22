
Using Dafny, it is possible to define new Abstract Data Types (ADT).
To do so, it is necessary to define a class (the ADT) and its Methods.
Then, for the class itdelf define an invariant, the class invariant,
which the ADT must be respect at all times.
For instance, an ADT Counter, which represents a counter, would have
an invariant stating that its value must be greater or equal to zero.
Beside the invariant, it is necessary to define the specifications
of the ADT's methods. In these specifications, besides defining what
the method does, it is also necessary to define the state in which the methods' execution leaves the ADT.

Lab 4:

* Implement the ADT in file __Account.dfy__.
Start by defining the msot strict class invariant you can think of.
Then, proceed to specify the class methods using the weakest preconditions and the strongest postconditions possible

After you have specified the methods, implement them accordingly.

# Equipa docente

João Costa Seco
Eduardo Geraldo