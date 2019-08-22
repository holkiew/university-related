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
requires 0 <= n <= a.Length
ensures -1 <= idx < n 
ensures idx == -1 <==> forall j:: 0 <= j < n ==> a[j] != elem
ensures idx >= 0 ==> a[idx] == elem
{ 
    var i := 0;
            while i < a.Length 
            decreases n - i
            invariant 0 <= i <= n
            invariant forall j:: 0 <= j < i ==> a[j] != elem
            {
                if (elem == a[n]) {
                    return i;
                }
            i := i + 1;
            }
           assert forall j:: 0 <= j < i ==> a[j] != elem
           assert i == n;
           assert forall j:: 0 <= j < n ==> a[j] != elem
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
requires 0 < n < a.Length
ensures 0 <= idx < n
ensures a[idx] == max && forall j:: 0 <= j < n ==> a[j] <= max
{
var i := 1;
while i < n 
    decreases n - i 
    invariant 0 <= i <= na
    invariant a[idx] == max
    invariant forall j:: 0 <= j < i ==> a[j] <= max
    {
        if max < a[i]
        {
            idx:=i;
            max := a[i];
        }
        i := i+1;
    }
    return max;
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

/**
    Specify and implement method fill. This method retuns true if and only
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
requires 0 <= n <= a.Length //base for every array loop
ensures b <==> forall j :: 0 <= j < count ==> a[j] == k
{
    var i:= 0;
    while i< n
        decreases n-i //base for array loop
        invariant 0 <= i <= n //base for array loop
    {
            if(a[i] != k){
                return false;
            }
            i := i +1;
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
/* sprawdza czy w a znajduje sie b od danego offsetu */

method containsSubString(a:array<char>, b:array<char>, offset:int) returns (res:bool)
requires b.length + offset <= a.Length
requires offset >= 0
ensures res <==> forall j :: 0 <= j < b.Length ==> a[j+offset] == b[j]
{
    var i:= 0;
    while i<n 
    decreases n-i
    invariant 0 <= i <= b.Length
    {
        if( a[i+offset] != b[i]){
            return false;
        }
        i := i + 1;
    }
    return true;

}


method containsSubString(a:array<char>, b:array<char>) returns (pos:int)
    
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
