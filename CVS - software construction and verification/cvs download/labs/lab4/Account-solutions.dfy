/**
    ©João Costa Seco, Eduardo Geraldo, CVS, FCT NOVA, MIEI 2018
    This is the forth lab assignment for the Construction and Verification of
    Software of the integrated master in computer science and engineering
    (MIEI) http://www.di.fct.unl.pt/miei

    The piazza page where you can discuss solutions and problems related to
    this lab class is at: https://piazza.com/class/jt9v3bajo03e3#

    Your mission is to specify and implement the Account ADT.
	  For the methods' specifications write the weakest pre-condition and the
	  strongest post-condition possible. Implement and verify the methods 
  	according to that same specification.
	
	  The class Account represents a bank account.  On this account it is
    to deposit and withdraw money.
 */
class Account {
  // Concrete State
  var balance:int;

  /**
    State invariante
   */
  function RepInv():bool
    reads `balance
  { 0 <= balance }

  /**
    Returns the balance.
    Implemented as function methos so it can be called both in methods and specs
   */
  function method Balance(): int
    reads `balance
  { balance }

   /**
    Specify and implement the constructor of the class

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
  */
  constructor() 
    requires true;
    ensures RepInv()
  {
    balance := 0;
  }

  /**
    Specify and implement method withdraw below. The functionality of this method
    emulates the a deposit operation on bank account. As such, this
    operation increases the balance of the account accodring
    to the amount withdrawed.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
  */
  method deposit(amount:int) 
    requires RepInv() && amount >= 0
    ensures  RepInv()
    modifies `balance
  {
    balance := balance + amount;
  }

  /**
    Specify and implement method withdraw below. The functionality of this method
    emulates a withdrawl operation on bank account. As such, this
    operation reduces the balance of the account accodring
    to the amount withdrawed. Take note that the account balance
    should always be non-negative.

    In the specification define the weakest preconditions and 
    the strongest postconditions you can think of. Implement the method
    so that it satisfies the post-conditions assuming the pre-conditions.
  */
  method withdraw(amount:int) 
    requires RepInv() && amount <= balance
    ensures  RepInv()
    modifies `balance
  {
    balance := balance - amount;
  }

  /**
    Main method. Creates an account and execites deposit and
    withdrawl operation on it. This method does not require
    annotations, you just have tom implement it.
   */
  method main() {
    var a:Account := new Account();
    a.deposit(10);
    if(a.Balance() >= 10)
    { a.withdraw(10); }
  }
}