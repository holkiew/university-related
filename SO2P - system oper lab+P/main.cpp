
#include "Skier.h"
#include "SkiLift.h"
#include "City.h"
#include "Route.h"
#include "Bus.h"
#include "LiftSeat.h"
#include "SlopesStats.h"
#include <ncurses.h>
#include <sstream>

extern void print_empty_rectangle(int y, int x, int h, int w);
extern void print_line_y(int y, int x, int length);
using namespace std;


void print_scene(){
	///wyciag C
	print_empty_rectangle(2,5,7,10);
	mvprintw(1,5,"Zjazd C");
	print_line_y(2,18,8);
	print_line_y(2,19,8);
	for(int x=18; x<40;x++)
		mvprintw(10,x,"=");
	///wyciag B
	print_empty_rectangle(2,60,7,10);
	mvprintw(1,60,"Zjazd B");
	print_line_y(2,55,8);
	print_line_y(2,56,8);
	for(int x=56; x>39;x--)
		mvprintw(10,x,"=");
	///wyciag A
	print_empty_rectangle(11,30,7,10);
	mvprintw(9,30,"Zjazd A");
	print_line_y(11,43,8);
	print_line_y(11,44,8);
	
	///zjazd C do A
	print_empty_rectangle(13,7,7,8);
	mvprintw(12,7,"C do Bus stop");
	for(int x=15; x<27;x++)
		mvprintw(19,x,"=");
	mvprintw(19,27,">");
	
	///zjazd B do A
	print_empty_rectangle(13,60,7,8);
	mvprintw(12,60,"B do Bus stop");
	for(int x=59; x>41;x--)
		mvprintw(19,x,"=");
	mvprintw(19,41,"<");
	
	///miasto
	mvprintw(23,30,"MIASTO");
	
	///bus stop
	mvprintw(19,30,"BUS STOP");
    
	///cols & rows marks
	/*for(int y =0;y<24;y++)
		mvprintw(y,0,to_string(y).c_str());
	for(int x =0;x<80;x++)
		mvprintw(0,x,to_string(x).c_str());*/
	
	refresh();
}
	
int main(int argc, char **argv)
{
	initscr();
    print_scene();

	///BASE STRUCTURE
    City *city = new City("City A",300);	///intervals between possible spawn
	Bus *bus = new Bus("Bus1",10,2);			///name,capacity,routeLength
    vector <SkiLift*> lifts;
		lifts.push_back(new SkiLift("SkiLift A", 2, 2, 2, 500, 100, LIFT_A));	///name,lengthSlope(sec), lengthLift(sec), singleSeatCapacity, intervalOfLiftSeats(milisec), price, ID
		lifts.push_back(new SkiLift("SkiLift B", 4, 4, 1, 700, 100, LIFT_B));
		lifts.push_back(new SkiLift("SkiLift C", 2, 2, 2, 500, 100, LIFT_C));
	vector <SlopesStats*> slopesStats;
		for(int i=0; i<SLOPES_COUNT;i++)
			slopesStats.push_back(new SlopesStats(i));
	vector <thread> threadLiftSkiers;
	vector <thread> threadcheckIfLifted;
	vector <thread> threadqueuePrinter_nc;
	vector <thread> threadoccupiedLiftSeatsPrinter_nc;
	vector <thread> threadslopeStatistiscPrinter_nc;
	
		
	///CREATING THREADS
	thread tCity = city->spawnThread();
	thread tBus = bus->spawnThread(city, &lifts, &slopesStats);
	thread tBusStop_printer = lifts.at(LIFT_A)->spawnThreadawaitingForBusPrinter_nc();
	thread tCity_printer = city->spawnThreadSkiersAmountPrinter_nc();
	for(int i = 0; i<lifts.size();i++)
	{
		threadLiftSkiers.push_back(lifts.at(i)->spawnThreadliftSkiers());
		threadcheckIfLifted.push_back(lifts.at(i)->spawnThreadcheckIfLifted());
		threadqueuePrinter_nc.push_back(lifts.at(i)->spawnThreadqueuePrinter_nc());
		threadoccupiedLiftSeatsPrinter_nc.push_back(lifts.at(i)->spawnThreadoccupiedLiftSeatsPrinter_nc());
	}
	for(int i=0;i<SLOPES_COUNT;i++)
	{
		threadslopeStatistiscPrinter_nc.push_back(slopesStats.at(i)->spawnThreadslopeStatisticsPrinter_nc());
	}
	
	
	///joins
	tCity.join();
	tBus.join();
	tBusStop_printer.join();
	tCity_printer.join();
	
	for(int i = 0; i<lifts.size();i++)
	{
		threadcheckIfLifted.at(i).join();
		threadLiftSkiers.at(i).join();
		threadqueuePrinter_nc.at(i).join();
		threadoccupiedLiftSeatsPrinter_nc.at(i).join();
	}
	for(int i=0;i<SLOPES_COUNT;i++)
	{
		threadslopeStatistiscPrinter_nc.at(i).join();
	}
	
	getch();
	endwin();
   return 0;
}








