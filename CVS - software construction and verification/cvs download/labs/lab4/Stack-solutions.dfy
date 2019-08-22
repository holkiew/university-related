class Stack {
  // Concrete State
  var a: array<int>;
  var size:int;

  function isEmpty():bool 
    reads `size
  { size == 0 }

  function RepInv():bool
    reads `size, `a
  {
    0 < a.Length && 0 <= size <= a.Length
  }
  
  constructor() 
    ensures RepInv()
  {
    a := new int[1];
    size := 0;
  }

  method push(x:int) 
    requires RepInv()
    ensures !isEmpty() && RepInv()
    modifies `a, `size, a
  {
    if( size >= a.Length ) { grow(); }
    a[size] := x;
    size := size + 1;
  }

  method pop() returns (x:int)
    requires RepInv() && !isEmpty()
    requires RepInv()
    modifies `size
  {
    size := size - 1;
    return a[size];
  }

  method grow() 
    requires RepInv()
    ensures RepInv()
    ensures size < a.Length
    ensures forall k:int :: 0 <= k < size ==> a[k] == old(a[k])
    ensures fresh(a)
    modifies `a
  {
    var b := new int[a.Length*2];
    var i := 0;
    while i < size 
      decreases size - i 
      invariant 0 <= i <= size
      invariant forall k :: 0 <= k < i ==> b[k] == a[k]
      modifies b
    {
      b[i] := a[i];
      i := i + 1;
    }
    a := b;
  }
}