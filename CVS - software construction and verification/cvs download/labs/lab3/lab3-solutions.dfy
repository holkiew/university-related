/**
    ©João Costa Seco, Eduardo Geraldo, CVS, FCT NOVA, MIEI 2018
    This is the third lab assignment for the Construction and Verification of
    Software of the integrated master in computer science and engineering
    (MIEI) http://www.di.fct.unl.pt/miei

    The piazza page where you can discuss solutions and problems related to
    this lab class is at: piazza.com/fct.unl.pt/spring2019/11159/home

    Your mission is to complete all methods below using dafny. Completely 
    specify the methods by writing the weakest pre-condition and the strongest
    post-condition possible. Implement and verify the methods according to that
    same specification.
 */

/**
    Specify and implement method indexOf below. The functionality of this method
    it to return the index of the first occurrence of elem in array a.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method indexOf(a:array<int>, n:int, elem:int) returns (idx:int)
requires n >= 0 && n <= a.Length
ensures idx >= 0 ==> idx < n
ensures idx >= 0 ==> a[idx] == elem
ensures idx < 0 ==> forall i :: (0 <= i < n) ==> a[i] != elem
{
    var i := 0;

    while (i < n)
    decreases n - i
    invariant i >= 0 && i <= n
    invariant forall idx :: (0 <= idx < i) ==> a[idx] != elem
    {
        if (a[i] == elem) {
            return i;
        }
        i := i + 1;
    } 

    return -1;
}

/**
    Specify and implement method max. This method retuns a pair where
    the first element is the greatest value in the array and the second
    element of the pair is its index. The first argument is the array
    to search and the second one the number of elements in the array.


    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method max(a:array<int>, n:int) returns (elem:int, idx:int)
requires n > 0 && n <=a.Length 
ensures idx >= 0 && idx < n
ensures forall i :: (0 <= i < n) ==> elem >= a[i]
{
    if (n == 0) {
        return 0, -1;
    }

    assert n > 0;

    var max:int := a[0];
    var maxIdx := 0;
    var i:int := 1;

    while (i < n)
    decreases n - i
    invariant i > 0 && i <= n
    invariant maxIdx >= 0 && maxIdx < n
    invariant a[maxIdx] == max
    invariant forall n :: (0 <= n < i) ==> a[n] <= max
    {
        if (a[i] > max) {
            max := a[i];
            maxIdx := i;
        }
        i := i + 1;
    }

    return max, maxIdx;
}

/**
    Specify and implement method min. This method retuns a pair where
    the first element is the lowest value in the array and the second
    element of the pair is its index. The first argument is the array
    to search and the second one the number of elements in the array.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method min(a:array<int>, n:int) returns (elem:int, idx:int)
requires n > 0 && n <=a.Length 
ensures idx >= 0 && idx < n
ensures forall i :: (0 <= i < n) ==> elem <= a[i]
{
    if (n == 0) {
        return 0, -1;
    }

    assert n > 0;

    var min:int := a[0];
    var minIdx := 0;
    var i:int := 1;

    while (i < n)
    decreases n - i
    invariant i > 0 && i <= n
    invariant minIdx >= 0 && minIdx < n
    invariant a[minIdx] == min
    invariant forall n :: (0 <= n < i) ==> min <= a[n]
    {
        if (a[i] < min) {
            min := a[i];
            minIdx := i;
        }
        i := i + 1;
    }

    return min, minIdx;
}

/**
    Specify and implement method fillK. This method retuns true if and only
    if the firs count elements of array a are equal to k.
    The first argument is the array, the second one is the number of 
    elements in the array, the third argument is the value to use as
    comparison, and the last argument is the number of times that k must
    be appear in the array.
    
    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method fillK(a:array<int>, n:int, k:int, count:int) returns (b:bool)
requires n > 0 && n <= a.Length 
requires count >= 0 && count <= n;
ensures b ==> forall i :: (0 <= i < count) ==> a[i] == k;
{
    var i:int := 0;

    while (i < count)
    decreases n - i
    invariant i >= 0 && i <= count
    invariant forall n :: (0 <= n < i) ==> a[n] == k
    {
        if (a[i] != k) {
            return false;
        }
        i := i + 1;
    }

    return true;
}

/**
    Specify and implement method containsSubString. This method tests wheteher or
    not the cahr array a contains the char array b. 
    If a contains b, then the method returns the offset of b on a.
    If a does not contain n then the method returns an illegal index.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.

    Hint: you may want to define an auxiliary function and method.
*/

