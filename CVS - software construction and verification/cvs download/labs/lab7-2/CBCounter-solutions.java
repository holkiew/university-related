import java.util.concurrent.locks.*;

/*@

predicate CounterP(CCounter c, int v, int m) = 
	        c.counter |-> ?s 
	    &*& s != null
	    &*& BCounterInv(s, v, m);


predicate_ctor CCounterSharedState(CCounter c)() =
	        c.counter |-> ?s 
	    &*& s != null
	    &*& BCounterInv(s, _, _);

predicate_ctor CCounterSharedStateNotZero(CCounter c)() =
	        c.counter |-> ?s 
	    &*& s != null
	    &*& BCounterInv(s, ?v, _)
	    &*& v > 0;

predicate_ctor CCounterSharedStateNotMax(CCounter c)() =
	        c.counter |-> ?s 
	    &*& s != null
	    &*& BCounterInv(s, ?v, ?m)
	    &*& v < m;

predicate CCounterInv(CCounter c;) =
	    c.mon |-> ?l
	&*& c.nonZero |-> ?cz
	&*& c.nonMax  |-> ?cm
	&*& l != null
	&*& cz != null
	&*& cm != null
	&*& lck(l, 1, CCounterSharedState(c))
	&*& cond(cz,CCounterSharedState(c),CCounterSharedStateNotZero(c))
	&*& cond(cm,CCounterSharedState(c),CCounterSharedStateNotMax(c));
	
@*/

/*@
predicate IncInv(Inc i;) = i.c |-> ?o &*& o != null &*& [_]CCounterInv(o);

predicate DecInv(Dec i;) = i.c |-> ?o &*& o != null &*& [_]CCounterInv(o);

predicate Counter_frac(real f) = true;

@*/

class CCounter {

	BCounter counter;
	ReentrantLock mon;
	Condition nonZero;
	Condition nonMax;
	
	CCounter(int max) 
	//@ requires 0 <= max;
	//@ ensures CCounterInv(this);
	{
		counter = new BCounter(max);
		//@ close CCounterSharedState(this)();
		//@ close enter_lck(1,CCounterSharedState(this));
		mon = new ReentrantLock();

		//@ close set_cond(CCounterSharedState(this), CCounterSharedStateNotZero(this));
		nonZero = mon.newCondition();
		//@ close set_cond(CCounterSharedState(this), CCounterSharedStateNotMax(this));
		nonMax = mon.newCondition();
		//@ close CCounterInv(this);
	}
	
	void inc() 
	//@ requires [?f]CCounterInv(this);
	//@ ensures [f]CCounterInv(this);
	{
		//@ open CCounterInv(this);
		mon.lock();
		//@ open CCounterSharedState(this)();
		while(counter.get() == counter.getMax())
		/*@
		invariant this.counter |-> ?v &*& v != null
		&*& BCounterInv(v, _, _) &*& [f]this.mon |-> ?m &*& m != null
		&*& [f]lck(m, -1, CCounterSharedState(this))
		&*& [f]this.nonZero |-> ?nzc &*& nzc != null
		&*& [f]cond(nzc, CCounterSharedState(this), CCounterSharedStateNotZero(this))
		&*& [f]this.nonMax |-> ?nmc	&*& nmc != null
		&*& [f]cond(nmc, CCounterSharedState(this), CCounterSharedStateNotMax(this));
		@*/
		{
			//@ close CCounterSharedState(this)();
			nonMax.await();
			//@ open CCounterSharedStateNotMax(this)();
		}
		//@ open BCounterInv(counter,_,_);
		this.counter.inc();
		//@ close CCounterSharedStateNotZero(this)();		
		nonZero.signal();
		mon.unlock();
		//@ close [f]CCounterInv(this);
	}

	void dec() 
	//@ requires [?f]CCounterInv(this);
	//@ ensures [f]CCounterInv(this);	
	{
		//@ open CCounterInv(this);
		mon.lock();
		//@ open CCounterSharedState(this)();
		while(counter.N == 0)
		/*@
		invariant this.counter |-> ?v &*& v != null
		&*& BCounterInv(v, _, _) &*& [f]this.mon |-> ?m &*& m != null
		&*& [f]lck(m, -1, CCounterSharedState(this))
		&*& [f]this.nonZero |-> ?nzc &*& nzc != null
		&*& [f]cond(nzc, CCounterSharedState(this), CCounterSharedStateNotZero(this))
		&*& [f]this.nonMax |-> ?nmc	&*& nmc != null
		&*& [f]cond(nmc, CCounterSharedState(this), CCounterSharedStateNotMax(this));
		@*/
		{
			//@ close CCounterSharedState(this)();
			nonZero.await();
			//@ open CCounterSharedStateNotZero(this)();
			
		}
		//@ open BCounterInv(counter,_,_);
		this.counter.dec();
		//@ close CCounterSharedStateNotMax(this)();
		nonMax.signal();
		mon.unlock();
		//@ close [f]CCounterInv(this);
	}
	
  	int get()
  	//@ requires CCounterInv(this);
  	//@ ensures CCounterInv(this);
  	{
  		int n;
		//@ open CCounterInv(this);
		mon.lock();
		//@ open CCounterSharedState(this)();
  		n = counter.get();
  		//@ close CCounterSharedState(this)();
  		mon.unlock();
  		return n; 
		//@ close CCounterInv(this);  		
  	} 

  	int getMax()
  	//@ requires CCounterInv(this);
  	//@ ensures CCounterInv(this);
  	{
  		int m;
		//@ open CCounterInv(this);
		mon.lock();
		//@ open CCounterSharedState(this)();
  		m = counter.get();
  		//@ close CCounterSharedState(this)();
  		mon.unlock();
  		return m; 
		//@ close CCounterInv(this);  		
  		
  	}


	public static void main(String[] args) 
	//@ requires true;
	//@ ensures true;
	{

		int MAX = 30;
		CCounter c = new CCounter(MAX);
       
		//@real frac = 1;
		//@ close Counter_frac(frac);
       
		for(int i = 0; i < 100; i++)
		//@ invariant Counter_frac(?f) &*& [f]CCounterInv(c);
		{       	  
			//@ close Counter_frac(f/2);
			new Thread(new Inc(c)).start();
			//@ close Counter_frac(f/4);
			new Thread(new Dec(c)).start();
			//@ close Counter_frac(f/4);
		}      
	}
}

class Inc implements Runnable {

	//@ predicate pre() = IncInv(this);
	//@ predicate post() = true;
  
	CCounter c;
  
	Inc(CCounter c)
	//@ requires c != null &*& Counter_frac(?f) &*& [f]CCounterInv(c);
	//@ ensures IncInv(this);
	{
		this.c = c;
		//@ close IncInv(this);
	}
  
	public void run() 
	//@ requires pre();
	//@ ensures post();  
	{
		c.inc();
		//@ close post();
	}
}

class Dec implements Runnable {

	//@ predicate pre() = DecInv(this);
	//@ predicate post() = true;
  
	CCounter c;
  
	Dec(CCounter c) 
	//@ requires c != null &*& Counter_frac(?f) &*& [f]CCounterInv(c);
	//@ ensures DecInv(this);
	{
		this.c = c;
	}
	
	public void run() 
	//@ requires pre();
	//@ ensures post();  
	{
		//@ open DecInv(this);
		c.dec();
		//@ close post();
	}
}