/*
class SkiLift;

class Route{	//time which takes to reach point B from A
    private:
    const int lengthSec;
    public:
    Route(int _lengthSec) : lengthSec(_lengthSec){};
    int getLengthSec(){return lengthSec;}
};

class Skier{
private:
    int money;
    bool outOfMoney;
    int queueSize[LIFTS_COUNT];      //last remembered queue size
    bool stage[STAGES_OF_SLOPE];        //point of slope
    bool isBeignLifted;
    mutex mtx;
public:
    condition_variable cv;
    Skier(int _money) : money(_money), outOfMoney(false), queueSize({0}), stage({false,true}), isBeignLifted(false){}
    ~Skier(){}
    int getMoney(){return money;}
    bool substractMoney(int amount){
        if(money-amount<0)
        {
            outOfMoney=true;
            return false;
        }
        else
        {
            money-=amount;
            return true;
        }
    }
    void rememberQueue(int lift, int queue){
      queueSize[lift]=queue;
    }
    //returns which lift do we take
    int chooseDirection(){
      int min=queueSize[0], where=0;
      for(int i=1;i<LIFTS_COUNT;i++)
      {
        if(queueSize[i]<min)
        {
          min=queueSize[i];
          where=i;
        }
      }
      return where;
    }
    //returns current stage
    int whereIsSkiier(){
      int where;
      for(int i=0; i<STAGES_OF_SLOPE;i++)
      {
        if(stage[i])
        {
          where=i;
          break;
        }
      }
      return where;
    }
    void brain(SkiLift &liftA){
      unique_lock<mutex> lck(mtx);
      switch (chooseDirection())
      {
        case LIFT_A:
        this_thread::sleep_for(chrono::milliseconds(liftA.getSlopeSkiiTimeSec()*1000)); //skiing
        liftA.addToQueue(this);
        break;
      }
    }
};

class City{
private:
    const string name;
    int awaitingSkiers;
	int spawningTimeMilisec;
public:
    City(string _name, int _spawningTimeMilisec) : name(_name), awaitingSkiers(0), spawningTimeMilisec(_spawningTimeMilisec){};
    void addSkier()
    {
        for(;;)
        {
            this_thread::sleep_for(chrono::milliseconds(spawningTimeMilisec));
			awaitingSkiers++;
        }
    }
    int takeSkiers(int takenSkiers)   //zwraca ilosc zabranych
    {
        if(takenSkiers>=awaitingSkiers)
        {
            int buffor=awaitingSkiers;
            awaitingSkiers=0;
            return buffor;
        }
        else
        {
            awaitingSkiers-=takenSkiers;
            return takenSkiers;
        }
    }
    int getAwaitingSkiers(){return awaitingSkiers;}
    thread spawnThread()
    {
        return thread([this] { addSkier(); });
    }
};

class LiftSeat{
private:
	thread *t;
	vector<Skier*> *skiers;
	int timeSec;
	bool lifted;
public:
	LiftSeat(vector<Skier*> *_skiers, int _timeSec) : skiers(_skiers), timeSec(_timeSec), lifted(false)
	{
		t=new thread([=] { liftSkiers(); });
	};
	void liftSkiers(){
    t->detach();
		this_thread::sleep_for(chrono::milliseconds(timeSec*1000));
		lifted=true;
	}
	bool isLifted(){return lifted;}
	~LiftSeat(){
		delete t;
		delete skiers; //only vector containing skiers, not skiers
	}
};

class SkiLift{
private:
	const string name;
	Route slope, lift;
	int singleLiftSeatCapacity;	//single seat capacity
	int seatIntervalMilisec;	//time between two seats take passengers
  int price;
	queue<Skier*> _queue;
	queue<LiftSeat*> seats;
	mutex _queue_mutex;
public:
	SkiLift(string _name, int _lengthSecSlope, int _lengthSecLift, int _singleLiftSeatCapacity, int _seatIntervalMilisec, int _price):
		name(_name),
		slope(_lengthSecSlope),
		lift(_lengthSecLift),
		singleLiftSeatCapacity(_singleLiftSeatCapacity),
		seatIntervalMilisec(_seatIntervalMilisec),
    price(_price){};
  int getSlopeSkiiTimeSec(){return slope.getLengthSec();}
	void addToQueue(Skier *skier){
		lock_guard<mutex> guard(_queue_mutex);
    	_queue.push(skier);
	}
	void liftSkiers(){
		vector<Skier*> *seatWithSkiers;
		for(;;)
		{
			seatWithSkiers=new vector<Skier*>();
			//load passengers
      cout<<"Znajduje sie :"<<_queue.size()<<" osob w kolejce"<<endl;
			for(int i=0;i<singleLiftSeatCapacity;i++)
			{
        if(_queue.size()!=0)
        {
          if(_queue.front()->substractMoney(price))
            seatWithSkiers->push_back(_queue.front());
          _queue.pop();
        }
			}
			//send seat
			seats.push(new LiftSeat( seatWithSkiers, lift.getLengthSec() ));
      cout<<"zaladowano narciarzy i wyslano"<<endl;
      this_thread::sleep_for(chrono::milliseconds(seatIntervalMilisec));
		}
	}
  void checkIfLifted(){
    for(;;)
    {
      if(seats.size()>0 && seats.front()->isLifted() )  //if seat ended his jurney
      {
        //przekaz komus narciarzy
        cout<<"Odebrano narciarzy"<<endl;
        delete seats.front();
        seats.pop();
      }
      this_thread::sleep_for(chrono::milliseconds(seatIntervalMilisec-50));
    }
  }

  thread spawnThreadliftSkiers(){
        return thread([=] { liftSkiers(); });
    }
  thread spawnThreadcheckIfLifted(){
        return thread([=] { checkIfLifted(); });
    }


};

class Bus{
private:
    const int capacity;
    const string name;
    Route route;
    int passengers;

    int addPassengers (int amount){  //returns amount of added passengers
        if(amount+passengers>capacity)
        {
            passengers+=capacity;
            return capacity;
        }
        else
        {
            passengers+=amount;
            return amount;
        }
    }
    int inOutPassengersTimeMilisec(int amount){return amount*20;}
    int releasePassengers(int amount){
        if(amount>passengers)
        {
            int buffor=passengers;
            passengers=0;
            return buffor;
        }else
        {
            passengers-=amount;
            return amount;
        }

    }
    int remainingSeats(){return capacity-passengers;}
public:
    Bus(string _name, int _capacity, int routeLength) : capacity(_capacity), name(_name), route(routeLength){}
    void rideRoute(City &city, SkiLift &liftA){			//from - to
        int amount;
        for(;;) //route begins at city
        {
            srand(time(NULL));
            amount = city.takeSkiers(remainingSeats());                                     //city action
			      cout<<"Miejsce w autobusie: "<<remainingSeats()<<endl;
            addPassengers(amount);
			      cout<<"Zabrano "<<amount<<" pasazerow z miasta"<<endl;
            this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount))); //gettin in, time
            this_thread::sleep_for(chrono::milliseconds(route.getLengthSec()*1000));        //routing time
			      amount=releasePassengers(this->capacity);
			      cout<<"Wysiadlo "<<amount<<" pasazerow z autobusu"<<endl;
			      this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount)));	//getting out, time
            for(int i=0;i<amount;i++)
            {
              liftA.addToQueue(new Skier(rand()%moneyRange));
            }
			      this_thread::sleep_for(chrono::milliseconds(route.getLengthSec()*1000));
        }
    }
	thread spawnThread(City *city, SkiLift *liftA){
        return thread([=] { rideRoute(*city, *liftA); });
    }

};

*/