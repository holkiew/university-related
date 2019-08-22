#include "Bus.h"
extern void print_ts(int y,int x, string msg);
extern void clr_line_ts(int y, int x, int lengthToClear);

int Bus::addPassengers(int amount)
{
	// returns amount of added passengers
	if (amount + passengers > capacity) {
		passengers += capacity;
		return capacity;
	} else {
		passengers += amount;
		return amount;
	}
}
int Bus::inOutPassengersTimeMilisec(int amount)
{
	return amount * passengerLoadTime;
}
int Bus::releasePassengers(int amount)
{
	if (amount > passengers) {
		int buffor = passengers;
		passengers = 0;
		return buffor;
	} else {
		passengers -= amount;
		return amount;
	}
}
int Bus::remainingSeats()
{
	return capacity - passengers;
}
Bus::Bus(string _name, int _capacity, int routeLength)
	: capacity(_capacity)
	, name(_name)
	, route(routeLength)
	, passengers(0)
	, passengerLoadTime(150)
{
}

void Bus::rideRoute(City& city, vector<SkiLift*> &lifts, vector<SlopesStats*> &slopes) /// from - to
{
	
	int x=22,y=30;	///coordinates of printed text
	int amount;
	string str;
	for (;;) { 																					/// route begins at city
		srand(time(NULL));
		amount = city.takeSkiers(remainingSeats()); 											///taking passengers from cit
		
		addPassengers(amount);
		str="Pasazerowie wchodza do autobusu";
		print_ts(x,y,str.c_str());
		this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount))); 		/// gettin in, time
		
		clr_line_ts(x,y,str.size());
		str=to_string(amount) + " --> STOK";
		print_ts(x-1,y,str.c_str());
		this_thread::sleep_for(chrono::milliseconds(route.getLengthSec() * 1000)); 				/// routing time
		
		clr_line_ts(x-1,y,str.size());
		str="Pasazerowie wychodza z autobusu";
		print_ts(x-2,y,str.c_str());
		amount = releasePassengers(this->capacity);
		this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount))); 		/// getting out, time
		
		for (int i = 0; i < amount; i++) {
			new Skier(rand() % MONEY_RANGE, lifts,slopes);												///giving passengers to lift, and making them alive
		}
		
		amount=lifts.at(LIFT_A)->getAwaitingForBus(remainingSeats());							///getting awaiting, broke passengers
		addPassengers(amount);
		clr_line_ts(x-2,y,str.size());
		str="Pasazerowie wchodza do autobusu";
		print_ts(x-2,y,str.c_str());
		this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount))); 		///getting in, time
		
		clr_line_ts(x-2,y,str.size());
		str=to_string(amount)+" --> MIASTO";
		print_ts(x-1,y,str.c_str());
		this_thread::sleep_for(chrono::milliseconds(route.getLengthSec() * 1000));				///routing time
		
		amount = releasePassengers(this->capacity);
		clr_line_ts(x-1,y,str.size());
		str="Pasazerowie wychodza z autobusu";
		print_ts(x,y,str.c_str());
		this_thread::sleep_for(chrono::milliseconds(inOutPassengersTimeMilisec(amount)));		///releasing passengers from lift
		
		clr_line_ts(x,y,str.size());
	}
}

thread Bus::spawnThread(City* city, vector<SkiLift*> *lifts,vector<SlopesStats*> *slopes)
{
	return thread([=] { rideRoute(*city, *lifts, *slopes); });
} 
