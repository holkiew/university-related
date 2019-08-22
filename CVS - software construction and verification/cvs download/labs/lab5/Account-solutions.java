
/*@ 
  predicate AccountInv(Account a; int b) = 
  	a.balance |-> b 
	&*& b >= 0;
  @*/
  
public class Account {

  int balance;

  public Account()
  //@ requires true;
  //@ ensures AccountInv(this, 0);
  {
	  balance = 0;
  }

  void deposit(int v)
  //@ requires AccountInv(this, ?b) &*& 0 <= v;
  //@ ensures AccountInv(this, b + v);
  {
	  balance += v;
  }

  void withdraw(int v)
  //@ requires AccountInv(this, ?b) &*& 0 <= v &*& v <= b;
  //@ ensures AccountInv(this, b - v);
  {
	  balance -= v;
  }

  int getBalance()
  //@ requires AccountInv(this, ?b);
  //@ ensures AccountInv(this, b) &*& result == b;
  {
	  return balance;
  }

  static void transfer(Account a1, Account a2, int v)
  //@ requires a1 != null &*& AccountInv(a1, ?ba1) &*& a2 != null &*& AccountInv(a2, ?ba2) &*& v >= 0;
  //@ ensures ba1 >= v ? AccountInv(a1, ba1-v) &*& AccountInv(a2, ba2+v) : AccountInv(a1, ba1) &*& AccountInv(a2, ba2);
  {
	  if(a1.getBalance() >= v){
		  a1.withdraw(v);
		  a2.deposit(v);
	  }
  }

  public static void main(String[] args)
  //@ requires true;
  //@ ensures true;
  {
	  Account a1 = new Account();
	  Account a2 = new Account();
	  
	  a1.deposit(500);
	  
	  transfer(a1, a2, 250);
  }
}
