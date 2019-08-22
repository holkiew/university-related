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

@*/


class IntArrayP {

  int array[];
  int nelems;
  int capacity;

  public IntArrayP(int size)

  {
    nelems = 0;
    capacity = size;
    array = new int[size];

  }
    
  public int indexOf(int elem)
  {
    int idx = 0;

    while(idx < nelems)
    {
      if(array[idx] == elem) {
        return idx;
      }
      idx ++;
    }
    return -1;
  }
}