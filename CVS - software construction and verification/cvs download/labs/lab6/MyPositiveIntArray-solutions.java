/*
Starting point for exercise of Lab Session 6.
CVS course - Integrated Master in Computer Science and Engineering
@FCT UNL Eduardo Geraldo, João Costa Seco 2019
*/

/*@
 
  predicate DiffFrom(int elem, int x; unit b) = elem != x &*& b == unit;

  predicate ArrayInv(IntArrayP arr; int[] a, int n, int c) =
         arr.array |-> a 
     &*& arr.nelems |-> n 
     &*& arr.capacity |-> c 
     &*& a != null &*& a.length == c 
     &*& a.length > 0 &*& 0 <= n &*& n <= a.length  
     &*& array_slice(a, 0, n, ?elems) 
     &*& array_slice(a, n, c, ?rest);

  predicate DiffUpTo(IntArrayP arr, int idx, int elem; int[] a, int n, int c) =
         arr.array |-> a 
     &*& arr.nelems |-> n 
     &*& arr.capacity |-> c 
     &*& a != null &*& a.length == c 
     &*& a.length > 0 &*& 0 <= n &*& n <= a.length  
     &*& 0 <= idx &*& idx <= n 
     &*& array_slice_deep(a, 0, idx, DiffFrom, elem, _, _) 
     &*& array_slice(a, idx, n, _) 
     &*& array_slice(a, n, c, _);

  predicate EqualAt(IntArrayP arr, int idx, int elem; int[] a, int n, int c) =
         arr.array |-> a 
     &*& arr.nelems |-> n 
     &*& arr.capacity |-> c 
     &*& a != null &*& a.length == c 
     &*& a.length > 0 &*& 0 <= n &*& n <= a.length  
     &*& 0 <= idx &*& idx <= n 
     &*& array_slice_deep(a, 0, idx, DiffFrom, elem, _, _) 
     &*& array_slice(a, idx, idx+1, ?vals) 
     &*& head(vals) == elem
     &*& array_slice(a, idx+1, n, _) 
     &*& array_slice(a, n, c, _);

@*/


class IntArrayP {

  int array[];
  int nelems;
  int capacity;

  public IntArrayP(int size)
  
  //@  requires size > 0;
  //@  ensures ArrayInv(this, _, 0, size);
  

  {
    nelems = 0;
    capacity = size;
    array = new int[size];

  }


  /*    result >= 0
      ?
        (array_slice(arr, result, result + 1, ?val) &*& all_eq(val, elem) == true)
      :
        (array_slice(arr, 0, n, ?vals) &*& all_diff(vals, elem) == true);
  @*/

    
  public int indexOf(int elem)
  //@ requires ArrayInv(this, ?arr, ?n, ?c);
  //@ ensures result == -1 ? DiffUpTo(this, n, elem, arr, n, c) : EqualAt(this, result, elem, _, _, _);
  {
    int idx = 0;

    //@ close DiffUpTo(this, idx, elem, arr, n, c);
    while(idx < nelems)
    //@ invariant DiffUpTo(this, idx, elem, arr, n, c);
    {
      //@ array_slice_split(arr,idx,idx+1);
      if(array[idx] == elem) {
        //@ close EqualAt(this, idx, elem, _, _, _);
        return idx;
      }
      idx ++;
    }
    //@ assert DiffUpTo(this, n, elem, arr, n, c);
    return -1;
  }
}