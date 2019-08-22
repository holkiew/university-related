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

function unique(a:array<int>, l:int, h:int) : bool 
reads a
requires 0 <= l <= h <= a.Length
{ forall i,j :: (l <= i < h) && (i < j < h) ==> a[i] != a[j] }

function method contains(a:array<int>, n:int, e:int) : bool 
reads a
requires 0 <= n <= a.Length
{ e in a[..n] }

function isSet(a:array<int>, na:int) : bool
reads a
{ 0 <= na <= a.Length && unique(a, 0, na) }

method XorSet(a:array<int>, na:int, b:array<int>, nb:int) returns (z:array<int>, nz:int)
/** Specify and implement this!! No excuses!! */