#ifndef SKILIFT_H
#define SKILIFT_H
#include <queue>
#include "Route.h"
#include "Skier.h"
#include "LiftSeat.h"
#include <thread>
#include <iostream>
class Skier;
class LiftSeat;
extern void print_ts(int y,int x, string msg);
extern void clr_line_ts(int y, int x, int lengthToClear);

using namespace std;
class SkiLift{
private:
	const string name;
	const int liftID;
	Route slope, lift;
	int singleLiftSeatCapacity;	//single seat capacity
	int seatIntervalMilisec;	//time between two seats take passengers
	int price;
	int awaitingForBus;
	int liftingSkiiersCount;
	queue<Skier*> _queue;
	queue<LiftSeat*> seats;
	mutex _queue_mutex,awaitingForBus_mutex,queuePrinter_mutex, occupiedLiftSeatsPrinter_mutex, awaitingForBusPrinter_mutex;
	condition_variable cv_queue_printer_nc, cv_occupiedSeats_printer_nc, cv_awaitingForBus_printer_nc;
	void addToLiftingSkiiersCount();
	void substractLiftingSkiiersCount();
public:
	SkiLift(string _name, int _lengthSecSlope, int _lengthSecLift, int _singleLiftSeatCapacity, int _seatIntervalMilisec, int _price, const int _liftID);
	int getSlopeSkiiTimeSec();
	void addToQueue(Skier *skier);
	void liftSkiers();
	void checkIfLifted();
	void addAwaitingForBus();
	int getAwaitingForBus(int amount);
	void queuePrinter_nc();
	void occupiedLiftSeatsPrinter_nc();
	void awaitingForBusPrinter_nc();
	
  thread spawnThreadliftSkiers();
  thread spawnThreadcheckIfLifted();
  thread spawnThreadqueuePrinter_nc();
  thread spawnThreadoccupiedLiftSeatsPrinter_nc();
  thread spawnThreadawaitingForBusPrinter_nc();


};

#endif // SKILIFT_H
