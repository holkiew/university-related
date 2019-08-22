/*
Starting point for exercise of Lab Session 6.
CVS course - Integrated Master in Computer Science and Engineering
@FCT UNL Luis Caires 2015
*/

/*@
predicate AccountP(unit a,BankAccount c; int n) = c != null &*& AccountInv(c,n,?m);
@*/


class Bank {

  BankAccount store[];
  int nelems;
  int capacity;

  public Bank(int size)
  {
    nelems = 0;
    capacity = size;
    store = new BankAccount[size];
  }

  void addnewAccount(int code)
  {
   	BankAccount c = new BankAccount(code);
   	
   	if(nelems < capacity) {
  	  store[nelems] = c;
  	  nelems = nelems + 1;
  	}
  }

  int getbalance(int code)
  {
    int i = 0;

    while (i < nelems)
    {
       if ( store[i].getcode() == code) {
       	return store[i].getbalance();
       }
       i = i + 1;
    }
    return -1;
  }

  int removeAccount(int code)
  {
    int i = 0;
    
    while (i < nelems)
    {
       if ( store[i].getcode() == code) {
       	   if (i<nelems-1) {
       	     store[i] == store[nelems-1];
       	   }
       	   nelems = nelems - 1;
       	   store[nelems] = null;
       	 return 0;
       }
       i = i + 1;
    }
    return -1;
  }
}
