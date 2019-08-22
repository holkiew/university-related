#ifndef LIFTSEAT_H
#define LIFTSEAT_H
#include "Skier.h"
#include <vector>
#include <thread>
#include "LiftSeat.h"

using namespace std;
class Skier;
class LiftSeat{
private:
	thread *t;
	vector<Skier*> *skiers;
	int timeSec;
	bool lifted;
public:
	LiftSeat(vector<Skier*> *_skiers, int _timeSec);
	void liftSkiers();
	bool isLifted();
	vector<Skier*> *passSkiers();
	int amountOfSkiers();
	~LiftSeat();
};

#endif // LIFTSEAT_H
