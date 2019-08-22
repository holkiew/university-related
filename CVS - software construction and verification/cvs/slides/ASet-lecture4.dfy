class ASet { 
    // Abstract state 
    ghost var s:set<int>;

    function AbsInv():bool
    reads this, a
    { true && RepInv() && Sound() }

    // Concrete state

    var a:array<int>;
    var size:int;

    function RepInv() : bool 
    reads `a, `size, a
    {
        0 <= size <= a.Length &&
        Unique(a,0,size)
    }

    function Unique(b:array<int>, l:int, h:int):bool
    requires 0 <= l <= h <= b.Length
    reads b
    { forall i :: l <= i < h ==> forall j :: i < j < h ==> b[i] != b[j] }

    function Sound() : bool 
    requires RepInv()
    reads this, a
    {
        forall x :: x in s <==> x in a[..size]
    }

    // Methods 
    constructor(maxSize:int)
    ensures AbsInv()

    method add(x:int)
    requires AbsInv() 
    ensures AbsInv()
    ensures s == old(s) + {x}
    modifies `s, a, `size, `a
    {
        if( size == a.Length ) { Grow(); }

        var idx := find(x);
        if( idx < 0 ) {
            a[size] := x;
            size := size + 1;
            s := s + {x};
            assert forall i :: 0 <= i < size-1 ==> a[i] == old(a[i]);
        }
    }

    method Grow() 
    requires AbsInv()
    ensures RepInv()
    ensures Sound()
    ensures size < a.Length
    ensures forall i :: 0 <= i < size ==> a[i] == old(a[i]);
    ensures fresh(a)
    modifies `a
    {
        var b := new int[a.Length*2+1];
        var i := 0;
        while i < size 
            decreases size - i
            invariant 0 <= i <= size
            invariant forall k :: 0 <= k < i ==> b[k] == a[k];
            invariant Sound()
            modifies b
        {
            b[i] := a[i];
            i := i + 1;
        }
        a := b;
    }


    method find(x:int) returns (idx:int)
    requires AbsInv()
    ensures AbsInv()
    ensures (idx == -1 && !(x in s)) || (0 <= idx < size && a[idx] == x)
    {
        var i:int := 0;
        while (i<size)
            decreases size-i
            invariant 0 <= i <= size;
            invariant forall j::(0<=j<i) ==> x != a[j];
        {
            if (a[i]==x) { return i; }
            i := i + 1;
        }
        return -1;
    }

    method contains(x:int) returns (b:bool)
    requires AbsInv()
    ensures AbsInv()
    ensures b <==> x in s
    {
        var f:= find(x);
        return f != -1;
    }

    static method main() {
        // var myset := new ASet(10);
        // myset.add(-1);
        // var b := myset.contains(1);
    }
}