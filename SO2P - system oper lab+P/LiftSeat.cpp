#include "LiftSeat.h"

	LiftSeat::LiftSeat(vector<Skier*> *_skiers, int _timeSec) : skiers(_skiers), timeSec(_timeSec), lifted(false)
	{
		t=new thread([=] { liftSkiers(); });
		t->detach();
	};
	void LiftSeat::liftSkiers(){
		this_thread::sleep_for(chrono::milliseconds(timeSec*1000));
		lifted=true;
	}
	bool LiftSeat::isLifted(){return lifted;}
	
	vector<Skier*>* LiftSeat::passSkiers(){
		return skiers;
	}
	int LiftSeat::amountOfSkiers(){
		return skiers->size();
	}	
	
	LiftSeat::~LiftSeat(){
		delete t;
		delete skiers; //only vector containing skiers, not skiers
	}
