#ifndef BUS_H
#define BUS_H
#include "Route.h"
#include "City.h"
#include "SkiLift.h"
#include <string>
#include <thread>
using namespace std;
class Bus{
private:
    const int capacity;
    const string name;
	int passengerLoadTime;
    Route route;
    int passengers;
    int addPassengers (int amount);
    int inOutPassengersTimeMilisec(int amount);
    int releasePassengers(int amount);
    int remainingSeats();
public:
    Bus(string _name, int _capacity, int routeLength);
    void rideRoute(City &city, vector<SkiLift*> &lifts, vector<SlopesStats*> &slopes);
	
  thread spawnThread(City *city, vector<SkiLift*> *lifts,vector<SlopesStats*> *slopes);

};

#endif // BUS_H
