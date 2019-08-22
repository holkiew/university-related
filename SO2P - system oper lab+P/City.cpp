#include "City.h"
extern void print_ts(int y,int x, string msg);
extern void clr_line_ts(int y, int x, int lengthToClear);
City::City(string _name, int _spawningTimeMilisec) : name(_name), awaitingSkiers(0), spawningTimeMilisec(_spawningTimeMilisec){};
    void City::addSkier()
    {
        for(;;)
        {
			srand(time(NULL));
            this_thread::sleep_for(chrono::milliseconds(spawningTimeMilisec));
			if((rand()  % 1000) > 500)
			{
				awaitingSkiers++;
				cv_skiersAmountPrinter.notify_one();
			}
        }
    }
    int City::takeSkiers(int takenSkiers)   //returns amount of taken skiers (to the bus)
    {
		lock_guard<mutex> guard(awaitingSkiers_mutex);
        if(takenSkiers>=awaitingSkiers)
        {
            int buffor=awaitingSkiers;
            awaitingSkiers=0;
			cv_skiersAmountPrinter.notify_one();
            return buffor;
        }
        else
        {
            awaitingSkiers-=takenSkiers;
			cv_skiersAmountPrinter.notify_one();
            return takenSkiers;
        }
    }
	void City::skiersAmountPrinter_nc()
	{
		int y=23,x=36;
		string buffor=":";
		print_ts(y,x,buffor.c_str());
		x++;
		buffor="";
		unique_lock<mutex> lk(skiersAmountPrinter_mutex);
		for(;;)
		{
			cv_skiersAmountPrinter.wait(lk);
			clr_line_ts(y,x,buffor.size());
			buffor=to_string(awaitingSkiers);
			print_ts(y,x,buffor.c_str());
			
		}
	}
    int City::getAwaitingSkiers(){return awaitingSkiers;}
    thread City::spawnThread()
    {
        return thread([=] { addSkier(); });
    }
	thread City::spawnThreadSkiersAmountPrinter_nc()
    {
        return thread([=] { skiersAmountPrinter_nc(); });
    }