/**
    Auxilliary function, checks if all the elements of array b are in
    array a starting at position offset.
*/
function substring(a:array<char>, offset:int, b:array<char>, n:int):bool
    reads a,b
    requires 0 <= n <= b.Length
    requires 0 <= offset <= a.Length - n
{
    forall j :: 0 <= j < n ==> b[j] == a[offset+j]
}

/**
    Auxilliary method, checks if all the elements of array b are in
    array a starting at position offset.
*/
method isSubString(a:array<char>, offset:int, b:array<char>) returns (r:bool)
    requires 0 <= offset <= a.Length - b.Length
    ensures r <==> substring(a,offset, b, b.Length)
{
    var i := 0;
    r := true;
    while i < b.Length
        decreases b.Length - i
        invariant 0 <= i <= b.Length
        invariant r <==> substring(a,offset, b, i)
    {
        if a[offset+i] != b[i]
        {
            r := false; 
            break;
        }
        i := i + 1;
    }
}

method containsSubString(a:array<char>, b:array<char>) returns (pos:int)
    requires b.Length <= a.Length 
    ensures 0 <= pos ==> pos < a.Length-b.Length && substring(a,pos,b, b.Length)
    ensures pos == -1 ==> forall j :: 0 <= j < a.Length-b.Length ==> !substring(a,j,b,b.Length)
{
    var i := 0;
    var n := a.Length - b.Length;
    while i < n
        decreases n - i
        invariant 0 <= i <= n
        invariant forall j :: 0 <= j < i ==> !substring(a,j,b,b.Length)
    {
        var r := isSubString(a,i,b);
        if r 
        { return i; }
        i := i + 1;
    }
    return -1;
}
    
/**
    Specify and implement method resize. This method returns a new array
    which Length is the double of the length of the array supplied as 
    argument.

    If the length of the array supplied as argument is zero, then set the
    length of b to a constant of your choice.

    All the elements of a should be inserted, in the same order, in b.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method resize(a:array<int>) returns (b:array<int>)
requires true
ensures a.Length == 0 ==> b.Length == 2
ensures a.Length > 0 ==> b.Length == 2 * a.Length
ensures forall e :: (0 <= e < a.Length) ==> a[e] == b[e]
{

    var result:array<int>;
    if (a.Length == 0) {
        result := new int[2];
    } else {
        result := new int[a.Length * 2];
    }

    var i:int := 0;

    while (i < a.Length)
    decreases a.Length - i
    invariant i >= 0 && i <= a.Length
    invariant forall e :: (0 <= e < i) ==> a[e] == result[e]
    {
        result[i] := a[i];
        i := i + 1;
    }

    return result;
}

/**
    Specify and implement method reverse. This method returns a new array b
    in which the elements of a appear in the inverse order.

    For instance the inverse of array a, a == [0, 1, 5, *, *], where '*'
    denotes an uninitialized array position, results i:
    b == [5, 1, 0, *, *].

    The first parameter is the array to reverse and the second one
    is the number of values in a.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method reverse(a:array<int>, n:int) returns (r:array<int>)
requires n >= 0 && n <= a.Length
ensures a.Length == r.Length
ensures forall i :: (0 <= i < n) ==> a[i] == r[n - 1 - i]
{
    var b:array<int> := new int[a.Length];

    var i:int := 0;

    while (i < n)
    decreases n - i
    invariant i >= 0 && i <= n
    invariant forall idx :: (0 <= idx < i) ==> a[idx] == b[n - 1 - idx]
    {
        b[n - 1 - i] := a[i];
        i := i + 1;
    }

    return b;
}

/**
    Specify and implement method push.
    This method accepts three arguments, an array, the number of elements in the
    array, and the new element.

    It will insert the new element at the end of the array and return a pair
    with the modified array and the new number of elements in the array.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.

    Note: You will need to use a modifies clause:
        <modifies a>.
    This clause lets Dafny know that you intend to change the contents of
    array a.
*/
method push(a:array<int>, na:int, elem:int) returns (b:array<int>, nb:int)
requires na >= 0 && na < a.Length
modifies a
ensures nb == na + 1
ensures a == b
ensures forall i :: (0 <= i < na) ==> a[i] == b[i];
ensures b[na] == elem
{
    a[na] := elem;

    return a, na + 1;
}

/**
    Specify and implement method pop. Given an array and the number of
    elements in it, this method removes the last element of the array and 
    return the modifies array, the number of elements of the modified array
    and the element removed from the array.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
*/
method pop(a:array<int>, n:int) returns (na:array<int>, nn:int, elem:int)
requires n > 0 && n < a.Length
modifies a
ensures nn == n - 1
ensures a == na
ensures forall i :: (0 <= i < nn) ==> a[i] == na[i]
ensures elem == a[n - 1]
{
    return a, n - 1, a[n - 1];
}
