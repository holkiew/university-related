

/*@
  predicate Node(Node n;Node nxt,int v) = 
  	n.next |-> nxt &*& 
  	n.val |-> v;
  	
  predicate List(Node n;) = 
  	n == null ? 
  	emp 
  	: 
  	Node(n,?h,_) &*& List(h);
@*/

public class Node {
  Node next; 
  int val;

  public Node()
    //@ requires true;
    //@ ensures Node(this,null,0);
  {
    next = null;
    val = 0;
  }
  
  public void setnext(Node n)
    //@ requires Node(this,_,?v);
    //@ ensures Node(this,n,v);
  {
    next = n;
  }

	
  public void setval(int v)
    //@ requires Node(this,?n,_);
    //@ ensures Node(this,n, v);
  {
    val = v;
  }

  public Node getnext()
    //@ requires Node(this,?n,?v);
    //@ ensures Node(this,n,v) &*& result == n;
  {
    return next;
  }
  public int getval()
    //@ requires Node(this,?n,?v);
    //@ ensures Node(this,n,v) &*& result == v;
  {
    return val;
  }
}



