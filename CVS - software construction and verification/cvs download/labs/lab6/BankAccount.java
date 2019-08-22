/*
Starting point for exercise of Lab Session 6.
CVS course - Integrated Master in Computer Science and Engineering
@FCT UNL 2018
*/


/*@
predicate AccountInv(BankAccount o; int code, int b) =
        o != null
    &*& o.balance |-> b 
    &*& o.accountid |-> code
    &*& o.climit |-> ?limit
    &*& 0 <= b + limit
    &*& 0 <= code &*& code <= 1000;
@*/

public class BankAccount {

  int balance;
  int climit;
  int accountid;

  public BankAccount(int id)
  //@ requires 0 <= id &*& id <= 1000;
  //@ ensures AccountInv(this, id, 0);
  {
      balance = 0;
      climit = 0;
      accountid = id;
      //@ close AccountInv(this, id, 0);
  }

  public void deposit(int v)
  //@ requires AccountInv(this,_,?b) &*& v >= 0;
  //@ ensures AccountInv(this,_,b+v);
  {
  	balance += v;
  }

  public void withdraw(int v)
  //@ requires AccountInv(this,_,?b) &*& this.climit |-> ?limit &*& v >= b-limit ;
  //@ ensures AccountInv(this,_,b-v);
  {
     balance  -= v;
  }

  public int getcode()
  //@ requires AccountInv(this,?c,_);
  //@ ensures AccountInv(this,_,_) &*& result == c;  
  {
     return accountid;
  }

	
  public int getbalance()
  {
     return balance;
  }

  public void setclimit(int cl)
  //@ requires AccountInv(this,?c,?b) &*& this.climit |-> _ &*& -cl <= b;
  //@ ensures AccountInv(this,c,b) &*& this.climit |-> cl;
  {
      climit = cl;
  }

  public int getclimit()
  //@ requires this.climit |-> ?a &*& this.climit |-> ?b;
  //@ ensures true;
  {
     return climit;
  }

  static void main()
  {
    BankAccount b1 = new BankAccount(1000);
    b1.setclimit(500);
  }
}
