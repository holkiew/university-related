/*@ predicate QueueInv(Queue q; int n, int m) = 
          q.front |-> ?f
      &*& q.rear |-> ?r
      &*& q.numberOfElements |-> n
      &*& q.maxElements |-> m
      &*& q.elements |-> ?a
      &*& a.length == m
      &*& 0 <= n &*& n <= m
      &*& 0 <= f &*& f < m
      &*& 0 <= r &*& r < m
      &*& array_slice(a,0,m,?items);
@*/

class Queue {
	
  int front;
  int rear;
  int numberOfElements;
  int maxElements;
  int[] elements;

  Queue(int max);

  void enqueue(int v);

  int dequeue();
  
  boolean isFull();
  
  boolean isEmpty();

}