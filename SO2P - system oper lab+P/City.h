#ifndef CITY_H
#define CITY_H
#include <thread>
#include <string>
#include <mutex>
#include <condition_variable>
using namespace std;
class City{
private:
    const string name;
    int awaitingSkiers;
	int spawningTimeMilisec;
	mutex awaitingSkiers_mutex, skiersAmountPrinter_mutex;
	condition_variable cv_skiersAmountPrinter;
public:
    City(string _name, int _spawningTimeMilisec);
    void addSkier();
    int takeSkiers(int takenSkiers);
    int getAwaitingSkiers();
	void skiersAmountPrinter_nc();
	
   thread spawnThread();
   thread spawnThreadSkiersAmountPrinter_nc();
};

#endif // CITY_H
