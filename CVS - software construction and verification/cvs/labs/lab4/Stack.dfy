class Stack {
  // Abstract State
  ghost var s:seq<int>;

  function AbsInv(): bool { true }

  // Concrete state

  var a:array<int>;
  var size:int;


  function RepInv(): bool

  function Sound(): bool

  // ADT Code && Spec
  function Empty():bool

  constructor() 
  ensures AbsInv() && Empty()

  method push(x:int)
  requires AbsInv()
  ensures AbsInv() && s == old(s) + [x]

  method pop() returns (x:int)
  requires AbsInv() && !Empty()
  ensures AbsInv() && s + [x] == old(s)

  static method main() {
    var stack:Stack := new Stack();

    stack.push(0);
    var v0 := stack.pop();
    stack.push(1);
    stack.push(2);
    var v1 := stack.pop();
    var v2 := stack.pop();
    assert stack.s == [];
    assert v1 == 2 && v2 == 1;

  }  

}