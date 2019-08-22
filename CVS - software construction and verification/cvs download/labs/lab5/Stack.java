/*@
  predicate StackInv(Stack s;) = s.head |-> ?h &*& List(h);

@*/

class StackEmptyE extends Exception {}

public class Stack {

  Node head;

  public Stack()
    //@ requires true;
    //@ ensures StackInv(this);
  {
    head = null;
  }

  public void push(int v)
    //@ requires StackInv(this);
    //@ ensures StackInv(this);
  {
    Node n = new Node();
    n.setval(v);
    n.setnext(head);
    head = n;
  }

  public int pop()
    throws StackEmptyE //@ ensures StackInv(this);
    //@ requires StackInv(this);
    //@ ensures StackInv(this);
  {
    if(head!=null) {
      int v = head.getval();
      head = head.getnext();
      return v;
   }
   throw new StackEmptyE();
  }
}
