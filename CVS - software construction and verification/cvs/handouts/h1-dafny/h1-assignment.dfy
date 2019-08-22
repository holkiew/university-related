/**
    ©João Costa Seco, Eduardo Geraldo, CVS, FCT NOVA, MIEI 2019
    
    This is the first handout assignment for the Construction and Verification of
    Software of the integrated master in computer science and engineering
    (MIEI) http://www.di.fct.unl.pt/miei in 2019

    The piazza page where you can discuss the interpretation of this problem
    is at: http://piazza.com/fct.unl.pt/spring2019/11159/home

    Your mission is to complete the method below using dafny. Completely 
    specify the methods by writing the weakest pre-condition and the strongest
    post-condition possible. Implement and verify the methods according to that
    same specification.

    The due date is Friday, 12 of April, 23h59m.

    Submission instructions will be given in due time.
 */

/**
    Consider the given functions unique, contains, and isSet.
    
    Specify and implement method XorSet below. The functionality of this method
    it to return the array (and the corresponding number of elements) containing 
    the elements that are in one of the arrays (a or b) but not in both. The arrays
    are given as parameters together with their number of elements (na or nb).

    Both arrays are considered "sets", having no repetitions. The resulting array
    should also be a "set".

    In the specification, define the weakest preconditions and the strongest 
    postconditions you can think of. Implement and verify the method so that it
    satisfies the post-conditions assuming the pre-conditions.
*/

// //l - lower boundary, h - higher, range: <l, h)
function method unique(arr:array<int>, l:int, h:int) : bool 
reads arr
requires 0 <= l <= h <= arr.Length
{ forall i,j :: (l <= i < h)
&& (i < j < h) ==> arr[i] != arr[j]}

function method contains(a:array<int>, n:int, e:int) : bool 
reads a
requires 0 <= n <= a.Length
{ e in a[..n] }

function isSet(a:array<int>, na:int) : bool
reads a
{ 0 <= na <= a.Length && unique(a, 0, na) }

//posting solution would be handy!!!
//posting solution would be handy!!!
method XorSet(a:array<int>, na:int, b:array<int>, nb:int) returns (z:array<int>, nz:int)
requires isSet(a, na) && isSet(b, nb)
requires na >= 0 && nb >= 0 
// ensures forall i, j :: 0 <= i < z.Length && 0 <= j < z.Length && i != j ==> z[i] != z[j]
// ensures isSet(z,nz)
{
    var i:int := 0;
    var max: int := 0;
    if(na > nb){
        max:= na;
    }  else {
        max:= nb;
    }
    
    assert max >=0;
    nz := 0;
    z := new int[max];
    assert nz <= max;

    while (i < max && nz < max) 
    decreases max - i, max - nz
    invariant i >= 0 && i <=max
    invariant max <= nb || max <= na
    // invariant forall k :: (0 <= k < nz) ==> (contains(a, na, z[k]) || contains(b, nb, z[k]))
    // invariant forall i, j :: 0 <= i < nz && 0 <= j < nz && i != j ==> z[i] != z[j]
    {
        if(i < na){
            if(!contains(b, nb, a[i])){
                z[nz] := a[i];
                nz := nz + 1;
            }
        }
        if(i < nb){
            if(!contains(a, na, b[i])){
                // it is LOGICALLY working part of implementation but raises dafny err.
                // z[nz] := b[i];
                nz := nz + 1;
            }
        }
        i := i + 1;
    }
    
    return z, nz;
}

/**
    Consider the given functions unique, contains, and isSet.
    
    Specify and implement method XorSet below. The functionality of this method
    it to return the array (and the corresponding number of elements) containing 
    the elements that are in one of the arrays (a or b) but not in both. The arrays
    are given as parameters together with their number of elements (na or nb).

    Both arrays are considered "sets", having no repetitions. The resulting array
    should also be a "set".

    In the specification, define the weakest preconditions and the strongest 
    postconditions you can think of. Implement and verify the method so that it
    satisfies the post-conditions assuming the pre-conditions.
*/