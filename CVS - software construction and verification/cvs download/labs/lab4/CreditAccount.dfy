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

   /**
        Constructor, given a maximum credit, it creates a new instanec of
		CreditAccount where the user can whitdraw beyind her current balance
		but limited by the credit.
    */
    constructor(maxCredit:int)

   /**
        Specify and implement method Deposit below. The functionality of this method
        emulates the a deposit operation on bank account. As such, this
        operation increases the balance of the account accodring
        to the amount withdrawed. However, if the account has used some of its credit,
        it necessary to rplensih the credit before increasing the account's balance.

        In the specification define the weakest preconditions and 
        the strongest postconditions you can think of. Implement the method
        so that it satisfies the post-conditions assuming the pre-conditions.
    */
    method Deposit(value:int)

   /**
        Specify and implement method Withdraw below. The functionality of this method
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

   /**
        Main method. Creates an account and execites deposit and
        withdrawl operation on it. This method does not require
        annotations, you just have tom implement it.
    */
    method main()
}
