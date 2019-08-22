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
	
	The class CreditAccount represents a bank account.  On this account it is
    to deposit and withdraw money with a little peculiarity, it is possible to 
    withdraw more money that the balance of the account as long as the account 
    has enough credit. Whn depositing money, that money will used first to 
    recover the credit and only then, it will count towards the increase of the
    account balance.
 */
class CreditAccount {

    var balance:int;
    var credit:int;
    var CREDIT:int;

    function stateInvariant() : bool
    reads this`balance, this`credit, this`CREDIT
    {
        0 <= credit <= CREDIT && balance >= 0 &&
        (balance > 0 ==> credit == CREDIT)
    }

    /**
        Returns the balance.
        Implemented as function methos so it can be called both in methods and specs
    */
    function method getBalance() : int
    reads `balance
    { balance }

    /**
        Returns the credit of the account.
        Implemented as function methos so it can be called both in methods and specs
     */
    function method getCredit(): int
    reads `credit
    { credit }

    /**
        Returns the maximum credi of the account.
        Implemented as function methos so it can be called both in methods and specs
     */
    function method getMaxCredit(): int
    reads `CREDIT
    { CREDIT }

   /**
        Main method. Creates an account and execites deposit and
        withdrawl operation on it. This method does not require
        annotations, you just have tom implement it.
    */
    constructor(maxCredit:int) 
    requires maxCredit > 0;
    ensures stateInvariant()
    ensures getBalance() == 0;
    ensures getCredit() == getMaxCredit()
    ensures getCredit() == maxCredit;
    {
        balance := 0;
        credit := maxCredit;
        CREDIT := maxCredit;
    }

   /**
        Specify and implement method withdraw below. The functionality of this method
        emulates the a deposit operation on bank account. As such, this
        operation increases the balance of the account accodring
        to the amount withdrawed. However, if the account has used some of its credit,
        it necessary to rplensih the credit before increasing the account's balance.

        In the specification define the weakest preconditions and 
        the strongest postconditions you can think of. Implement the method
        so that it satisfies the post-conditions assuming the pre-conditions.
    */
    method Deposit(value:int)
    modifies this`balance, this`credit
    requires stateInvariant()
    requires value > 0
    ensures stateInvariant()
    ensures credit + balance == old(credit) + old(balance) + value
    ensures old(credit) + value > CREDIT ==> credit == CREDIT && balance == old(balance) + (old(credit) + value - CREDIT)
    ensures old(credit) + value <= CREDIT ==> credit == old(credit) + value && balance == 0
    {
        credit := credit + value;

        if(credit > CREDIT) {
            balance := balance + credit - CREDIT;
            credit := CREDIT;
        }

        assert 0 <= credit <= CREDIT;
    }

   /**
        Specify and implement method withdraw below. The functionality of this method
        emulates a withdrawl operation on bank account. As such, this
        operation reduces the balance of the account accodring
        to the amount withdrawed. Take note that the account balance
        should always be non-negative but it is possible to whithdraw
        beyonf the balance of account. However the credit should never be
        surpassed.

        In the specification define the weakest preconditions and 
        the strongest postconditions you can think of. Implement the method
        so that it satisfies the post-conditions assuming the pre-conditions.
    */
    method Withdraw(value:int)
    modifies this`balance, this`credit
    requires stateInvariant()
    requires 0 < value <= balance + credit
    ensures stateInvariant()
    ensures value + credit + balance == old(balance) + old(credit);
    {
        balance := balance - value;
        if (balance < 0) {
            credit := credit + balance;
            balance := 0;
        }
    }

   /**
        Main method. Creates an account and execites deposit and
        withdrawl operation on it. This method does not require
        annotations, you just have tom implement it.
    */
    method main() {
        var ca:CreditAccount := new CreditAccount(1000);
        assert ca.getBalance() == 0;
        assert ca.getCredit() == 1000;
        assert ca.getCredit() == ca.getMaxCredit();
        assert ca.stateInvariant();

        assert ca.credit == ca.CREDIT;
        ca.Deposit(100);
        assert ca.stateInvariant();
        assert ca.getBalance() == 100;

        ca.Withdraw(600);
        assert ca.getBalance() == 0;
        assert ca.getCredit() == 500;

        ca.Deposit(100);
        assert ca.getBalance() == 0;
        assert ca.getCredit() == 600;
    }
}
